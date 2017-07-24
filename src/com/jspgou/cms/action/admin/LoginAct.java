package com.jspgou.cms.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspgou.common.security.BadCredentialsException;
import com.jspgou.common.security.UsernameNotFoundException;
import com.jspgou.core.entity.Website;

import com.jspgou.core.security.UserNotInWebsiteException;
import com.jspgou.core.web.WebErrors;

import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.service.LoginSvc;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.cms.web.threadvariable.AdminThread;
/**
* This class should preserve.
* @preserve
*/
@Controller
public class LoginAct {
	private static final Logger log = LoggerFactory.getLogger(LoginAct.class);

	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public String index(ModelMap model) {
		ShopAdmin admin = AdminThread.get();
		if (admin != null) {
			model.addAttribute("admin", admin);
			return "index";
		} else {
			return "login";
		}
	}

	
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return "login";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String submit(String username, String password,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
//		Object error = request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
//		if (error != null) {
//			model.addAttribute("error", error);
//		}
		
		WebErrors errors = validateSubmit(username,request,response);
		if (!errors.hasErrors()) {
			Website web = SiteUtils.getWeb(request);
			try {
				loginSvc.adminLogin(request, response, web, username, password);
				log.info("admin '{}' login success.", username);
				return "redirect:index.do";
			} catch (UsernameNotFoundException e) {
				errors.addError(e.getMessage());
				log.info(e.getMessage());
			} catch (BadCredentialsException e) {
//				if(!username.trim().equals("admin")||SiteUtils.getWeb(request).getGlobal().getErrorTimes()==0){
//					AdminMap.addAdminMapVal(username);
//				}
				errors.addError(e.getMessage());
				log.info(e.getMessage());
			} catch (UserNotInWebsiteException e) {
				errors.addError(e.getMessage());
				log.info(e.getMessage());
			}
		}
//		if(errors!=null){
//			userMng.errorRemaing(username,request);
//		}
		errors.toModel(model);
		
		return "login";
	}

	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		loginSvc.logout(request, response);  
		return "redirect:index.do";
	}
	
	
	private WebErrors validateSubmit(String username,HttpServletRequest request,HttpServletResponse response) {
//		System.out.println("7777");
		WebErrors errors = WebErrors.create(request);
//		Integer errCount=AdminMap.getAdminMapVal(username);
//		if(errCount!=null&&errCount>=SiteUtils.getWeb(request).getGlobal().getErrorTimes()){
//			errors.addError("你的账号被锁定!,"+SiteUtils.getWeb(request).getGlobal().getErrorInterval()+"分钟后，自动解锁");
//			return errors;
//		}
		return errors;
	}

	@Autowired
	private LoginSvc loginSvc;
//	@Autowired
//	private UserMng userMng;
	
}
