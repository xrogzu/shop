package com.jspgou.cms.action.member;

import static com.jspgou.cms.Constants.MEMBER_SYS;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspgou.common.web.session.SessionProvider;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.EmailSender;
import com.jspgou.core.entity.MessageTemplate;
import com.jspgou.core.entity.User;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.UserMng;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * 找回密码Action
 * 
 * 用户忘记密码后点击找回密码链接，输入用户名、邮箱和验证码<li>
 * 如果信息正确，返回一个提示页面，并发送一封找回密码的邮件，邮件包含一个链接及新密码，点击链接新密码即生效<li>
 * 如果输入错误或服务器邮箱等信息设置不完整，则给出提示信息<li>
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
 */
@Controller
public class ForgotPasswordAct {
	private static final Logger log = LoggerFactory
			.getLogger(ForgotPasswordAct.class);
	private static final String FORGOTTEN_INPUT = "tpl.forgottenInput";
	private static final String FORGOTTEN_RESULT = "tpl.forgottenResult";
	private static final String RESET_PASSWORD_TPL = "tpl.resetPassword";

	/**
	 * 找回密码输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forgot_password.jspx", method = RequestMethod.GET)
	public String fogottenInput(HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,
				FORGOTTEN_INPUT),request);
	}

	/**
	 * 找回密码提交页
	 * 
	 * @param username
	 * @param email
	 * @param captcha
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forgot_password.jspx", method = RequestMethod.POST)
	public String fogottenSubmit(String checkcode, String username,
			String email, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		WebErrors errors = validateFogotten(checkcode, username, email,
				request, response);
		if (errors.hasErrors()) {
			return FrontHelper.showError(errors, web, model, request);
		}
		// 0:成功1:用户名不存在;2:邮箱地址错误;3: 邮箱输入错误;4:邮件发送器没有设置;5:找回密码模板没有设置
		User user = userMng.getByUsername(username);
		MessageTemplate tpl = web.getMessages().get(
				MessageTemplate.RESET_PASSWORD);
		EmailSender sender = web.getEmailSender();
		if (user == null) {
			// 用户名不存在
			model.addAttribute("status", 1);
		} else if (!user.getEmail().equalsIgnoreCase(email)) {
			// 用户没有设置邮箱
			model.addAttribute("status", 2);
		} else if (!user.getEmail().equals(email)) {
			// 邮箱输入错误
			model.addAttribute("status", 3);
		} else if (sender == null) {
			// 邮件服务器没有设置好
			model.addAttribute("status", 4);
		} else if (tpl == null) {
			// 邮件模板没有设置好
			model.addAttribute("status", 5);
		} else {
			// 0:成功
			try {
			String base=new String(web.getUrlBuff(true));
			  user = userMng.passwordForgotten(user.getId(),base, sender, tpl);
			  // 获取邮件类型
			   String emailtype = email.substring(email.indexOf("@") + 1, email.indexOf("."));
			   model.addAttribute("emailtype", emailtype);
			   model.addAttribute("status", 0);
			   model.addAttribute("user", user);
			} catch (Exception e) {
				// 发送邮件异常
				model.addAttribute("status", 100);
				model.addAttribute("message", e.getMessage());
				log.error("send email exception.", e);
			}
		}
		model.addAttribute("user", user);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		log.info("find passsword, username={} email={}", username, email);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,
				FORGOTTEN_RESULT),request);
	}

	@RequestMapping(value = "/reset_password.jspx")
	public String resetPwd(Long uid, String activationCode,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		WebErrors errors = validateReset(uid, activationCode, request);
		if (errors.hasErrors()) {
			return FrontHelper.showMessage(errors.getErrors().get(0), web,
					model, request);
		}
		User user = userMng.findById(uid);
		boolean success;
		if (activationCode.equals(user.getResetKey())) {
			user = userMng.resetPassword(user.getId());
			success = true;
		} else {
			success = false;
		}
		model.addAttribute("user", user);
		model.addAttribute("success", success);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,
				RESET_PASSWORD_TPL),request);
	}

	private WebErrors validateFogotten(String checkcode, String username,
			String email, HttpServletRequest request,
			HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		String id = session.getSessionId(request, response);
		if (errors.ifOutOfLength(checkcode, "checkcode", 3, 10)) {
			return errors;
		}
		try {
			if (!captchaService.validateResponseForID(id, checkcode
					.toUpperCase(Locale.ENGLISH))) {
				errors.addErrorCode("error.checkcodeIncorrect");
				return errors;
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.checkcodeInvalid");
			errors.addErrorString(e.getMessage());
			return errors;
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return errors;
		}
		if (errors.ifNotUsername(username, "username", 3, 100)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateReset(Long uid, String resetKey,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(uid, "uid")) {
			return errors;
		}
		User user = userMng.findById(uid);
		if (errors.ifNotExist(user, User.class, uid)) {
			return errors;
		}
		if (errors.ifOutOfLength(resetKey, "resetKey", 32, 32)) {
			return errors;
		}
		return errors;
	}

	@Autowired
	private UserMng userMng;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private SessionProvider session;
}
