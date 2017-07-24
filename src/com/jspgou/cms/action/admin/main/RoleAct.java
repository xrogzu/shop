package com.jspgou.cms.action.admin.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.core.entity.Role;
import com.jspgou.core.manager.LogMng;
import com.jspgou.core.manager.RoleMng;
import com.jspgou.core.security.CmsAuthorizingRealm;
import com.jspgou.core.web.WebErrors;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class RoleAct {
	private static final Logger log = LoggerFactory.getLogger(RoleAct.class);
	
	@RequestMapping("/role/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<Role> list = manager.getList();
		model.addAttribute("list", list);
		return "role/list";
	}

	@RequestMapping("/role/v_add.do")
	public String add(ModelMap model) {
		return "role/add";
	}

	@RequestMapping("/role/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		model.addAttribute("role", manager.findById(id));
		return "role/edit";
	}

	@RequestMapping("/role/o_save.do")
	public String save(Role bean, String[] perms,
			HttpServletRequest request, ModelMap model) {
		bean = manager.save(bean, splitPerms(perms));
		return "redirect:v_list.do";
	}

	@RequestMapping("/role/o_update.do")
	public String update(Role bean, String[] perms,boolean all,
			HttpServletRequest request, ModelMap model) {
		bean = manager.update(bean, splitPerms(perms));
//		return list(request, model);
		
//		WebErrors errors = validateUpdate(bean.getId(), request);
//		if (errors.hasErrors()) {
//			return errors.showErrorPage(model);
//		}
//		bean = manager.update(bean, splitPerms(perms));
//		//权限更改 清空用户权限缓存
//		if(hasChangePermission(all, perms, bean)){
//			Set<User>admins=bean.getUsers();
//			for(User admin:admins){
//				authorizingRealm.removeUserAuthorizationInfoCache(admin.getUsername());
//			}
//		}
//		log.info("update Role id={}.", bean.getId());
////		cmsLogMng.operating(request, "Role.log.update", "id=" + bean.getId()
////				+ ";name=" + bean.getName());
		return list(request, model);
	}

	@RequestMapping("/role/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		Role[] beans = manager.deleteByIds(ids);
		return list(request, model);
	}

	private Set<String> splitPerms(String[] perms) {
		Set<String> set = new HashSet<String>();
		if (perms != null) {
			for (String perm : perms) {
				for (String p : StringUtils.split(perm, ',')) {
					if (!StringUtils.isBlank(p)) {
						set.add(p);
					}
				}
			}
		}
		return set;
	}
	
	private boolean hasChangePermission(boolean all,String[] perms,Role bean){
		Role role=manager.findById(bean.getId());
		if(!bean.getPerms().toArray().equals(perms)){
			return true;
		}
		return false;
	}
	
	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		Role entity = manager.findById(id);
		if (errors.ifNotExist(entity, Role.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private RoleMng manager;
	@Autowired
	private LogMng cmsLogMng;
	@Autowired
	private CmsAuthorizingRealm authorizingRealm;
	
}