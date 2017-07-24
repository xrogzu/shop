package com.jspgou.cms.action.front;

import static com.jspgou.cms.Constants.MEMBER_SYS;


import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.threadvariable.AdminThread;
import com.jspgou.cms.web.threadvariable.MemberThread;
import com.jspgou.common.security.encoder.PwdEncoder;
import com.jspgou.common.web.HttpRequestUtil;
import com.jspgou.common.web.LoginUtils;

import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.session.SessionProvider;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.ThirdAccount;
import com.jspgou.core.entity.User;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.GlobalMng;
import com.jspgou.core.manager.ThirdAccountMng;
import com.jspgou.core.manager.UserMng;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.service.WeixinSvc;
@Controller
public class ThirdLoginAct {
	
	private static final Logger log = LoggerFactory.getLogger(ThirdLoginAct.class);
	
	public static final String TPL_AUTH = "tpl.member.auth";
	public static final String TPL_BIND = "tpl.member.bind";
	public static final String UNIFORM_SINGLE_INTERFACE="https://open.weixin.qq.com/connect/qrconnect";
	public static final String characterEncodingUTF8 = "UTF-8";

	
	//用户授权返回
	@RequestMapping(value = "/public_auth.jspx")
	public String auth(String openId,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);	
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,TPL_AUTH),request);
	}
	
	//pc端微信登陆
	@RequestMapping(value = "/public_wechat_login.jspx")
	public String weChatLogin(String code,String state,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws IOException, JSONException{
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		JSONObject json=new JSONObject();
		//防止csrf攻击
		if(StringUtils.isBlank(state)){
			return FrontHelper.pageNotFound(web, model, request);
		}
		//通过code参数去获取用户openid和access_token,进而获得用户的信息
		String AccessToken=WeixinSvc.getAccesstoken(code);
		json=new JSONObject(AccessToken);
		String oppenId=json.getString("openid");//通用户的标识，对当前开发者帐号唯一
		WebErrors error=validateKey(oppenId,request);
		if(error.hasErrors()){
			return FrontHelper.showError(error, web, model, request);
		}
		if("WECHAT".equals(ThirdAccount.WECHAT_PLAT)){
			session.setAttribute(request, response, ThirdAccount.WECHAT_KEY, oppenId);
		}
		if(StringUtils.isNotBlank(oppenId)){
			oppenId=pwdEncoder.encodePassword(oppenId);
		}
       String token=json.getString("access_token");
    	//检验微信登录授权凭证（access_token）是否有效
       String TestToken=WeixinSvc.testToken(token, oppenId);
		json=new JSONObject(TestToken);
		 //微信会返回JSON数据包
	   String errcode=json.getString("errcode");
	   String errmsg=json.getString("errmsg");
	   String nickname=null;//用户昵称
	  
	   if(errcode.equals("0")&&errmsg.equals("ok")){
			String getUserinfo=WeixinSvc.getUserInfo(token, oppenId);
			json=new JSONObject(getUserinfo);
			nickname=json.getString("nickname");							
		}
		ThirdAccount account=accountMng.findByKey(oppenId);
		model.addAttribute("WECHAT", "WECHAT");
	     if(account!=null){
	    	 
	    	if(!account.getThirdLoginName().equals(nickname)){
	    		account.setThirdLoginName(nickname);
	    		accountMng.update(account);	
	    	} 
	    	model.addAttribute("succ", true);

			//已绑定直接登陆
			loginByKey(oppenId, request, response, model);
	     }else{
				model.addAttribute("nickname", nickname);
				model.addAttribute("succ", false);
			}

			ShopFrontHelper.setCommonData(request, model, web, 1);
	  
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,TPL_AUTH),request);
	}
	
	
	//绑定新用户
	@RequestMapping(value = "/public_bind_username.jspx")
	public String bind_username_post(String username,String password,String nickname,Boolean sina,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) throws IOException {

		Website web=SiteUtils.getWeb(request);
		WebErrors errors = WebErrors.create(request);
		String source="";
		if(StringUtils.isBlank(username)){
			//用户名为空
			errors.addErrorCode("error.usernameRequired");
		}else{
			boolean usernameExist=userMng.usernameExist(username);
			if(usernameExist){
				//用户名存在
				errors.addErrorCode("error.usernameExist");
			}else{
				//获取用户来源
				String openId=(String) session.getAttribute(request, ThirdAccount.QQ_KEY);
				String uid=(String) session.getAttribute(request, ThirdAccount.SINA_KEY);
				String wechatOpenId=(String) session.getAttribute(request, ThirdAccount.WECHAT_KEY);
				//(获取到登录授权key后可以注册用户)
				if(StringUtils.isNotBlank(openId)||StringUtils.isNotBlank(uid)||StringUtils.isNotBlank(wechatOpenId)){
					//初始设置密码同用户名
					shopMemberMng.register(username, password, null,true,null,
							null,null,web.getId(), 1L);

				}
				if(StringUtils.isNotBlank(openId)){
					source=ThirdAccount.QQ_PLAT;
				}else if(StringUtils.isNotBlank(uid)){
					source=ThirdAccount.SINA_PLAT;
				}else if(StringUtils.isNotBlank(wechatOpenId)){
					source=ThirdAccount.WECHAT_PLAT;
				}
				//提交登录并绑定账号
				loginByUsername(username,nickname,request, response, model);
			}
		}
		if(errors.hasErrors()){
			errors.toModel(model);
			model.addAttribute("success",false);
		}else{
			model.addAttribute("success",true);
		}
		model.addAttribute("source", source);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,TPL_BIND),request);
	}
	
	
	@RequestMapping(value = "/public_auth_login.jspx")
	public void authLogin(String key,String source,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JSONException {
		if(StringUtils.isNotBlank(source)){
			if(source.equals(ThirdAccount.QQ_PLAT)){
				session.setAttribute(request,response,ThirdAccount.QQ_KEY, key);
			}else if(source.equals(ThirdAccount.QQ_WEBO_PLAT)){
				session.setAttribute(request,response,ThirdAccount.QQ_WEBO_KEY, key);
			}else if(source.equals(ThirdAccount.SINA_PLAT)){
				session.setAttribute(request,response,ThirdAccount.SINA_KEY, key);
			}
		}
		JSONObject json=new JSONObject();
		//库中存放的是加密后的key
		if(StringUtils.isNotBlank(key)){
			key=pwdEncoder.encodePassword(key);
		}
		ThirdAccount account=accountMng.findByKey(key);
		if(account!=null){
			json.put("succ", true);
			//已绑定直接登陆
			loginByKey(key, request, response, model);
		}else{
			json.put("succ", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	
	
	
	@RequestMapping(value = "/public_bind.jspx",method = RequestMethod.GET)
	public String bind_get(String nickname,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		model.addAttribute("nickname", nickname);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,TPL_BIND),request);

	}
	
	
	
	
	//判断用户是否登陆
	@RequestMapping(value="/sso/authenticate.jspx")
	public void authenticate(String username,String sessionId,HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		User user=userMng.getByUsername(username);
		if(user!=null&&sessionId!=null){
			String userSessionId=user.getSessionId();
			if(StringUtils.isNotBlank(userSessionId)){
				if(userSessionId.equals(sessionId)){
					ResponseUtils.renderJson(response, "true");
				}
			}else{
				ResponseUtils.renderJson(response, "false");
			}
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/sso/login.jspx")
	public void loginSso(String username,String sessionId,String ssoLogout,HttpServletRequest request,HttpServletResponse response) {
		User user=null;
		ShopAdmin admin=AdminThread.get();
		if(admin!=null){
			user=admin.getAdmin().getUser();
		}
		ShopMember member=MemberThread.get();
		if(member!=null){
			user=member.getMember().getUser();
		}
		Website site=SiteUtils.getWeb(request);
		if(StringUtils.isNotBlank(username)){
			JSONObject object =new JSONObject();
			try {
				if(user==null){
					//未登录，其他地方已经登录，则登录自身
					List<String>authenticateUrls=site.getSsoAuthenticateUrls();
					String success=authenticate(username, sessionId, authenticateUrls);
					if(success.equals("true")){
						LoginUtils.loginShiro(request, response, username);
						user = userMng.getByUsername(username);
						if(user!=null){
							userMng.updateLoginInfo(user.getId(), null,null,sessionId);
						}
						object.put("result", "login");
					}
				}else if(StringUtils.isNotBlank(ssoLogout)&&ssoLogout.equals("true")){
					LoginUtils.logout();
					object.put("result", "logout");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseUtils.renderJson(response, object.toString());
		}
	}
	
	
	
	
	/**
	 * 已绑定用户key登录
	 * @param key
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByKey(String key,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		ThirdAccount account=accountMng.findByKey(key);
		if(StringUtils.isNotBlank(key)&&account!=null){
			String username=account.getUsername();
			loginShiro(request, response, username);
		}
	}
	
	/**
	 * 用户名登陆,绑定用户名和第三方账户key
	 * @param username
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByUsername(String username,String nickname,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String openId=(String) session.getAttribute(request, ThirdAccount.QQ_KEY);
		String uid=(String) session.getAttribute(request, ThirdAccount.SINA_KEY);
		String weChatOpenId=(String) session.getAttribute(request, ThirdAccount.WECHAT_KEY);
		if(StringUtils.isNotBlank(openId)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username,nickname,openId,ThirdAccount.QQ_PLAT);
		}
		if(StringUtils.isNotBlank(uid)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username,nickname,uid,ThirdAccount.SINA_PLAT);
		}
		if(StringUtils.isNotBlank(weChatOpenId)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username,nickname,weChatOpenId,ThirdAccount.WECHAT_PLAT);
		}
	}
	
	
	private void bind(String username,String nickname,String openId,String source){
		ThirdAccount account=accountMng.findByKey(openId);
		if(account==null){
			account=new ThirdAccount();
			account.setUsername(username);			
			account.setThirdLoginName(nickname);//昵称
			//第三方账号唯一码加密存储 防冒名登录
			openId=pwdEncoder.encodePassword(openId);
			account.setAccountKey(openId);
			account.setSource(source);
			accountMng.save(account);
		}
	}
	private WebErrors validateKey(String key, HttpServletRequest request){
		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isBlank(key)) {
			errors.addErrorString("网站系统后台参数错误");
			return errors;
		}
		return errors;
	}
	
	private void loginShiro(HttpServletRequest request,HttpServletResponse response,String username){
		PrincipalCollection principals = new SimplePrincipalCollection(username, username);  
		Builder builder = new WebSubject.Builder( request,response);  
		builder.principals(principals);  
		builder.authenticated(true);  
		WebSubject subject = builder.buildWebSubject();  
		ThreadContext.bind(subject); 
	}
	
	
	private String authenticate(String username,String sessionId,List<String>authenticateUrls){
		String result="false";
		for(String url:authenticateUrls){
			result=authenticate(username, sessionId, url);
			if(result.equals("true")){
				break;
			}
		}
		return result;
	}
	
	private String authenticate(String username,String sessionId,String authenticateUrl){
		Map<String,String>params=new HashMap<String, String>();
		params.put("username", username);
		params.put("sessionId", sessionId);
		String success="false";
		try {
			success=HttpRequestUtil.request(authenticateUrl, params, "post", "utf-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	
	@Autowired 
	private UserMng userMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private ThirdAccountMng accountMng;
	@Autowired
	private GlobalMng globalMng;
	@Autowired
	private WeixinSvc WeixinSvc;
	@Autowired
	private ShopMemberMng shopMemberMng;

}
