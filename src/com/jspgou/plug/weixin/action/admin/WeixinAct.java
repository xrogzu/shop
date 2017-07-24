package com.jspgou.plug.weixin.action.admin;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.WebsiteMng;
import com.jspgou.core.web.WebErrors;
import com.jspgou.plug.weixin.entity.Weixin;
import com.jspgou.plug.weixin.manager.WeixinMng;

@Controller
public class WeixinAct {
	@RequiresPermissions("weixin:v_edit")
	public String add(HttpServletRequest request, ModelMap model) {

		return "weixin/add";
	}
	
	@RequiresPermissions("weixin:v_edit")
	@RequestMapping("/weixin/v_edit.do")
	public String edit(HttpServletRequest request, ModelMap model) {

		Weixin entity = manager.find(SiteUtils.getWebId(request));
		if(entity!=null){
			model.addAttribute("site",SiteUtils.getWeb(request));
			model.addAttribute("weixin",entity);
			return "weixin/edit";
		}else{
			return add(request, model);
		}
	}
	
	@RequiresPermissions("weixin:o_update")
	@RequestMapping("/weixin/o_save.do")
	public String save(Weixin bean,String appKey,String appSecret,HttpServletRequest request, ModelMap model) {
		Website site = SiteUtils.getWeb(request);
		bean.setSite(site);
		bean.setAppKey(appKey);
		bean.setAppSecret(appSecret);
		manager.save(bean);
		return edit(request, model);
	}
	
	
	
	
	
	@RequiresPermissions("weixin:o_update")
	@RequestMapping("/weixin/o_update.do")
	public String update(Weixin bean,String appKey,String appSecret,HttpServletRequest request, ModelMap model) {
		Website site = SiteUtils.getWeb(request);
		bean.setAppKey(appKey);
		bean.setAppSecret(appSecret);
		manager.update(bean);
		return edit(request, model);
	}
	
	
	private WebErrors validateCheck(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
	/*	CmsSite site = CmsUtils.getSite(request);*/
		Website site = SiteUtils.getWeb(request);
		errors.ifEmpty(ids, "ids");
		/*for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}*/
		return errors;
	}
	
	

	@Autowired
	private WeixinMng manager;
	@Autowired
	private WebsiteMng siteMng;



}
