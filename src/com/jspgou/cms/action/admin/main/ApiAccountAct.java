package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;

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
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.manager.ApiAccountMng;

@Controller
public class ApiAccountAct {
	private static final Logger log = LoggerFactory.getLogger(ApiAccountAct.class);

	@RequestMapping("/apiAccount/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "apiAccount/list";
	}

	@RequestMapping("/apiAccount/v_add.do")
	public String add(ModelMap model) {
		return "apiAccount/add";
	}

	@RequestMapping("/apiAccount/v_edit.do")
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("apiAccount", manager.findById(id));
		return "apiAccount/edit";
	}

	@RequestMapping("/apiAccount/o_save.do")
	public String save(ApiAccount bean, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.save(bean);
		log.info("save ApiAccount id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/apiAccount/o_update.do")
	public String update(ApiAccount bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update ApiAccount id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/apiAccount/o_delete.do")
	public String delete(Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		ApiAccount[] beans = manager.deleteByIds(ids);
		for (ApiAccount bean : beans) {
			log.info("delete ApiAccount id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}

	private WebErrors validateSave(ApiAccount bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		//bean.setWebsite(web);
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
		ApiAccount entity = manager.findById(id);
		if(errors.ifNotExist(entity, ApiAccount.class, id)) {
			return true;
		}
		/*if (!entity.getWebsite().getId().equals(webId)) {
			errors.notInWeb(ApiAccount.class, id);
			return true;
		}*/
		return false;
	}
	
	@Autowired
	private ApiAccountMng manager;
}