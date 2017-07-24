package com.jspgou.cms.manager.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.Alipay;
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.entity.ApiRecord;
import com.jspgou.cms.entity.ApiUserLogin;
import com.jspgou.cms.manager.ApiAccountMng;
import com.jspgou.cms.manager.ApiRecordMng;
import com.jspgou.cms.manager.ApiUserLoginMng;
import com.jspgou.cms.manager.ApiUtilMng;
import com.jspgou.common.util.AES128Util;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ShopMemberMng;

@Service
@Transactional
public class ApiUtilMngImpl implements ApiUtilMng {

	/**  
     * 相关参数协议：
     * 00	请求失败
     * 01	请求成功
     * 02	解密失败
     * 03   签名验证失败
     * 04	签名重复利用
	 */	
	public String getJsonStrng(String message,String url,Boolean decryptionFailure,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "00";
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		String callback = request.getParameter("callback");
		if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(sign)) {
			if(verifySign(appId,sign)){
				String validateSign = getValidateSign(appId, request);
				if(verifyValidateSign(sign,validateSign)){
					if(decryptionFailure){
						// 成功返回
						result = "01";
						if(StringUtils.isNotBlank(message)){
							map.put("pd", message);
						}
						if(StringUtils.isNotBlank(url)){
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),appId, url,sign);
						}
					}else{
						//解密失败
						result = "02";
					}
				}else{
					// 签名验证失败
					result = "03";
				}
			}else{
				// 签名数据重复利用
				result = "04";
			}
		}
		map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			return callback + "(" + ConvertMapToJson.buildJsonBody(map, 0, false) + ")";
		} else {
			return ConvertMapToJson.buildJsonBody(map, 0, false);
		}
	}
	
	
	
	/**  
     * 相关参数协议：
     * 00	请求失败
     * 01	请求成功
     * 02   签名验证失败
     * 03	签名重复利用
	 */	
	public String getJsonStrng(String message,String url,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "00";
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		String callback = request.getParameter("callback");
		if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(sign)) {
			if(verifySign(appId,sign)){
				String validateSign = getValidateSign(appId, request);
				if(verifyValidateSign(sign,validateSign)){
						// 成功返回
						result = "01";
						if(StringUtils.isNotBlank(message)){
							map.put("pd", message);
						}
						if(StringUtils.isNotBlank(url)){
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),appId, url,sign);
						}
				}else{
					// 签名验证失败
					result = "03";
				}
			}else{
				// 签名数据重复利用
				result = "04";
			}
		}
		map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			return callback + "(" + ConvertMapToJson.buildJsonBody(map, 0, false) + ")";
		} else {
			return ConvertMapToJson.buildJsonBody(map, 0, false);
		}
	}
 
	public String getEncryptpass(HttpServletRequest request) throws Exception {
		String encryptPass = null;
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		String aesPassword = request.getParameter("aesPassword");
		if (StringUtils.isNotBlank(appId)&&StringUtils.isNotBlank(aesPassword)&&StringUtils.isNotBlank(sign)) {
			if(verifySign(appId,sign)){
				String validateSign = getValidateSign(appId,request);
				if(verifyValidateSign(sign,validateSign)){
					ApiAccount apiAccount = apiAccountMng.findByAppId(appId);
					String aesKey = apiAccount.getAesKey();
					String ivVal = apiAccount.getIvKey();
					encryptPass = AES128Util.decrypt(aesPassword,aesKey,ivVal);
				}
			}
		}
		return encryptPass;
	}
 
	public ShopMember findbysessionKey(HttpServletRequest request){
		ShopMember user = null;
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		String sessionKey = request.getParameter("sessionKey");
		if (StringUtils.isNotBlank(appId)&&StringUtils.isNotBlank(sessionKey)&&StringUtils.isNotBlank(sign)) {
			if(verifySign(appId,sign)){
				String validateSign = getValidateSign(appId,request);
				if(verifyValidateSign(sign,validateSign)){
					ApiUserLogin apiUserLogin = apiUserLoginMng.findBySessionKey(sessionKey);
					if(apiUserLogin!=null){
						String username = apiUserLogin.getUsername();
						user = shopMemberMng.findUsername(username);
					}
				}
			}
		}
		return user;
	}
 
	
	public Boolean verify(HttpServletRequest request){
		Boolean verify = false;
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(sign)) {
			if(verifySign(appId,sign)){
				String validateSign = getValidateSign(appId, request);
				if(verifyValidateSign(sign,validateSign)){
					verify = true;
				}
			}
		}
		return verify;
	}
	
	
	//先验证签名是否重复利用
	public Boolean verifySign(String appId,String sign){
		Boolean verify = false;
		if(StringUtils.isNotBlank(appId)&&StringUtils.isNotBlank(sign)){
			ApiRecord apirecord = apiRecordMng.findBySign(sign, appId);
			if(apirecord==null){
				verify = true;
			}
		}
		return verify;
	} 	
		
	//先验证签名
	public Boolean verifyValidateSign(String sign,String validateSign){
		Boolean verify = false;
		if(StringUtils.isNotBlank(validateSign)&&StringUtils.isNotBlank(sign)){
			if(sign.equals(validateSign)){
				 verify = true;
			}
		}
		return verify;
	} 				

	//获取系统的签名
 	private String getValidateSign(String appId, HttpServletRequest request){
	    String validateSign;
	    ApiAccount apiAccount = apiAccountMng.findByAppId(appId);
		String appKey=apiAccount.getAppKey();
		Map<String,String>param=new HashMap<String, String>();
		Enumeration penum = (Enumeration) request.getParameterNames();
		while(penum.hasMoreElements()){
			String pKey=(String) penum.nextElement();
			String value=request.getParameter(pKey);
			//sign不参与 值为空也不参与
			if(!pKey.equals("sign")&&StringUtils.isNotBlank(value)){
				param.put(pKey,value);
			}
		}
		validateSign = Alipay.createSign(param, appKey);
		return validateSign; 
	}		
 	
 	
 	
 
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private ShopMemberMng shopMemberMng;

}