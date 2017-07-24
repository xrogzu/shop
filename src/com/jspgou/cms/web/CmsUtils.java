package com.jspgou.cms.web;

import javax.servlet.http.HttpServletRequest;

import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.core.entity.Website;

/**
 * 提供一些CMS系统中使用到的共用方法
 * 
 * 比如获得会员信息,获得后台站点信息
 * 
 * @author liufang
 * 
 */
public class CmsUtils {
	/**
	 * 用户KEY
	 */
	public static final String REQUEST_MEMBER_KEY = "_member_key";

	// *添加方法，将后台登陆用户添加到session中
	public static final String REQUEST_ADMIN_KEY = "_admin_key";
	/**
	 * 站点KEY
	 */
	public static final String REQUEST_WEBSITE_KEY = "_web_key";
	/**
	 * 站点基础域名
	 */
	public static final String REQUEST_BASE_DOMAIN_KEY = "_base_domain_key";

	/**
	 * 获得会员
	 * 
	 * @param request
	 * @return
	 */
	public static ShopMember getMember(HttpServletRequest request) {

		return (ShopMember) request.getAttribute(REQUEST_MEMBER_KEY);
	}

	// * 添加方法，获取admin,需要直接从post请求中获取2015.03.10
	public static void setAdmin(HttpServletRequest request, ShopAdmin admin) {
		request.setAttribute(REQUEST_ADMIN_KEY, admin);
	}

	public static ShopAdmin getAdmin(HttpServletRequest request) {
		return (ShopAdmin) request.getAttribute(REQUEST_ADMIN_KEY);
	}

	/**
	 * 获得会员ID
	 * 
	 * @param request
	 * @return
	 */
	public static Long getMemberId(HttpServletRequest request) {
		ShopMember member = getMember(request);
		if (member != null) {
			return member.getId();
		} else {
			return null;
		}
	}

	/**
	 * 设置用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setMember(HttpServletRequest request, ShopMember member) {
		request.setAttribute(REQUEST_MEMBER_KEY, member);
	}

	/**
	 * 获得站点
	 * 
	 * @param request
	 * @return
	 */
	public static Website getWebsite(HttpServletRequest request) {
		return (Website) request.getAttribute(REQUEST_WEBSITE_KEY);
	}

	/**
	 * 设置站点
	 * 
	 * @param request
	 * @param site
	 */
	public static void setWebsite(HttpServletRequest request, Website web) {
		request.setAttribute(REQUEST_WEBSITE_KEY, web);
	}

	/**
	 * 获得站点ID
	 * 
	 * @param request
	 * @return
	 */
	public static Long getWebsiteId(HttpServletRequest request) {
		return getWebsite(request).getId();
	}

	
}
