package com.jspgou.cms.action.front;
import static com.jspgou.cms.Constants.MEMBER_SYS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.ProtocolException;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;
import com.jspgou.cms.Alipay;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.PaymentPlugins;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.PaymentPluginsMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.web.FrontUtils;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.threadvariable.MemberThread;


/**
* This class should preserve.
* @preserve
*/
@Controller
public class AlipayAct extends Alipay {
	
	private static final Logger log = LoggerFactory.getLogger(AlipayAct.class);
	public static final String WEIXIN_AUTH_CODE_URL ="https://open.weixin.qq.com/connect/oauth2/authorize";
	/*微信授权页*/
	private static final String WECHAT_CODE = "tpl.weChatCode";
	/*微信公众号支付页面*/
	public static final String WECHAT_PUBLIC_PAY="tpl.weChatPublicPay";
	/*成功提交订单页面*/
	public static final String SUCCESSFULLY_ORDER = "tpl.successfullyOrder";
	public static final String JSAPI="JSAPI";//公众号支付
	//微信公众号支付立即购买回调地址
	public static final String PUBLIC_PAY_RETURN_URL="/WeChatPublicPayReturn.jspx";
	
	public static final String WEIXIN_AUTH_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";

	//支付宝手机网站支付
	private static final String ALIPAY_TRADE_WAP_PAY="https://openapi.alipay.com/gateway.do";
	 //支付完成后跳回会员中心
	public static final String ALIPAY_MOBILE_RETURN_URL="/shopMember/index.jspx";
	//支付宝移动端回调地址
	public static final String ALIPAY_MOBILE_PAY_NOTIFY_URL="/alipayMobilePayReturn.jspx";
	public static final String UTF8="UTF-8";
	   /**API调用客户端*/
	private static AlipayClient alipayClient;
	private static final String JSON="json";
	
		
	//在线支付订单
	@RequestMapping(value="/pay.jspx")
	public String pay(Long orderId,String code,String pay,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JDOMException, IOException{
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		if(orderId!=null&&manager.findById(orderId)!=null){
			Order order = manager.findById(orderId);
			PaymentPlugins paymentPlugins = paymentPluginsMng.findByCode(code);
			PrintWriter out = null;
			String aliURL = null;
			if(!StringUtils.isBlank(code)&&code.equals("weChatPayment")){
				return weChatPayment(paymentPlugins,web, order, request,response, model);	
				
			}else if(!StringUtils.isBlank(code)&& code.equals("alipayMobile")){
					//initAliPayUrl();
				String alipayUrl = null;
					return alipayMobile(alipayUrl,paymentPlugins,web,order,null,
							null,request,response,model);
					
			}else if(!StringUtils.isBlank(code) && code.equals("weChatPublicPay")){	
				ShopMember member = MemberThread.get();
				//用户没有登录，跳转到登录页
				if(member==null){
					return "redirect:../login.jspx";
				}
				String wechatOppenId=member.getWechatOppenid();
				if(StringUtils.isNotEmpty(wechatOppenId)){	
					return weChatPublicPay(paymentPlugins, web,pay,order,wechatOppenId,
							request, model);
				}else{
			       return weChatCode(paymentPlugins,orderId,pay,1,request, response,model);
				}	
			}else{
				try {
					if(!StringUtils.isBlank(code)&&code.equals("alipayPartner")){
						aliURL = alipay(paymentPlugins,web, order, request, model);		
					}else if(!StringUtils.isBlank(code)&&code.equals("alipay")){
						aliURL = alipayapi(paymentPlugins,web, order, request, model);
					}
					response.setContentType("text/html;charset=UTF-8");
					out = response.getWriter();
					out.print(aliURL);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					out.close();
				}				
				return null;	
			}
		}else {
			return FrontUtils.showMessage(request, model,"请回到我的订单页面，再进行支付");
		}
	}

	//支付宝返回参数
	@RequestMapping(value = "/aliReturn.jspx")
	public String aliReturn(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException {
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		PaymentPlugins paymentPlugins =paymentPluginsMng.findByCode("alipayPartner");
		Long orderId=Long.parseLong(out_trade_no);
		Order order=manager.findById(orderId);
		if(verify(params,paymentPlugins.getPartner(),paymentPlugins.getSellerKey())){
			if(trade_status.equals("WAIT_BUYER_PAY")){
				//该判断表示买家已在支付宝交易管理中产生了交易记录，但没有付款
				return  FrontUtils.showMessage(request, model,"付款失败！");
			}else if(trade_status.equals("WAIT_SELLER_SEND_GOODS")){
				//该判断表示买家已在支付宝交易管理中产生了交易记录且付款成功，但卖家没有发货
				order.setPaymentStatus(2);
				order.setTradeNo(trade_no);
				order.setPaymentCode("alipayPartner");
				manager.updateByUpdater(order);
				return  FrontUtils.showMessage(request, model,"付款成功，请等待发货！");
			}else if(trade_status.equals("WAIT_BUYER_CONFIRM_GOODS")){
				//该判断表示卖家已经发了货，但买家还没有做确认收货的操作
				return  FrontUtils.showMessage(request, model,"已发货，未确认收货！");
			}else if(trade_status.equals("TRADE_FINISHED")){
				//该判断表示买家已经确认收货，这笔交易完成
				return  FrontUtils.showMessage(request, model,"交易完成！");
			}else {
				return  FrontUtils.showMessage(request, model,"success！");
			}
		}
		return  FrontUtils.showMessage(request, model,"付款异常！");
	}
	
	private String alipay(PaymentPlugins paymentPlugins,Website web,Order order,
			HttpServletRequest request,ModelMap model){
		//支付类型
		String payment_type = "1"; //必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://"+web.getDomain()+"/aliReturn.jspx";
		//页面跳转同步通知页面路径
		String return_url = "http://"+web.getDomain()+"/aliReturn.jspx";
		//卖家支付宝帐户
		String seller_email = paymentPlugins.getSellerEmail();//必填
		//商户订单号
		String out_trade_no = order.getId().toString();//商户网站订单系统中唯一订单号，必填
		//订单名称
		String subject = "("+order.getId()+")";//必填
		//付款金额
		String price = String.valueOf(order.getTotal());//必填
		//商品数量
		String quantity = "1";//必填，建议默认为1，不改变值，把一次交易看成是一次下订单而非购买一件商品
		//物流费用
		String logistics_fee = String.valueOf(order.getFreight());
		//物流类型
		String logistics_type = getLogisticsType(order);//必填，三个值可选：EXPRESS（快递）、POST（平邮）、EMS（EMS）
		//物流支付方式
		String logistics_payment = "BUYER_PAY";//必填，两个值可选：SELLER_PAY（卖家承担运费）、BUYER_PAY（买家承担运费）
		//订单描述
		String body = "("+order.getId()+")";
		//商品展示地址
		String show_url ="http://"+web.getDomain()+"/";
		//收货人姓名
		String receive_name = order.getReceiveName();
		//收货人地址
		String receive_address = order.getReceiveAddress();
		//收货人邮编
		String receive_zip = order.getReceiveZip();
		//收货人电话号码
		String receive_phone = order.getReceivePhone();
		//收货人手机号码
		String receive_mobile = order.getReceiveMobile();
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_partner_trade_by_buyer");
        sParaTemp.put("partner", paymentPlugins.getPartner());
        sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("price", price);
		sParaTemp.put("quantity", quantity);
		sParaTemp.put("logistics_fee", logistics_fee);
		sParaTemp.put("logistics_type", logistics_type);
		sParaTemp.put("logistics_payment", logistics_payment);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("receive_name", receive_name);
		sParaTemp.put("receive_address", receive_address);
		sParaTemp.put("receive_zip", receive_zip);
		sParaTemp.put("receive_phone", receive_phone);
		sParaTemp.put("receive_mobile", receive_mobile);
		//建立请求
		String sHtmlText = buildRequest(sParaTemp,paymentPlugins.getSellerKey(),"get","确认");
		return sHtmlText;
	}

	//支付宝返回参数
	@RequestMapping(value = "/aliReturnUrl.jspx")
	public String aliReturndirect(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws UnsupportedEncodingException {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		PaymentPlugins paymentPlugins =paymentPluginsMng.findByCode("alipay");
		Long orderId=Long.parseLong(out_trade_no);
		Order order=manager.findById(orderId);
		if(verify(params,paymentPlugins.getPartner(),paymentPlugins.getSellerKey())){//验证成功
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				order.setPaymentStatus(2);
				manager.updateByUpdater(order);
				return  FrontUtils.showMessage(request, model,"付款成功，请等待发货！");
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				order.setPaymentStatus(2);
				manager.updateByUpdater(order);
				return  FrontUtils.showMessage(request, model,"付款成功，请等待发货！");
				//注意：
				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
			}
		}else{//验证失败
			return  FrontUtils.showMessage(request, model,"验证失败！");
		}
		return  FrontUtils.showMessage(request, model,"付款异常！");
	}

	private String alipayapi(PaymentPlugins paymentPlugins,Website web,Order order,
			HttpServletRequest request,ModelMap model){
		//支付类型
		String payment_type = "1";//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://"+web.getDomain()+"/aliReturnUrl.jspx";
		//页面跳转同步通知页面路径
		String return_url = "http://"+web.getDomain()+"/aliReturnUrl.jspx";
		//卖家支付宝帐户
		String seller_email = paymentPlugins.getSellerEmail();//必填
		//商户订单号
		//String out_trade_no = order.getId().toString();//商户网站订单系统中唯一订单号，必填
		String out_trade_no = order.getCode().toString();// 商户网站订单系统中唯一订单id，必填
		//订单名称
		//String subject = "("+order.getId()+")";//必填
		String subject = order.getProductName();// 必填
		//付款金额
		String total_fee = String.valueOf(order.getTotal());//必填
		//订单描述
		String body = "("+order.getId()+")";
		//商品展示地址
		String show_url = "http://"+web.getDomain()+"/";
		//防钓鱼时间戳
		String anti_phishing_key = "";//若要使用请调用类文件submit中的query_timestamp函数
		//客户端的IP地址
		String exter_invoke_ip = RequestUtils.getIpAddr(request);//非局域网的外网IP地址，如：221.0.0.1
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", paymentPlugins.getPartner());
        sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		//建立请求
		String sHtmlText = buildRequest(sParaTemp,paymentPlugins.getSellerKey(),"get","确认");	
		return sHtmlText;
	}
	
	@RequestMapping(value="/WeChatScanCodePayReturn.jspx")
	public void WeChatScanCodePayReturn(Long code,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JDOMException, IOException, JSONException {
		JSONObject json=new JSONObject();
		//回调结果
		if(code!=null){
			if(manager.findByCode(code).getPaymentStatus()==2){					
				json.put("status", 4);
				json.put("error", "支付成功,点击确定，跳转到我的订单");
			}else{
				json.put("status", 6);
				json.put("error", "订单未支付");
			}
		}else{
			String xml_receive_result = getXmlRequest(request);
			if(StringUtils.isBlank(xml_receive_result)||xml_receive_result==""){
				json.put("status", 5);
				json.put("error", "检测到您可能没有进行扫码支付，请支付");
			}else{
				Map<String, String> result_map = doXMLParse(xml_receive_result);
				String sign_receive=result_map.get("sign");
				result_map.remove("sign");
				String key=paymentPluginsMng.findByCode("weChatPayment").getSellerKey();
				if(key==null){
					json.put("status", 1);
					json.put("error", "微信扫码支付密钥错误，请通知商户");
				}
				String checkSign=createSign(result_map,key);
				if(checkSign!=null&&checkSign.equals(sign_receive)){
					try{
						//通知微信该订单已处理
						noticeWeChatSuccess();
						if(result_map!=null){
							String return_code=result_map.get("return_code");
							if("SUCCESS".equals(return_code)&&"SUCCESS".equals(result_map.get("result_code"))){
								//微信返回的微信订单号（属于微信商户管理平台的订单号，跟自己的系统订单号不一样）
								String transaction_id=result_map.get("transaction_id");
								//商户系统的订单号，与请求一致。
								String out_trade_no=result_map.get("out_trade_no"); 
								Order order=manager.findByCode(Long.parseLong(out_trade_no));
								order.setPaymentStatus(2);
								order.setTradeNo(transaction_id);
								order.setPaymentCode("weChatPayment");
								manager.updateByUpdater(order);
								json.put("status", 0);
								json.put("error", "支付成功,点击确定，跳转到我的订单");
							}else if("SUCCESS".equals(return_code)&&result_map.get("err_code")!=null){
								String message=result_map.get("err_code_des");
								json.put("status", 2);
								json.put("error", message);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					Map<String,String> parames=new HashMap<String,String>();
					parames.put("return_code", "FAIL");
					parames.put("return_msg", "校验错误");
					//将参数转成xml格式
					String xmlWeChat = getRequestXml(parames);
					try{    	
						testPost(UNIFORM_SINGLE_INTERFACE,xmlWeChat);
					}catch(Exception e){
						e.printStackTrace();
					}
					json.put("status", 3);
					json.put("error", "支付参数错误，请重新支付!");
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	private String weChatPayment(PaymentPlugins paymentPlugins,Website web,Order order,
			HttpServletRequest request,HttpServletResponse response,ModelMap model) throws JDOMException, IOException{
		Map<String,String> paramMap = new HashMap<String,String>();
		if(StringUtils.isNotBlank(paymentPlugins.getPartner())&&StringUtils.isNotBlank(paymentPlugins.getSellerEmail())){
			//微信分配的公众账号ID（企业号corpid即为此appId）[必填]
			paramMap.put("appid", paymentPlugins.getPartner()); 
			//微信支付分配的商户号 [必填]
			paramMap.put("mch_id", paymentPlugins.getSellerEmail()); 
			//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB" [非必填]
			paramMap.put("device_info", "WEB");
			//随机字符串，不长于32位。  [必填]
			paramMap.put("nonce_str", getWCRandomNumber(10));  
			//商品或支付单简要描述 [必填]
			paramMap.put("body", order.getProductName()); 
			//商户系统内部的订单号,32个字符内、可包含字母, [必填]
			paramMap.put("out_trade_no", order.getCode().toString()); 
			//符合ISO 4217标准的三位字母代码，默认人民币：CNY. [非必填]
			paramMap.put("fee_type", "CNY"); 
			//金额必须为整数  单位为分 [必填]
			paramMap.put("total_fee", changeY2F(order.getTotal()+order.getFreight())); 
			//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP [必填]
			/*paramMap.put("spbill_create_ip",RequestUtils.getIpAddr(request)); */
			paramMap.put("spbill_create_ip","127.0.0.1");
			//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。 [必填]    
			paramMap.put("notify_url", "http://"+web.getDomain()+"/WeChatScanCodePayReturn.jspx"); 
			//交易类型{取值如下：JSAPI，NATIVE，APP，(JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付)} [必填]
			paramMap.put("trade_type", "NATIVE"); 
			// 商品ID{trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。}
			paramMap.put("product_id", order.getId().toString()); 
			if(StringUtils.isNotBlank(paymentPlugins.getSellerKey())){
				//根据微信签名规则，生成签名 
				paramMap.put("sign",createSign(paramMap,paymentPlugins.getSellerKey()));
				//把参数转换成XML数据格式   
				String xmlWeChat = getRequestXml(paramMap);
				String resXml=testPost(UNIFORM_SINGLE_INTERFACE,xmlWeChat);
				Map<String, String> map = doXMLParse(resXml);
				String returnCode = map.get("return_code");
				if(returnCode.equalsIgnoreCase("FAIL")){
					return FrontUtils.showMessage(request, model,map.get("return_msg"));
				}else if(returnCode.equalsIgnoreCase("SUCCESS")){
					if(map.get("err_code")!=null){
						return FrontUtils.showMessage(request, model,map.get("err_code_des"));
					}else if(map.get("result_code").equalsIgnoreCase("SUCCESS")){				
						String code_url=map.get("code_url");
						model.addAttribute("code_url", code_url);
						ShopFrontHelper.setCommonData(request, model, web, 1);
						//getQRCode(code_url,response);
						model.addAttribute("orderId", order.getId());
						model.addAttribute("out_trade_no", order.getCode());
						model.addAttribute("weChatPayment", "weChatPayment");
						return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,WECHATPAYMENT),request);	
					}
				}
				return FrontUtils.showMessage(request, model,"系统超时，请重试!");
			}else{
				return FrontUtils.showMessage(request, model,"请通知商城管理员，微信扫码支付密钥没有设置，请检查!");
			}
		}else{
			return FrontUtils.showMessage(request, model,"请通知商城管理员，微信扫码支付商户号或者appid没有设置，请检查!");
		}
	}

	
	
	
	/*微信授权*/
	private String weChatCode(PaymentPlugins paymentPlugins,Long orderId,String pay,Integer type,HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		Website web=SiteUtils.getWeb(request);
		String codeUrl="";
		String redirect_uri="/member/wechat_auth_call.jspx";
		if(StringUtils.isNotBlank(web.getGlobal().getContextPath())){
			redirect_uri="http://"+web.getDomain()+web.getGlobal().getContextPath()+redirect_uri;
		}else{
			redirect_uri="http://"+web.getDomain()+redirect_uri;
		}
		codeUrl=WEIXIN_AUTH_CODE_URL+"?appid="+paymentPlugins.getPartner().trim()+"&redirect_uri="+redirect_uri
				+"&response_type=code&scope=snsapi_base&state="+orderId+"@"+pay+"@"+type+"#wechat_redirect";
		model.addAttribute("codeUrl", codeUrl);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,WECHAT_CODE),request);
	}
	
	
	
	@RequestMapping(value = "/member/wechat_auth_call.jspx")
	public String weixinAuthCall(String code,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JDOMException, IOException {
		Website web=SiteUtils.getWeb(request);
		String[]state=request.getParameter("state").split("@");
		String orderId=state[0];
		String pay=state[1];
		String type=state[2];

		 Order order=manager.findById(Long.valueOf(orderId));
		
		PaymentPlugins paymentPlugins=paymentPluginsMng.findByCode("weChatPublicPay");
		//这个地方的secret指的是公众号的密钥,并非支付密钥
		String tokenUrl=WEIXIN_AUTH_TOKEN_URL+"&appid="+paymentPlugins.getPartner().trim()+"&secret="+paymentPlugins.getPublicKey().trim()+"&code="+code;
		JSONObject json = httpsRequestToJsonObject(tokenUrl, "POST", null);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		String openid=null;
		if(json!=null){
			try {
				openid = json.getString("openid");
				if(StringUtils.isNotBlank(openid)){
					ShopMember member = MemberThread.get();
					//用户没有登录，跳转到登录页
					if(member==null){
						return "redirect:../login.jspx";
					}
					member.setWechatOppenid(openid);
					shopMemberMng.update(member);
				}
			} catch (JSONException e) {
				WebErrors errors=WebErrors.create(request);
				String errcode = null;
				try {
					errcode = json.getString("errcode");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(StringUtils.isNotBlank(errcode)){
					errors.addErrorCode("wechat.auth.fail");
				}
				return FrontHelper.showError(errors, web, model, request);
			}
		}
		WebErrors errors=WebErrors.create(request);
		if(openid==null){
			errors.addErrorCode("wechat.auth.fail");
			return FrontHelper.showError(errors, web, model, request);
		}
		if(Integer.valueOf(type)==0){				
			errors.addErrorCode("wechat.pay.fail");
				return FrontHelper.showError(errors, web, model, request);
			
		}else{
			if(order==null){			
				errors.addErrorCode("wechat.pay.fail");
				return FrontHelper.showError(errors, web, model, request);
			}else{
				if(Integer.valueOf(type)==1){
					return weChatPublicPay(paymentPlugins, web,pay,order,openid,request, model);
				}
			}
		} 

		return  FrontUtils.showMessage(request, model,"wechat.pay.fail");
	}
	
	public static JSONObject httpsRequestToJsonObject(String requestUrl, 
			String requestMethod, String outputStr) {
	    JSONObject jsonObject = null;
		try {
			  StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr);
			  jsonObject = new JSONObject(buffer.toString());
	    } catch (ConnectException ce) {
	    	log.error("connect.timeout"+ce.getMessage());
	    } catch (Exception e) {
	    	log.error("https.request.exception"+e.getMessage());
	    }
	  return jsonObject;
	}
	
	private static StringBuffer httpsRequest(String requestUrl, String requestMethod, String output)
			  throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, MalformedURLException,
			  IOException, ProtocolException, UnsupportedEncodingException {
				  URL url = new URL(requestUrl);
				  HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				  connection.setDoOutput(true);
				  connection.setDoInput(true);
				  connection.setUseCaches(false);
				  connection.setRequestMethod(requestMethod);
				  if (null != output) {
					  OutputStream outputStream = connection.getOutputStream();
					  outputStream.write(output.getBytes(characterEncodingUTF8));
					  outputStream.close();
				  }
				  // 从输入流读取返回内容
				  InputStream inputStream = connection.getInputStream();
				  InputStreamReader inputStreamReader = new InputStreamReader(inputStream, characterEncodingUTF8);
				  BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				  String str = null;
				  StringBuffer buffer = new StringBuffer();
				  while ((str = bufferedReader.readLine()) != null) {
					  buffer.append(str);
				  }
				  bufferedReader.close();
				  inputStreamReader.close();
				  inputStream.close();
				  inputStream = null;
				  connection.disconnect();
				  return buffer;
			 } 
	
	
	//立即购买(公众号支付)
	public String	weChatPublicPay(PaymentPlugins paymentPlugins, Website web,String pay,
			Order order,String wechatOppenId,HttpServletRequest request,
			ModelMap model){
		if (StringUtils.isNotBlank(paymentPlugins.getPartner())&& StringUtils.isNotBlank(paymentPlugins.getSellerEmail())) {
			if (StringUtils.isNotBlank(paymentPlugins.getSellerKey())) {
				Map<String, String> map=weixinUniformOrder(JSAPI,PUBLIC_PAY_RETURN_URL,web,paymentPlugins,order,wechatOppenId,null,request);
				String returnCode = map.get("return_code");
				if (returnCode.equalsIgnoreCase("FAIL")) {
					return  FrontUtils.showMessage(request, model,map.get("return_msg"));

				} else if (returnCode.equalsIgnoreCase("SUCCESS")) {
					if (map.get("err_code") != null) {		
						return FrontUtils.showMessage(request, model,map.get("err_code_des"));
					} else if (map.get("result_code").equalsIgnoreCase("SUCCESS")) {
						//预支付交易会话标识
						String prepay_id = map.get("prepay_id");
						Long time=System.currentTimeMillis()/1000;
						String nonceStr=getWCRandomNumber(16);
						//公众号appid
						model.addAttribute("appId",paymentPlugins.getPartner().trim());
						//时间戳 当前的时间 需要转换成秒
						model.addAttribute("timeStamp",time);
						//随机字符串  不长于32位
						model.addAttribute("nonceStr",nonceStr);
						//订单详情扩展字符串 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
						model.addAttribute("package","prepay_id="+prepay_id);
						//签名方式 签名算法，暂支持MD5
						model.addAttribute("signType","MD5");
						Map<String, String> paramMapSign = new HashMap<String, String>();
						//生成prepay_id时appid是小写的i,生成paySign时，appId是大写的I
						paramMapSign.put("appId",paymentPlugins.getPartner().trim());
						paramMapSign.put("timeStamp",time.toString());
						paramMapSign.put("nonceStr",nonceStr);
						paramMapSign.put("package","prepay_id="+prepay_id);
						paramMapSign.put("signType","MD5");
						//签名
						model.addAttribute("paySign",createSign(paramMapSign, paymentPlugins.getSellerKey().trim()));
							model.addAttribute("order",order);
						ShopFrontHelper.setCommonData(request, model, web, 1);
					
							 model.addAttribute("publicPay","payOrderAgain");
						
							return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request, WECHAT_PUBLIC_PAY),request);
						
					}
				}
				return  FrontUtils.showMessage(request, model,"system.timeout.please.try.again");
			} else {
				return  FrontUtils.showMessage(request, model,"weChat.publickey.null");
			}
		} else {
			return  FrontUtils.showMessage(request, model,"weChat.publicMchidorApenid.null");
		}
	}
	
	
	//微信统一下单
	public static Map<String, String>  weixinUniformOrder(String tradeType,String returnUrl,Website web,
			PaymentPlugins paymentPlugins, Order order,String wechatOppenId,
			String retainage,HttpServletRequest request){
		Map<String, String> paramMap = new HashMap<String, String>();
		// 微信分配的公众账号ID（企业号corpid即为此appId）[必填]
		paramMap.put("appid", paymentPlugins.getPartner().trim());
		// 微信支付分配的商户号 [必填]
		paramMap.put("mch_id", paymentPlugins.getSellerEmail().trim());
		// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB" [非必填]
		paramMap.put("device_info", "WEB");
		// 随机字符串，不长于32位。 [必填]
		paramMap.put("nonce_str", getWCRandomNumber(10));
		// 商品或支付单简要描述 [必填]								
		paramMap.put("body", order.getProductName());
		// 商户系统内部的订单号,32个字符内、可包含字母, [必填]		
	    paramMap.put("out_trade_no", order.getCode().toString());
		// 符合ISO 4217标准的三位字母代码，默认人民币：CNY. [非必填]
		paramMap.put("fee_type", "CNY");
		// 金额必须为整数 单位为分 [必填]							
		paramMap.put("total_fee", changeY2F(order.getTotal()+order.getFreight()));
		// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP [必填]
		paramMap.put("spbill_create_ip", RequestUtils.getIpAddr(request));
		//paramMap.put("127.0.0.1", RequestUtils.getIpAddr(request));
		// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。 [必填]
		paramMap.put("notify_url", "http://" + web.getDomain()
				+ returnUrl);
		// 交易类型{取值如下：JSAPI，NATIVE，APP，(JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付)}
		// [必填]
		paramMap.put("trade_type",tradeType);
		//openid trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		if(tradeType.equals("JSAPI")&&wechatOppenId!=null){
			paramMap.put("openid",wechatOppenId.trim());
		}
		// 商品ID{trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。}
						
			paramMap.put("product_id", order.getId().toString());
		
		if (StringUtils.isNotBlank(paymentPlugins.getSellerKey())) {
			// 根据微信签名规则，生成签名
			paramMap.put("sign",createSign(paramMap, paymentPlugins.getSellerKey().trim()));
		}
		// 把参数转换成XML数据格式
		String xmlWeChat = getRequestXml(paramMap);
		String resXml = testPost(UNIFORM_SINGLE_INTERFACE, xmlWeChat);
		Map<String, String> map=new HashMap<String, String>();
		try {
			map = doXMLParse(resXml);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
		
	
	
	//立即购买付款回调(公众号支付)
	@RequestMapping(value = "/WeChatPublicPayReturn.jspx")
	public void WeChatPublicPayReturn(HttpServletRequest request, 
			HttpServletResponse response,ModelMap model) 
		throws JDOMException, IOException, JSONException {
		String xml_receive_result = getXmlRequest(request);
		if (StringUtils.isNotBlank(xml_receive_result)|| xml_receive_result != "") {
			Map<String, String> result_map = doXMLParse(xml_receive_result);
			String sign_receive = result_map.get("sign");
			result_map.remove("sign");
			String key = paymentPluginsMng.findByCode("weChatPublicPay").getSellerKey().trim();
			if(StringUtils.isNotBlank(key)){			
				String checkSign = createSign(result_map, key);
				if (checkSign != null && checkSign.equals(sign_receive)) {
					try {
						// 通知微信该订单已处理
						noticeWeChatSuccess();
						if (result_map != null) {
							String return_code = result_map.get("return_code");
							if ("SUCCESS".equals(return_code)&& "SUCCESS".equals(result_map.get("result_code"))) {
								// 微信返回的微信订单号（属于微信商户管理平台的订单号，跟自己的系统订单号不一样）
								String transaction_id = result_map.get("transaction_id");
								// 商户系统的订单号，与请求一致。
								String out_trade_no = result_map.get("out_trade_no");
								Order order = manager.findByCode(Long.valueOf(out_trade_no));
								order.setPaymentStatus(2);
								order.setTradeNo(transaction_id);
								order.setPaymentCode("weChatPublicPay");
								manager.updateByUpdater(order);
							} 
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Map<String, String> parames = new HashMap<String, String>();
					parames.put("return_code", "FAIL");
					parames.put("return_msg", "check.error");
					// 将参数转成xml格式
					String xmlWeChat = getRequestXml(parames);
					try {
						testPost(UNIFORM_SINGLE_INTERFACE, xmlWeChat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}	
	}
	
	//手机移动端网页支付
	public  String alipayMobile(String aliPayUrl ,PaymentPlugins paymentPlugins, Website web,Order order,
			String deposit,String retainage,HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		String aliPayUrl1=ALIPAY_TRADE_WAP_PAY;
		// 开放平台应用的APPID  paymentPlugins.getPublicKey()
		// 开发者应用私钥                    paymentPlugins.getSellerKey()
		// 支付宝公钥                 paymentPlugins.getSellerEmail()
		AlipayClient alipayClient = getAlipayClient(aliPayUrl1,paymentPlugins.getPublicKey(),//实例化客户端
				paymentPlugins.getSellerKey(),paymentPlugins.getSellerEmail(),UTF8);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        //在公共参数中设置回跳地址  同步返回（同步跳转通知）,网页重定向通知,买家付款成功后,会跳到 return_url所在的页面
        alipayRequest.setReturnUrl("http://" + web.getDomain() + ALIPAY_MOBILE_RETURN_URL);  //支付完成后跳回会员中心
        //		异步回调（异步通知） 传递给支付宝时的notify_url参数所对应的页面文件 
		alipayRequest.setNotifyUrl("http://" + web.getDomain()+ ALIPAY_MOBILE_PAY_NOTIFY_URL);//这是回调地址
        String subject=null;
        String out_trade_no=null;
        Double total_amount=null;
        				
	    subject=order.getProductName();
	    out_trade_no=order.getCode().toString().trim();
		total_amount=order.getTotal();
	
        alipayRequest.setBizContent("{" +
        //商户订单号
		"    \"out_trade_no\":\""+out_trade_no+"\"," +
        //卖家支付宝用户 ID
		"    \"seller_id\":\""+paymentPlugins.getPartner()+"\"," +
		//订单标题
		"    \"subject\":\""+subject+"\"," +
		//订单总金额
		"    \"total_amount\":"+total_amount+"," +
		//支付超时时间
		"    \"timeout_express\":\"90m\"" +
		"  }");
        String form = null;
		try {
			 //调用SDK生成表单
			form = alipayClient.pageExecute(alipayRequest).getBody();
	        response.setContentType("text/html;charset="+UTF8);
	        response.getWriter().write(form);//直接将完整的表单html输出到页面
	        response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return form;
	}
	 public static AlipayClient getAlipayClient(String aliPayUrl,String appId,
	    		String privateKey,String publicKey,String charset){
	    	if(null==alipayClient){
	    		alipayClient=new DefaultAlipayClient(aliPayUrl, appId, 
	            		privateKey, JSON,charset,publicKey);
	    	}
	    	return alipayClient;
	    }
	
	 
	// 支付宝返回参数：订单提交付款时{加入购物车的形式最终付款})
		@RequestMapping(value = ALIPAY_MOBILE_PAY_NOTIFY_URL)
		public void alipayMobileReturndirect(HttpServletRequest request,
				HttpServletResponse response, ModelMap model)
				throws UnsupportedEncodingException, AlipayApiException {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 商户订单编号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			PaymentPlugins paymentPlugins = paymentPluginsMng.findByCode("alipayMobile");
			Order order = manager.findByCode(Long.valueOf(out_trade_no));
			boolean signVerified = AlipaySignature.rsaCheckV1(params, paymentPlugins.getSellerEmail(), UTF8);
			if (signVerified) {// 验证成功
				if (trade_status.equals("TRADE_FINISHED")) {
					//交易结束，不可退款
					// 主订单改成“已支付”
					order.setStatus(2);
					order.setTradeNo(trade_no);
					manager.updateByUpdater(order);
					((PrintWriter) response).write("success"); 	//请不要修改或删除
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					//交易支付成功
					// 主订单改成“已支付”
					order.setStatus(2);
					order.setTradeNo(trade_no);
					manager.updateByUpdater(order);
					
					((PrintWriter) response).write("success"); 	//请不要修改或删除
				}
			} 
		}
	 
		
	public String getLogisticsType(Order order){
		String logistics_type;
		if(!StringUtils.isBlank(order.getShipping().getLogisticsType())){
			logistics_type = order.getShipping().getLogisticsType();
		}else{
			logistics_type = "EXPRESS";
		}
		return logistics_type;
		
	}
	
	@Autowired
	private OrderMng manager;
	@Autowired
	private PaymentPluginsMng paymentPluginsMng;
	@Autowired
	private ShopMemberMng shopMemberMng;
}
