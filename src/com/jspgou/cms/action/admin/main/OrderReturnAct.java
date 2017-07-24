package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.OrderItem;
import com.jspgou.cms.entity.OrderReturn;
import com.jspgou.cms.entity.PaymentPlugins;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopScore;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.OrderReturnMng;
import com.jspgou.cms.manager.PaymentPluginsMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.manager.ShopScoreMng;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class OrderReturnAct {
	private static final Logger log = LoggerFactory.getLogger(OrderReturnAct.class);

	@RequestMapping("/orderReturn/v_list.do")
	public String list(Integer status,Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(status,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("status",status);
		return "orderReturn/list";
	}

	@RequestMapping("/orderReturn/v_view.do")
	public String view(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}
	
	@RequestMapping("/orderReturn/o_affirm.do")
	public String affirm(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn entity = manager.findById(id);
		entity.setStatus(2);
		manager.update(entity);
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}
	
	@RequestMapping("/orderReturn/o_sendback.do")
	public String sendback(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn entity = manager.findById(id);
		entity.setStatus(3);
		manager.update(entity);
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}
	
	@RequestMapping("/orderReturn/o_accomplish.do")
	public String accomplish(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn entity = manager.findById(id);
		entity.setStatus(7);
		Order order =manager.findById(id).getOrder();
		order.setStatus(3);
		manager.update(entity);
		orderMng.updateByUpdater(order);
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}
	
	@RequestMapping("/orderReturn/o_reimburse.do")
	public String reimburse(Long id, HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		Website web = SiteUtils.getWeb(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn entity = manager.findById(id);
		PaymentPlugins paymentPlugins = paymentPluginsMng.findByCode("alipay");
		if(paymentPlugins!=null){	
	    	PrintWriter out = null;
			try {
				String aliURL = alipayReturn(paymentPlugins,web,entity, request, model);
				response.setContentType("text/html;charset=UTF-8");
				out = response.getWriter();
				out.print(aliURL);
			} catch (Exception e) {
			}finally{
				out.close();
			}
    	}else{
    		ShopMember shopMember = entity.getOrder().getMember();
    		shopMember.setMoney(shopMember.getFreezeScore()+entity.getMoney());
    		shopMemberMng.update(shopMember);
    	}
		entity.setStatus(6);
		manager.update(entity);
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}

	private String alipayReturn(PaymentPlugins paymentPlugins,Website web,OrderReturn orderReturn,
			HttpServletRequest request,ModelMap model){
		////////////////////////////////////请求参数//////////////////////////////////////
		//必填参数//
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date1=sdf.format(date);
		//退款批次号。格式为：退款日期（8位当天日期）+流水号（3～24位，不能接受“000”，但是可以接受英文字符）
//		String batch_no = request.getParameter("batch_no");
		String batch_no = date1.concat(String.valueOf(orderReturn.getId()*100));
		//退款请求时间
//		String refund_date = request.getParameter("refund_date");
		String refund_date = sdf1.format(date);
		//退款总笔数
//		String batch_num = request.getParameter("batch_num");
		String batch_num =String.valueOf(1);
		//单笔数据集
//		String detail_data =  new String(request.getParameter("detail_data").getBytes("ISO-8859-1"),"gbk");
		String detail_data = orderReturn.getOrder().getId().toString()+"^"+1.00+"^"+orderReturn.getShopDictionary().getName();
		//格式：第一笔交易#第二笔交易#第三笔交易
	        //第N笔交易格式：交易退款信息
	        //交易退款信息格式：原付款支付宝交易号^退款总金额^退款理由
	        //注意：
	        //1.detail_data中的退款笔数总和要等于参数batch_num的值
	        //2.detail_data的值中不能有“^”、“|”、“#”、“$”等影响detail_data的格式的特殊字符
	        //3.detail_data中退款总金额不能大于交易总金额
	        //4.一笔交易可以多次退款，只需要遵守多次退款的总金额不超过该笔交易付款时金额。
	        //5.不支持退分润功能
		//选填参数（以下两个参数不能同时为空）
		//卖家支付宝账号
//		String seller_email = request.getParameter("seller_email");
		String seller_email = paymentPlugins.getSellerEmail();
		//卖家用户ID
//		String seller_user_id = request.getParameter("seller_user_id");
	    String seller_user_id = paymentPlugins.getSellerKey();
		//服务器页面跳转同步通知页面
		String notify_url="http://"+web.getDomain()+"/cart/aliReturn.jspx";
		//////////////////////////////////////////////////////////////////////////////////
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
	 	sParaTemp.put("partner", paymentPlugins.getPartner());
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("seller_user_id", seller_user_id);
		
		
        sParaTemp.put("batch_no", batch_no);
        sParaTemp.put("refund_date", refund_date);
        sParaTemp.put("batch_num", batch_num);
        sParaTemp.put("detail_data", detail_data);
        sParaTemp.put("notify_url", notify_url);
		//构造函数，生成请求URL  
		String sHtmlText=null;
		try {
			sHtmlText = refund_fastpay_by_platform_pwd(sParaTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
        //out.println(sHtmlText);
		return sHtmlText;
	}
	
	/**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	
	/**
     * 构造即时到账批量退款有密接口
     * @param sParaTemp 请求参数集合
     * @return 支付宝返回表单提交HTML信息
     * @throws Exception 
     */
    public static String refund_fastpay_by_platform_pwd(Map<String, String> sParaTemp) throws Exception {
    	//增加基本配置
        sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
        sParaTemp.put("_input_charset", "UTF-8");
        String strButtonName="退款";
        return buildForm(sParaTemp, ALIPAY_GATEWAY_NEW,"get",strButtonName);
    }
    
    /**
     * 构造提交表单HTML数据
     * @param sParaTemp 请求参数数组
     * @param gateway 网关地址
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildForm(Map<String, String> sParaTemp, String gateway, String strMethod,
                                   String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + gateway
                      + "_input_charset=" + "UTF-8" + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = keys.get(i);
            String value = sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
    
    /**
	    * 生成要请求给支付宝的参数数组
	    * @param sParaTemp 请求前的参数数组
	    * @return 要请求的参数数组
	    */
	   private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
	       //除去数组中的空值和签名参数
	       Map<String, String> sPara = paraFilter(sParaTemp);
	       //生成签名结果
	       String mysign = buildMysign(sPara);

	       //签名结果与签名方式加入请求提交参数组中
	       sPara.put("sign", mysign);
	       sPara.put("sign_type", "MD5");

	       return sPara;
	   }
	   
	   public static String buildMysign(Map<String, String> sArray) {
	       String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
	       prestr = prestr +sArray.get("key"); //把拼接后的字符串再与安全校验码直接连接起来
	       String mysign = md5(prestr);
	       return mysign;
	   }
	   
	   public static String md5(String text) {

	       return DigestUtils.md5Hex(getContentBytes(text, "UTF-8"));

	   }
	   
	   private static byte[] getContentBytes(String content, String charset) {
	       if (charset == null || "".equals(charset)) {
	           return content.getBytes();
	       }

	       try {
	           return content.getBytes(charset);
	       } catch (UnsupportedEncodingException e) {
	           throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
	       }
	   }
	   
	   /** 
	    * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	    * @param params 需要排序并参与字符拼接的参数组
	    * @return 拼接后字符串
	    */
	   public static String createLinkString(Map<String, String> params) {

	       List<String> keys = new ArrayList<String>(params.keySet());
	       Collections.sort(keys);

	       String prestr = "";

	       for (int i = 0; i < keys.size(); i++) {
	           String key = keys.get(i);
	           String value = params.get(key);

	           if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
	               prestr = prestr + key + "=" + value;
	           } else {
	               prestr = prestr + key + "=" + value + "&";
	           }
	       }

	       return prestr;
	   }
	   
	   /** 
	    * 除去数组中的空值和签名参数
	    * @param sArray 签名参数组
	    * @return 去掉空值与签名参数后的新签名参数组
	    */
	   public static Map<String, String> paraFilter(Map<String, String> sArray) {

	       Map<String, String> result = new HashMap<String, String>();

	       if (sArray == null || sArray.size() <= 0) {
	           return result;
	       }

	       for (String key : sArray.keySet()) {
	           String value = sArray.get(key);
	           if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
	               || key.equalsIgnoreCase("sign_type")) {
	               continue;
	           }
	           result.put(key, value);
	       }

	       return result;
	   }
	
	@RequestMapping("/orderReturn/o_salesreturn.do")
	public String salesreturn(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn entity = manager.findById(id);
		entity.setStatus(5);
		
		//处理库存
		for(OrderItem item:entity.getOrder().getItems()){	
			Product product=item.getProduct();
			if(item.getProductFash()!=null){
				ProductFashion fashion=item.getProductFash();
				fashion.setStockCount(fashion.getStockCount()+item.getCount());
				product.setStockCount(product.getStockCount()+item.getCount());
				product.setSaleCount(product.getSaleCount()-item.getCount());
				product.setLiRun(product.getLiRun()-item.getCount()*(fashion.getSalePrice()-fashion.getCostPrice()));
				productFashionMng.update(fashion);
			}else{
				product.setLiRun(product.getLiRun()-item.getCount()*(product.getSalePrice()-product.getCostPrice()));
				product.setSaleCount(product.getSaleCount()-item.getCount());
				product.setStockCount(product.getStockCount()+item.getCount());
			}
			productMng.updateByUpdater(product);
		}
		//会员冻结的积分
		ShopMember member = entity.getOrder().getMember();
		member.setFreezeScore(member.getScore()-entity.getOrder().getScore());
		shopMemberMng.update(member);
		List<ShopScore> list = shopScoreMng.getlist(Long.toString(entity.getOrder().getCode()));
		for(ShopScore s:list){
			shopScoreMng.deleteById(s.getId());
		}
		
		manager.update(entity);
		model.addAttribute("order", manager.findById(id).getOrder());
		return "orderReturn/view";
	}
	
	@RequestMapping("/orderReturn/o_delete.do")
	public String delete(Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		OrderReturn[] beans = manager.deleteByIds(ids);
		for (OrderReturn bean : beans) {
			log.info("delete OrderReturn id={}", bean.getId());
		}
		return list(null,pageNo, request, model);
	}
	
	
	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		if (vldExist(id, web.getId(), errors)) {
			return errors;
		}
		return errors;
	}


	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		errors.ifEmpty(ids, "ids");
		for (Long id : ids) {
			vldExist(id, web.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Long id, Long webId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		OrderReturn entity = manager.findById(id);
		if(errors.ifNotExist(entity, OrderReturn.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private OrderReturnMng manager;
	@Autowired
	private ProductMng productMng;
	@Autowired
	private ProductFashionMng productFashionMng;
    @Autowired
	private ShopMemberMng shopMemberMng;
 	@Autowired
	private ShopScoreMng shopScoreMng;
 	@Autowired
	private PaymentPluginsMng paymentPluginsMng;
 	@Autowired
	private OrderMng orderMng;
 	
}