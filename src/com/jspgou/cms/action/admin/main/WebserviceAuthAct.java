package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.WebserviceAuth;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.WebserviceAuthMng;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.security.encoder.Md5PwdEncoder;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.core.web.WebErrors;

@Controller
public class WebserviceAuthAct {
	private static final Logger log = LoggerFactory.getLogger(WebserviceAuthAct.class);

	@RequestMapping("/webserviceAuth/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "webserviceAuth/list";
	}

	@RequestMapping("/webserviceAuth/v_add.do")
	public String add(ModelMap model) {
		return "webserviceAuth/add";
	}

	@RequestMapping("/webserviceAuth/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		model.addAttribute("WebserviceAuth", manager.findById(id));
		return "webserviceAuth/edit";
	}

	@RequestMapping("/webserviceAuth/o_save.do")
	public String save(WebserviceAuth bean, HttpServletRequest request, ModelMap model) {
		bean.setPassword(pwdEncoder.encodePassword(bean.getPassword()));
		bean = manager.save(bean);
		log.info("save WebserviceAuth id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/webserviceAuth/o_update.do")
	public String update(Integer id,String username,String password,String system,Boolean enable, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebserviceAuth bean = manager.update(id, username, password, system, enable);
		log.info("update WebserviceAuth id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/webserviceAuth/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		WebserviceAuth[] bean;
		try{
			bean=manager.deleteByIds(ids);			
		}catch(Exception e){
			errors.addErrorString(productMng.getTipFile("Please.and.user.operation"));
			return errors.showErrorPage(model);	
		}
		return list(pageNo, request, model);
	}
	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, WebErrors errors) {
		WebserviceAuth entity = manager.findById(id);
		return errors.ifNotExist(entity, WebserviceAuth.class, id);
	}
	
	@Autowired
	private WebserviceAuthMng manager;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private ProductMng productMng;
}