package com.jspgou.cms.action.front;

import static com.jspgou.cms.Constants.SHOP_SYS;
import static com.jspgou.common.page.SimplePage.cpn;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.front.FrontHelper;
import com.jspgou.core.web.front.URLHelper;
import com.jspgou.cms.entity.Consult;
import com.jspgou.cms.entity.Discuss;
import com.jspgou.cms.entity.OrderItem;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ConsultMng;
import com.jspgou.cms.manager.DiscussMng;
import com.jspgou.cms.manager.OrderItemMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.cms.web.threadvariable.MemberThread;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class ProductFormAct {

	/**
	 * 查询 买家评论(分页)
	 */
	@RequestMapping(value = "/searchDiscussPage*.jspx")
	public String searchDiscussPage(Long productId,Integer pageNo,
			HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		Product bean = productMng.findById(productId);
		model.addAttribute("product", bean);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		ShopFrontHelper.setDynamicPageData(request, model, web, "","searchDiscussPage", ".jspx", cpn(pageNo));
		return web.getTemplate(SHOP_SYS, MessageResolver.getMessage(request,"tpl.discussContentPage"), request);
	}
	
	/**
	 * 
	 * 
	 * @param request  商品评价
	 */

	@RequestMapping(value= "/pingjia*.jspx")
	public  String pingjia(Long productId,Long orderId,Integer pageNo,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		Product bean = productMng.findById(productId);
		OrderItem orderItem = orderItemMng.findByMember(member.getId(), productId,orderId);
		  model.addAttribute("orderItem", orderItem);
		  model.addAttribute("product", bean);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		ShopFrontHelper.setDynamicPageData(request, model, web, "","discussContentPage", ".jspx", cpn(pageNo));
		return web.getTemplate(SHOP_SYS, MessageResolver.getMessage(request,"tpl.assess"),request);
	}
	
	
	/**
	 * 发表评论
	 */
	@RequestMapping(value = "/haveDiscuss*.jspx")
	public String haveDiscuss(Long productId,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if (member == null) {
			ResponseUtils.renderJson(response, "denru");
			return null;
		}
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		OrderItem bean = orderItemMng.findByMember(member.getId(), productId,null);
		if (bean != null) {
			ResponseUtils.renderJson(response, "success");
			return null;
		} else {
			ResponseUtils.renderJson(response, "false");
			return null;
		}
	}

	/**
	 * 查询 购买咨询(分页)
	 */
	@RequestMapping(value = "/consultProduct*.jspx")
	public String consultProduct(Long productId,
			HttpServletResponse response, HttpServletRequest request,
			ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		
		int pageNo = URLHelper.getPageNo(request);
		ShopFrontHelper.setCommonData(request, model, web, cpn(pageNo));
		Product bean = productMng.findById(productId);
		Pagination page = consultMng.getPage(productId,null,null,null,null, cpn(pageNo), 5, true);
		model.addAttribute("product", bean);
		model.addAttribute("pagination", page);
		ShopFrontHelper.setDynamicPageData(request, model, web, "","consultProduct", ".jspx", cpn(pageNo));
		return web.getTemplate(SHOP_SYS, MessageResolver.getMessage(request,"tpl.consultProduct"), request);
	}
	
	/**
	 * 查询成交记录(分页)
	 */
	@RequestMapping(value = "/bargain*.jspx")
	public String list(Long productId,HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		int pageNo = URLHelper.getPageNo(request);
		Product bean = productMng.findById(productId);
		Pagination page = orderItemMng.getOrderItem(cpn(pageNo), 4, productId);
		model.addAttribute("pagination", page);
		model.addAttribute("product", bean);
		ShopFrontHelper.setCommonData(request, model, web,pageNo);
		ShopFrontHelper.setDynamicPageData(request, model, web, RequestUtils.getLocation(request),"bargain", ".jspx", pageNo);
		return web.getTplSys(SHOP_SYS, MessageResolver.getMessage(request,"tpl.bargain"), request);
	}
	
	/**
	 * 发布购买咨询
	 */
	/*@RequestMapping(value = "/insertConsult.jspx")
	public String insertConsult(Long productId, String content,
			HttpServletResponse response, HttpServletRequest request,
			ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if (member == null) {
			ResponseUtils.renderJson(response, "false");
			return null;
		}
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		Consult bean = consultMng.saveOrUpdate(productId, content, member.getId());
		if (bean == null) {
			ResponseUtils.renderJson(response, "sameUsually");
			return null;
		}
		ResponseUtils.renderJson(response, "success");
		return null;
	}*/
	
	
	@RequestMapping(value = "/insertConsult.jspx")
	public void insertConsult(Long productId, String content,
			HttpServletResponse response, HttpServletRequest request,
			ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember user = MemberThread.get();
		if (user != null) {
			if(StringUtils.isEmpty(content)){
				ResponseUtils.renderJson(response, "empty");
			}else{
				Consult bean = consultMng.saveOrUpdate(productId, content, user.getId());
				if (bean == null) {
					ResponseUtils.renderJson(response, "sameUsually");
				}else{
					ResponseUtils.renderJson(response, "success");
				}
			}
		}else{
			ResponseUtils.renderJson(response, "false");
		}
		
	}
	
	//显示咨询填写面板
		@RequestMapping(value="showProblem.jspx")
		public void showProblem(HttpServletResponse response,Long productId,
				HttpServletRequest request, ModelMap model){
			ShopMember user = MemberThread.get();
			if(user != null){
					ResponseUtils.renderJson(response, "success");
			}else{
				ResponseUtils.renderJson(response, "false");
			}
		}

	/**
	 * 发布评论
	 */
	@RequestMapping(value = "/insertDiscuss.jspx ")
	public String insertDiscuss(Long productId, String disCon,String discussType,
			HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if (member == null) {
			ResponseUtils.renderJson(response, "false");
			return null;
		}
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		Discuss bean = discussMng.saveOrUpdate(productId, disCon, member.getId(),discussType);
		if (bean == null) {
			ResponseUtils.renderJson(response, "sameUsually");
			return null;
		}
		ResponseUtils.renderJson(response, "success");
		return null;
	}

	/**
	 * 会员浏览历史记录
	 */
	@RequestMapping(value = "/historyRecord.jspx") 
	public String historyRecord(Long productId, HttpServletResponse response,HttpServletRequest request,ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if (member == null) {
			ResponseUtils.renderJson(response, "false");
			return null;
		}
		if(productId==null||productMng.findById(productId)==null){
			return FrontHelper.pageNotFound(web, model, request);
		}
		String str = "";
		Cookie[] cookeis = request.getCookies();
		int num = cookeis.length;
		for (int i = 0; i < num; i++) {
			if (cookeis[i].getName().equals("shop_record")) {
				str = ',' + cookeis[i].getValue();
				break;
			}
		}
		str = productId + str;
		int n = 0, m = 0;
		int j = str.length();
		for (int i = 0; i < j; i++) {
			if (str.charAt(i) == ',') {
				n++;
			}
			if (n == 6) {
				break;
			}
			m = i + 1;
		}
		Cookie cook = new Cookie("shop_record", str.substring(0,m));
		String path = request.getContextPath();
		cook.setPath(StringUtils.isBlank(path) ? "/" : path);
		response.addCookie(cook);
		return null;
	}

	@Autowired
	private ProductMng productMng;
	@Autowired
	private ConsultMng consultMng;
	@Autowired
	private OrderItemMng orderItemMng;
	@Autowired
	private DiscussMng discussMng;
}
