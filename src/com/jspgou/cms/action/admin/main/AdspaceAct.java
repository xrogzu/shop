package com.jspgou.cms.action.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Adspace;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.manager.AdspaceMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.core.web.WebErrors;
/**
* This class should preserve.
* @preserve
*/
@Controller
public class AdspaceAct {
	private static final Logger log = LoggerFactory
			.getLogger(AdspaceAct.class);
	
	@RequestMapping("/adspace/v_list.do")
	public String list(HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		List<Adspace> list=manager.getList();
		model.addAttribute("list", list);
		return "adspace/list";
	}
	
	@RequestMapping("/adspace/v_add.do")
	public String add(HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		return "adspace/add";
	}
	
	@RequestMapping("/adspace/v_edit.do")
	public String edit(Integer pageNo,Integer id,HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		Adspace bean =manager.findById(id);
		model.addAttribute("adspace", bean);
		return "adspace/edit";
	}
	
	@RequestMapping("/adspace/o_update.do")
	public String update(Adspace bean,HttpServletRequest request ,
			HttpServletResponse response,ModelMap model){
		manager.updateByUpdater(bean);
		return list(request,response,model);
	}
	
	@RequestMapping("/adspace/o_save.do")
	public String save(Integer pageNo,Adspace bean,HttpServletRequest request ,
			HttpServletResponse response,ModelMap model){
		manager.save(bean);
		return list(request,response,model);
	}
	
	@RequestMapping("/adspace/o_delete.do")
	public String delete(Integer[] ids,HttpServletRequest request ,
			HttpServletResponse response,ModelMap model){
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Adspace[] beans;
		try{
			beans=manager.deleteByIds(ids);
			for (Adspace bean : beans) {
				log.info("delete Adspace. id={}", bean.getId());
			}			
		}catch(Exception e){
			errors.addErrorString(productMng.getTipFile("Please.delete.the.advertisement.to.contain.advertising.management.data"));
			return errors.showErrorPage(model);
		}
		return list(request ,response,model);
	}
	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}
	
	private void vldExist(Integer id, WebErrors errors) {
		if (errors.hasErrors()) {
			return;
		}
		Adspace entity = manager.findById(id);
		errors.ifNotExist(entity, Adspace.class, id);
	}
	
	@Autowired
	private ProductMng productMng;
	@Autowired
	private AdspaceMng manager;
}
