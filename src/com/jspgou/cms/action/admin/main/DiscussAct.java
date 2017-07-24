package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.cms.entity.Discuss;
import com.jspgou.cms.manager.DiscussMng;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class DiscussAct {
	private static final Logger log = LoggerFactory.getLogger(DiscussAct.class);
	private String product_name="";

	@RequestMapping("/discuss/v_list.do")
	public String list(Date startTime,Date endTime,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		String userName = RequestUtils.getQueryParam(request, "userName");
		userName = StringUtils.trim(userName);
		String productName = RequestUtils.getQueryParam(request, "productName");
		productName = StringUtils.trim(productName);
		Pagination pagination = manager.getPage(null,null,null,userName,productName,startTime,endTime,
				cpn(pageNo), CookieUtils.getPageSize(request),true);
		if (!StringUtils.isBlank(userName)) {
			model.addAttribute("userName",userName);
		}
		if (!StringUtils.isBlank(productName)) {
			model.addAttribute("productName",productName);
		}
		
		
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pageNo);
		return "discuss/list";
	}

	@RequestMapping("/discuss/v_add.do")
	public String add(ModelMap model) {
		return "discuss/add";
	}

	@RequestMapping("/discuss/v_edit.do")
	public String edit(Long id,Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("discuss", manager.findById(id));
		model.addAttribute("pageNo", pageNo);
		return "discuss/edit";
	}

	@RequestMapping("/discuss/o_save.do")
	public String save(Discuss bean, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
//		bean = manager.save(bean);
		log.info("save Discuss id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/discuss/o_update.do")
	public String update(Discuss bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update Discuss id={}.", bean.getId());
		return list(null,null,pageNo,request, model);
	}

	@RequestMapping("/discuss/o_delete.do")
	public String delete(Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Discuss[] beans = manager.deleteByIds(ids);
		for (Discuss bean : beans) {
			log.info("delete Discuss id={}", bean.getId());
		}
		return list(null,null,pageNo, request, model);
	}
	/**
	 * 
	 * @param ids
	 * @param pageNo
	 * @param request
	 * @param model
	 * @return  在商品评论‘直观列表’删除评论，并返回‘直观列表’页面
	 */
	@RequestMapping("/discuss/o_visual_delete.do")
	public String visual_delete(Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Discuss[] beans = manager.deleteByIds(ids);
		for (Discuss bean : beans) {
			log.info("delete Discuss id={}", bean.getId());
		}
		return get_visual_list(null,null,pageNo, request, model);
	}
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param pageNo
	 * @param request
	 * @param model
	 * @return  获得‘直观商品评论列表’页面
	 */
	@RequestMapping("/discuss/v_visual_list.do")
	public String get_visual_list(Date startTime,Date endTime,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		String userName = RequestUtils.getQueryParam(request, "userName");
		userName = StringUtils.trim(userName);
		String productName = RequestUtils.getQueryParam(request, "productName");
		productName = StringUtils.trim(productName);
		/*当评论商品的名字非空时，保存供后面使用，使商品评论列表显示同一商品的评论信息*/
		if(StringUtils.isNotBlank(productName)){
			product_name=productName;
		/*当评论商品的名字为空时，取保存值使用，使商品评论列表显示同一商品的评论信息*/
		}else{
			productName=product_name;
		}
		Pagination pagination = manager.getPage(null,null,null,userName,productName,startTime,endTime,
				cpn(pageNo), CookieUtils.getPageSize(request),true);
		if (!StringUtils.isBlank(userName)) {
			model.addAttribute("userName",userName);
		}
		if (!StringUtils.isBlank(productName)) {
			model.addAttribute("productName",productName);
		}
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pageNo);
		return "discuss/visual_list";
	}

	private WebErrors validateSave(Discuss bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
//		Website web = SiteUtils.getWeb(request);
//		bean.setWebsite(web);
		return errors;
	}
	
	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		if (vldExist(id, web.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Long id, HttpServletRequest request) {
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
		Discuss entity = manager.findById(id);
		if(errors.ifNotExist(entity, Discuss.class, id)) {
			return true;
		}
//		if (!entity.getWebsite().getId().equals(webId)) {
//			errors.notInWeb(Discuss.class, id);
//			return true;
//		}
		return false;
	}
	
	@Autowired
	private DiscussMng manager;
}