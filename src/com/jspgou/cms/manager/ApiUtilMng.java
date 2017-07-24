package com.jspgou.cms.manager;

import javax.servlet.http.HttpServletRequest;

import com.jspgou.cms.entity.ShopMember;


public interface ApiUtilMng {
	
	public ShopMember findbysessionKey(HttpServletRequest request);
	 
	public String getJsonStrng(String message,String url,Boolean decryptionFailure,HttpServletRequest request);
	
	public String getJsonStrng(String message,String url,HttpServletRequest request);
	
	public String getEncryptpass(HttpServletRequest request)throws Exception;
	
	public Boolean verify(HttpServletRequest request);
}