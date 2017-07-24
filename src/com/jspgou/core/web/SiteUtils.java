package com.jspgou.core.web;

import javax.servlet.http.HttpServletRequest;

import com.jspgou.core.entity.Website;
/**
* This class should preserve.
* @preserve
*/
public abstract class SiteUtils{
	
    public static Website getWeb(HttpServletRequest request){
        Website website = (Website)request.getAttribute("_web_key");
        if(website == null){
            throw new IllegalStateException("Webiste not found in Request Attribute '_web_key'");
        }else{
            return website;
        }
    }

    public static Long getWebId(HttpServletRequest request){
        return getWeb(request).getId();
    }
    

}
