package com.jspgou.cms.action.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.core.entity.Website;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.Coupon;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.CouponMng;


/**
* This class should preserve.
* @preserve
*/
@Controller
public class CouponAct {

	@RequestMapping("/coupon/v_add.do")
	public String add(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		List<Category> parentList = categoryMng.getListForParent(com.jspgou.core.web.SiteUtils.getWebId(request), null);
        model.addAttribute("parentList", parentList);
		return "coupon/add";
	}

	@RequestMapping("/coupon/o_save.do")
	public String save(Coupon bean, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website site = com.jspgou.core.web.SiteUtils.getWeb(request);
		manager.save(bean, site.getId());
		return list(request, response, model);
	}

	@RequestMapping("/coupon/v_list.do")
	public String list(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Coupon> cList = manager.getList();
		model.addAttribute("list", cList);
		return "coupon/list";
	}

	@RequestMapping("/coupon/v_edit.do")
	public String edit(Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		model.addAttribute("coupon", manager.findById(id));
		return "coupon/edit";
	}

	@RequestMapping("/coupon/o_update.do")
	public String update(Coupon bean, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		manager.update(bean);
		return this.list(request, response, model);
	}

	@RequestMapping("/coupon/o_delete.do")
	public String delete(Long[] ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website site = com.jspgou.core.web.SiteUtils.getWeb(request);
		String url = site.getUploadUrl();
		manager.deleteByIds(ids, url);
		return this.list(request, response, model);
	}

	@Autowired
	private CouponMng manager;
	@Autowired
	private CategoryMng categoryMng;

}