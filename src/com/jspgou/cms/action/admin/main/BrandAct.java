package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.util.CnToSpell;
import com.jspgou.common.util.StrUtils;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.web.WebErrors;
/**
* This class should preserve.
* @preserve
*/
@Controller
public class BrandAct {
	private static final Logger log = LoggerFactory.getLogger(BrandAct.class);

	@RequestMapping("/brand/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request,
			ModelMap model) {	
		String name = RequestUtils.getQueryParam(request, "name");
		String brandtype = RequestUtils.getQueryParam(request, "brandtype");
		List<Brand> list = manager.getAllList();
		Pagination pagination = manager.getPage(name,brandtype,cpn(pageNo), CookieUtils
				.getPageSize(request));
		
		List<Category> categoryList = categoryMng.getListForParent(com.jspgou.core.web.SiteUtils
				.getWebId(request), null);
		model.addAttribute("categoryList", categoryList);
		
		model.addAttribute("list", list);
		model.addAttribute("name", name);
		model.addAttribute("brandtype", brandtype);
		model.addAttribute("pagination",pagination);
		return "brand/list";
	}

	@RequestMapping("/brand/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		List<Category> list = categoryMng.getListForParent(com.jspgou.core.web.SiteUtils
				.getWebId(request), null);
		model.addAttribute("list", list);
		return "brand/add";
	}

	@RequestMapping("/brand/v_edit.do")
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		if(id !=null){
			WebErrors errors = validateEdit(id, request);
			if (errors.hasErrors()) {
				return errors.showErrorPage(model);
			}
			List<Category> list = categoryMng.getListForParent(com.jspgou.core.web.SiteUtils
					.getWebId(request), null);
			model.addAttribute("list", list);
		}else{ 
			return "brand/list";
		}
	
		model.addAttribute("brand", manager.findById(id));
		return "brand/edit";
	}

	@RequestMapping("/brand/o_save.do")
	public String save(Brand bean, String text, Long brandtypeId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		//获取品牌名称的首字母
		String name=bean.getName();
		CnToSpell cts = new CnToSpell();
	    bean.setFirstCharacter(cts.getBeginCharacter(name).substring(0, 1));
	    bean.setCategory(categoryMng.findById(brandtypeId));
		bean = manager.save(bean, text);
		log.info("save brand. id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/brand/o_update.do")
	public String update(Brand bean, String text,Long brandtypeId,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		//获取品牌名称的首字母
		String name=bean.getName();
		CnToSpell cts = new CnToSpell();
	    bean.setFirstCharacter(cts.getBeginCharacter(name).substring(0, 1));
	    bean.setCategory(categoryMng.findById(brandtypeId));
		bean = manager.update(bean, text);
		log.info("update brand. id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/brand/o_delete.do")
	public String delete(Long[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Brand[] beans;
		try{
			beans = manager.deleteByIds(ids);
			for (Brand bean : beans) {
				log.info("delete brand. id={}", bean.getId());
			}			
		}catch(Exception e){
			errors.addErrorString(productMng.getTipFile("Pleaseselectedoperation"));
			return errors.showErrorPage(model);	
		}
		return list(pageNo, request, model);
	}

	@RequestMapping("/brand/o_priority.do")
	public String priority(Long[] wids, Integer[] priority, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validatePriority(wids, priority, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.updatePriority(wids, priority);
		model.addAttribute("message", "global.success");
		return list(pageNo, request, model);
	}
	
	@RequestMapping(value = "/brand/v_check_brandName.do")
	public void checkUsername(String name,HttpServletRequest request, HttpServletResponse response) {
		try {
			name = new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		String pass;
		if (StringUtils.isBlank(name)) {
			pass = "false";
		} else {
			pass = manager.brandNameNotExist(name) ? "true" : "false";
		}
		ResponseUtils.renderJson(response, pass);
	}

	private WebErrors validateSave(Brand bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		bean.setWebUrl(StrUtils.handelUrl(bean.getWebUrl()));
		bean.setWebsite(com.jspgou.core.web.SiteUtils.getWeb(request));
		return errors;
	}

	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifNull(id, "id");
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateUpdate(Brand bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Long id = bean.getId();
		bean.setWebUrl(StrUtils.handelUrl(bean.getWebUrl()));
		errors.ifNull(id, "id");
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Long id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private WebErrors validatePriority(Long[] wids, Integer[] priority,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(wids, "wids")) {
			return errors;
		}
		if (errors.ifEmpty(priority, "priority")) {
			return errors;
		}
		if (wids.length != priority.length) {
			errors.addErrorString("wids length not equals priority length");
			return errors;
		}
		for (int i = 0, len = wids.length; i < len; i++) {
			vldExist(wids[i], errors);
			if (priority[i] == null) {
				priority[i] = 0;
			}
		}
		return errors;
	}

	private boolean vldExist(Long id, WebErrors errors) {
		Brand entity = manager.findById(id);
		return errors.ifNotExist(entity, Brand.class, id);
	}

	@Autowired
	private ProductMng productMng;
	@Autowired
	private BrandMng manager;
	@Autowired
	private CategoryMng categoryMng;

	
}