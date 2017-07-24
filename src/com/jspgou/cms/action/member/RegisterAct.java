package com.jspgou.cms.action.member;

import static com.jspgou.cms.Constants.MEMBER_SYS;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspgou.cms.entity.ShopConfig;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopScore;
import com.jspgou.cms.entity.ShopScore.ScoreTypes;
import com.jspgou.cms.entity.Webservice;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.manager.ShopScoreMng;
import com.jspgou.cms.manager.WebserviceMng;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.session.SessionProvider;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.EmailSender;
import com.jspgou.core.entity.Member;
import com.jspgou.core.entity.MessageTemplate;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.MemberMng;
import com.jspgou.core.manager.UserMng;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class RegisterAct {
	private static final Logger log = LoggerFactory
			.getLogger(RegisterAct.class);
	private static final String REGISTER= "tpl.register";
	private static final String REGISTER_RESULT = "tpl.registerResult";
	private static final String REGISTER_TREATY = "tpl.registerTreaty";
	private static final String REGISTER_ACTIVE_STATUS="tpl.registerActiveStatus";

	@RequestMapping(value = "/register.jspx",method = RequestMethod.GET)
	public String registerInput(HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,REGISTER),request);
	}

	@RequestMapping(value = "/register.jspx",method = RequestMethod.POST)
	public String registerSubmit(String checkcode, String username,
			String email, String password, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopConfig config = SiteUtils.getConfig(request);
		WebErrors errors = validate(checkcode, username, email, password,request, response);
		if (errors.hasErrors()) {
			return FrontHelper.showError(errors, web, model, request);
		}
		ShopFrontHelper.setCommonData(request, model, web, 1);
        EmailSender sender = web.getEmailSender();
		MessageTemplate tpl = web.getMessages().get(MessageTemplate.RESET_PASSWORD);
		//发送激活邮件
		if (sender == null) {
			// 邮件服务器没有设置好
			model.addAttribute("status", 2);
		} else if (tpl == null) {
			// 邮件模板没有设置好
			model.addAttribute("status", 3);
		} else {
		    try {
		    	//uuid激活码
		    	String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
		    	String base=new String(web.getUrlBuff(true));
		    	userMng.senderActiveEmail(username,base,email,uuid, sender, tpl);
		    	ShopMember member = shopMemberMng.register(username, password, email,false,uuid,
						request.getRemoteAddr(), false, web.getId(), config.getRegisterGroup().getId());
		    	//调用接口
		    	webserviceMng.callWebService("false",username, password, email, null, Webservice.SERVICE_TYPE_ADD_USER);	
		    	// 获取邮件类型
				String emailtype = email.substring(email.indexOf("@") + 1, email.indexOf("."));
				model.addAttribute("emailtype", emailtype);
				model.addAttribute("member", member);
			    model.addAttribute("status", 1);
			    log.info("register member '{}'", member.getUsername());
		       } catch (Exception e) {
					// 发送邮件异常
					model.addAttribute("status", 4);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception {}.", e.getMessage());
		       }
		}
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,REGISTER_RESULT),request);
	}
	
	//激活用户
	@RequestMapping(value = "/active.jspx", method = RequestMethod.GET)
	public String active(String userName, String activationCode,HttpServletRequest request, 
			HttpServletResponse response,ModelMap model) throws IOException {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		String userName1 = new String(userName.getBytes("ISO_8859_1"),"GBK");
		Member bean=memberMng.getByUsername(web.getId(), userName1);
		long l=System.currentTimeMillis();
	    l=l-24*60*60*1000;
	    Date date=new Date();
		date.setTime(l);
		if (StringUtils.isBlank(String.valueOf(userName))|| StringUtils.isBlank(activationCode)) {
             model.addAttribute("status", 2);
		}else if (bean == null) {
			 model.addAttribute("status", 3);
		}else if (bean.getActive()) {
			 model.addAttribute("status", 4);
		}else if (!bean.getActivationCode().equals(activationCode)) {
			 model.addAttribute("status", 5);
		}else if(bean.getCreateTime().compareTo(date)<0){
			 model.addAttribute("status", 6);
	    }else{
			bean.setActive(true);
			memberMng.update(bean); 
			//验证邮箱加十个积分
			shopMemberMng.updateScore(shopMemberMng.findById(bean.getId()),
			com.jspgou.core.web.SiteUtils.getWeb(request).getGlobal().getActiveScore());
			ShopScore shopScore=new ShopScore();
			shopScore.setMember(shopMemberMng.findById(bean.getId()));
			shopScore.setName("邮箱验证送积分");
			shopScore.setScoreTime(new Date());
			shopScore.setStatus(true);
			shopScore.setUseStatus(false);
			shopScore.setScoreType(ScoreTypes.EMAIL_VALIDATE.getValue());
			shopScoreMng.save(shopScore);
			model.addAttribute("status", 1);
			model.addAttribute("member", bean);
		}
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,REGISTER_ACTIVE_STATUS),request);
	}
	
	// 重新发送激活邮件
	@RequestMapping(value = "/reactive.jspx", method = RequestMethod.POST)
	public void reactive(Long userId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		JSONObject json = new JSONObject();
		Member bean=memberMng.findById(userId);
		if(bean.getActive()){
			try {
				json.put("data", 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		    //重新获取uuid激活码，更新激活码，更新创建时间
    	    String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
         	bean.setActivationCode(uuid);
         	bean.setCreateTime(new Date());
    	    memberMng.update(bean);
		    String base=new String(web.getUrlBuff(true));
            EmailSender sender = web.getEmailSender();
            Map<String, MessageTemplate> messages = web.getMessages();
		    MessageTemplate tpl =messages.get("resetPassword");
	        try {
    	         userMng.senderActiveEmail(bean.getUsername(),base,bean.getEmail(),uuid, sender, tpl);
    				try {
    					json.put("data", 2);
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
	         } catch (Exception e) {
				  // 发送邮件异常
					try {
						json.put("data", 3);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				  //log.error("send email exception {}.", e.getMessage());
	         }
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	//用户协议
	@RequestMapping(value = "/treaty.jspx")
	public String treaty(HttpServletRequest request, ModelMap model) {
			Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
			model.addAttribute("global", com.jspgou.core.web.SiteUtils.getWeb(request).getGlobal());
			ShopFrontHelper.setCommonData(request, model, web, 1);
			return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,
					REGISTER_TREATY),request);
	}
	
	//检查用户名是否唯一
	@RequestMapping(value="/username_unique.jspx")
	public void checkUsername(HttpServletRequest request,
			HttpServletResponse response) {
		String username = RequestUtils.getQueryParam(request, "username");
		// 用户名为空，返回false。
		if (StringUtils.isBlank(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// 用户名存在，返回false。
		if (userMng.usernameExist(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}
	
	//检查邮箱是否唯一
	@RequestMapping(value="/email_unique.jspx")
	public void checkEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String email = RequestUtils.getQueryParam(request, "email");
		// email为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// email存在，返回false。
		if (userMng.emailExist(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		} 
		ResponseUtils.renderJson(response, "true");
	}
	private WebErrors validate(String checkcode, String username, String email,
			String password, HttpServletRequest request,
			HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		String id = session.getSessionId(request, response);
		if (errors.ifOutOfLength(checkcode, "checkcode", 3, 10)) {
			return errors;
		}
		if (errors.ifOutOfLength(password, "password", 3, 32)) {
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
		if (userMng.emailExist(email)) {
			errors.addErrorCode("error.emailExist");
			return errors;
		}
		if (errors.ifNotUsername(username, "username", 3, 100)) {
			return errors;
		}
		if (userMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");
			return errors;
		}
		return errors;
	}

	@Autowired
	private UserMng userMng;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private ShopScoreMng shopScoreMng;
	@Autowired
	private MemberMng memberMng;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private SessionProvider session;
	@Autowired
	private WebserviceMng webserviceMng;
}
