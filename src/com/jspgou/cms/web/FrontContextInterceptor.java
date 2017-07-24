package com.jspgou.cms.web;

import static com.jspgou.cms.Constants.REQUEST_SHOP_CONFIG_KEY;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jspgou.common.util.CheckMobile;
import com.jspgou.common.web.session.SessionProvider;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopConfig;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ShopAdminMng;
import com.jspgou.cms.manager.ShopConfigMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.service.LoginSvc;
import com.jspgou.cms.web.threadvariable.AdminThread;
import com.jspgou.cms.web.threadvariable.GroupThread;
import com.jspgou.cms.web.threadvariable.MemberThread;

/**
 * 商城信息拦截器
 * 
 * 处理Config、和登录信息
 * 
 * @author liufang
* This class should preserve.
* @preserve
*/
public class FrontContextInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		Website web = SiteUtils.getWeb(request);
		ShopConfig config = shopConfigMng.findById(web.getId());
		if (config == null) {
			throw new IllegalStateException(
					"no ShopConfig found in Website id=" + web.getId());
		}
		request.setAttribute(REQUEST_SHOP_CONFIG_KEY, config);
		/*
		ShopMember member = loginSvc.getMember(request, response, web);
		// 将会员、会员组信息放入ThreadLocal，便于调用。
		if (member != null) {
			MemberThread.set(member);
			GroupThread.set(member.getGroup());
		}*/
		// 获得用户
		ShopMember member = null;
		ShopAdmin admin=null;
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			String username =  (String) subject.getPrincipal();
			member = shopMemberMng.getByUsername(web.getId(), username);
			admin = shopAdminMng.getByUsername(username);
		}
		if(admin!=null){
			Long userId=admin.getAdmin().getUser().getId();
			member=shopMemberMng.getByUserId(web.getId(), userId);
			if (member == null) {
				// 自动注册
				if (config.getRegisterAuto()) {
					member = shopMemberMng.join(userId, web.getId(), config.getRegisterGroup());
				}
			}
			AdminThread.set(admin);
		}
		checkEquipment(request, response);
		if (member != null) {
			MemberThread.set(member);
			GroupThread.set(member.getGroup());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MemberThread.remove();
		GroupThread.remove();
	}
    
	
	
	/**
	 * 检查访问方式
	 */
	public void checkEquipment(HttpServletRequest request,HttpServletResponse response){
		String ua=(String) session.getAttribute(request,"ua");
		if(null==ua){
			try{
				String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
				if(null == userAgent){  
				    userAgent = "";  
				}
				if(CheckMobile.check(userAgent)){
					ua="mobile";
				} else {
					ua="pc";
				}

				session.setAttribute(request, response, "ua",ua);
			}catch(Exception e){}
		}
		if(StringUtils.isNotBlank((ua) )){
			request.setAttribute("ua", ua);
		}
	}
	
	
	
	
	@Autowired
	private ShopConfigMng shopConfigMng;
	@Autowired
	private LoginSvc loginSvc;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private ShopAdminMng shopAdminMng;
	@Autowired
	private SessionProvider session;
}
