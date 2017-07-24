package com.jspgou.cms.web;

import static com.jspgou.core.web.Constants.MODEL_PERMISSION_KEY;

import static com.jspgou.core.web.Constants.REQUEST_PERMISSION_KEY;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.manager.ShopAdminMng;
import com.jspgou.cms.service.LoginSvc;
import com.jspgou.cms.web.threadvariable.AdminThread;

/**
 * 商城后台信息拦截器
 * 
 * 登录信息、权限信息
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
*/
public class AdminContextInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
	throws Exception  {
		Website web = SiteUtils.getWeb(request);
		ShopAdmin admin=null;
		if (developAdminId != null) {
			// 开发状态
			admin = shopAdminMng.findById(developAdminId);
			// 指定管理员不存在
			if (admin == null) {
				throw new IllegalStateException("developAdminId not found: "+ developAdminId);
			}
			Long webId = admin.getWebsite().getId();
			// 指定管理员站点错误
			if (!webId.equals(web.getId())) {
				throw new IllegalStateException("developAdminId's website id="
						+ webId + " not in current website id=" + web.getId());
			}
		} else {
			//admin = loginSvc.getAdmin(request, response, web);
			// 获得用户
			Subject subject = SecurityUtils.getSubject();
			if(subject!=null){
				String username =  (String) subject.getPrincipal();
				if (subject.isAuthenticated()) {
				//	String username =  (String) subject.getPrincipal();
					//改成用户名获取对象
					admin = shopAdminMng.getByUsername(username);
					// 将管理员信息放入ThreadLocal
					AdminThread.set(admin);
				}
			}
		}
		String uri = getURI(request);
		if (exclude(uri)) {
			return true;
		}else{
			if (admin != null) {
				boolean viewonly = admin.getViewonlyAdmin();
				// 没有访问权限，提示无权限。
				// 权限放入request
				Set<String> perms = admin.getPerms();
				request.setAttribute(REQUEST_PERMISSION_KEY, perms);
				if(viewonly){
					if (permistionPass(uri)) {
						request.setAttribute("message", MessageResolver.getMessage(request,"login.notPermission"));
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return false;
					}
				}
			}
		}
		return true;
		
	}
	
	
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		ShopAdmin admin = AdminThread.get();
		if (admin != null && mav != null && mav.getModelMap() != null
				&& mav.getViewName() != null
				&& !mav.getViewName().startsWith("redirect:")) {
			mav.getModelMap().addAttribute(MODEL_PERMISSION_KEY,
					admin.getPerms());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		AdminThread.remove();
	}
	
	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(uri)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private boolean permistionPass(String uri) {
		String u = null;
		int i;
	    i = uri.lastIndexOf("/");
		if (i == -1) {
			throw new RuntimeException("uri must start width '/':"+ uri);
		}
		u = uri.substring(i + 1);
		// 操作型地址被禁止
		if (u.startsWith("o_")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 2;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException(
					"admin access path not like '/jeeadmin/jspgou/...' pattern: "
							+ uri);
		}
		return uri.substring(start);
	}

	@Autowired
	private LoginSvc loginSvc;
	@Autowired
	private ShopAdminMng shopAdminMng;
	private Long developAdminId;
	
	private String[] excludeUrls;

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}



	public void setDevelopAdminId(Long developAdminId) {
		this.developAdminId = developAdminId;
	}

}
