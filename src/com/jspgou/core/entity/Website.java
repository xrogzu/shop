package com.jspgou.core.entity;
import com.jspgou.core.entity.base.BaseWebsite;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
* This class should preserve.
* @preserve
*/
public class Website extends BaseWebsite{
    private static final long serialVersionUID = 1L;
    
    public static final String RES_BASE = "r/gou/www";
    public static final String USER_BASE = "t";
    public static final String FRONT_RES = "/res/front";
    public static final String UPLOAD_PATH = "u";
    public static final String TEMPLATE_PATH = "gou/tpl";
    public static final String DEFAULT = "default";
    public static final String TPL_SUFFIX = ".html";
    public static final String TPL_PREFIX_SYS = "sys_";
    public static final String TPL_PREFIX_TAG = "tag_";
    //public static final String TPL_BASE = "/WEB-INF/t/gou/tpl";
    /**
	 * 模板路径
	 */
	public static final String TPL_BASE = "/WEB-INF/t/gou";
	public static final String UA= "ua";
    public static final String TEMPLATE_MOBILE_PATH = "mobile";
	 
	
	/* [CONSTRUCTOR MARKER BEGIN] */
    public Website(){
    	super();
    }

	/**
	 * Constructor for primary key
	 */
    public Website(Long id){
        super(id);
    }
    
	/**
	 * Constructor for required fields
	 */
    public Website(Long id, Global global, String domain, String name,
    		String currentSystem, String suffix, Integer lft, Integer rgt, Date createTime, 
            Boolean close, Boolean relativePath, String frontEncoding, String frontContentType, 
            String localeFront, String localeAdmin, Integer controlNameMinlen, String company, 
            String copyright, String recordCode, String email, String phone, String mobile){
        super(id, global, domain,  name, currentSystem, suffix, lft, 
        		rgt, createTime, close, relativePath, frontEncoding, frontContentType, localeFront,
        		localeAdmin, controlNameMinlen, company, copyright,recordCode, email, phone, mobile);
    }

    public String getTemplate(String dir, String name, String s2, String s3,HttpServletRequest request){
        //StringBuilder stringbuilder = getTemplateRelBuff().append("/").append(dir).append("/");
    	String equipment=(String) request.getAttribute(UA);
    	StringBuilder stringbuilder = getTemplateRelBuff().append("/").append(dir).append("/");
    	if(StringUtils.isNotBlank(equipment)&&equipment.equals(TEMPLATE_MOBILE_PATH)){
    		stringbuilder = getTemplateMobileRelBuff().append("/").append(dir).append("/");
    	}
    	
    	if(!StringUtils.isBlank(s3)){
            stringbuilder.append(s3);
        }
        stringbuilder.append(name);
        if(!StringUtils.isBlank(s2)){
            stringbuilder.append("_").append(s2);
        }
        return stringbuilder.append(TPL_SUFFIX).toString();
    }

    public String getUrl(){
        return getUrlBuff(false).append("/").toString();
    }

    public String getUploadRel(String s){
        StringBuilder stringbuilder = (new StringBuilder("/")).append(UPLOAD_PATH);
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    

    public String getUploadUrls(String s){
        StringBuilder stringbuilder = getResBaseUrlBuff().append("/").append(UPLOAD_PATH);
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public String getUploadUrl(String s){
        StringBuilder stringbuilder = getResBaseUrlBuff();
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
   
    /**
	 * 获得模板路径。如：/WEB-INF/t/gou/tpl
	 * 
	 * @return
	 */
	public String getTplPath() {
		return TPL_BASE;
	}

    public String getTemplateRel(String s,HttpServletRequest request){
       //StringBuilder stringbuilder = getTemplateRelBuff();
    	String equipment=(String)request.getAttribute(UA);
    	StringBuilder stringbuilder = getTemplateRelBuff();
    	if(StringUtils.isNotBlank(equipment)&&equipment.equals(TEMPLATE_MOBILE_PATH)){
    		stringbuilder=getTemplateMobileRelBuff();
        }
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    
    
    public String getResBaseRel(String s){
        StringBuilder stringbuilder = getResBaseRelBuff();
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public Boolean getSsoEnable(){
    	String ssoenable = null;
    	try{
    	 ssoenable=getAttr().get("ssoEnable");			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		if(StringUtils.isBlank(ssoenable)){
    		return false;
    	}else if(ssoenable.equals("true")){
    		return true;
    	}else{
    		return false;
    	}		
    }
    
    public Map<String,String> getSsoAttr(){
    	Map<String,String> ssoMap=new HashMap<String,String>();
    	Map<String,String> attr=getAttr();
    	for(String ssoKey:attr.keySet()){
    		if(ssoKey.startsWith("sso_")){
    			ssoMap.put(ssoKey, attr.get(ssoKey));
    		}
    	}
    	return ssoMap;
    }
    
	public List<String> getSsoAuthenticateUrls() {
		Map<String,String>ssoMap=getSsoAttr();
		List<String>values=new ArrayList<String>();
		for(String key:ssoMap.keySet()){
			values.add(ssoMap.get(key));
		}
		return values;
	}
    
    public WebsiteAttr getWebsiteAttr(){
    	WebsiteAttr websiteAttr=new WebsiteAttr(getAttr());
    	return websiteAttr;
    }
    
    public String getFrontResUrl(){
        return getUrlBuff(false).append(FRONT_RES).toString();
    }
    
    public String getResBaseUrl(String s){
        StringBuilder stringbuilder = getResBaseUrlBuff();
        if(!StringUtils.isBlank(s)){
            if(!s.startsWith("/")){
                stringbuilder.append("/");
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }

    private StringBuilder getUserBaseRelBuff(){
        return (new StringBuilder("/")).append("WEB-INF").append("/").append(USER_BASE);
    }

    private StringBuilder getResBaseRelBuff(){
        return (new StringBuilder("/")).append(RES_BASE);
    }

    private StringBuilder getResBaseUrlBuff(){
        return getUrlBuff(false).append("/").append(RES_BASE);
    }

    public String getResBaseUrl(){
        return getResBaseUrlBuff().toString();
    }
  

    public String getTemplateRel(HttpServletRequest request){
        return getTemplateRel(null,request);
    }
    public String getTemplateRel1(){
    	StringBuilder stringbuilder = null;
    	stringbuilder = getTemplateBuff();
         return stringbuilder.toString();
    }
      
    //模板总路径
    public StringBuilder getTemplateBuff(){
        StringBuilder stringbuilder = getUserBaseRelBuff().append("/").append("gou").append("/");
        return stringbuilder;
    }
    
    
    
    //pc端路径
    public StringBuilder getTemplateRelBuff(){
        StringBuilder stringbuilder = getUserBaseRelBuff().append("/").append(TEMPLATE_PATH);
        return stringbuilder;
    }
    //移动端路径
    public StringBuilder getTemplateMobileRelBuff(){
    	StringBuilder stringbuilder=getUserBaseRelBuff().append("/").append("gou").append("/").append(TEMPLATE_MOBILE_PATH);
    	return stringbuilder;
    }
 
	
    public StringBuilder getUrlBuff(boolean flag){
        StringBuilder stringbuilder = new StringBuilder();
        if(flag || !getRelativePath().booleanValue()){
            stringbuilder = (new StringBuilder("http://")).append(getDomain());
            Integer integer = getGlobal().getPort();
            if(integer != null && integer.intValue() != 80){
                stringbuilder.append(":").append(integer);
            }
        }
        if(getContextPath()!=null){
        stringbuilder.append(getContextPath());
        }
        return stringbuilder;
    }

    public String getResBaseRel(){
        return getResBaseRelBuff().toString();
    }

    public String getUploadRel(){
        return getUploadRel(null);
    }

    public String getUploadUrl(){
        return getUploadUrl(null);
    }

    public String getTemplate(String dir, String name, HttpServletRequest request){
        return getTemplate(dir, name, null, null,request);
    }

    public String getTplSys(String dir, String name,HttpServletRequest request){
        return getTemplate(dir, name, null, null,request);
    }
    public String getTplTag(String s, String s1, String s2){
          return getTemplate(s, s1, s2, null,null);
    }

    public String getContextPath(){
        String s = getGlobal().getContextPath();
        return StringUtils.isBlank(s) ? "" : s;
    }
  
    public Integer getPort(){
    	return getGlobal().getPort();
    }

    
	
    public String getMobileSolutionPath() {
		return TPL_BASE + "/" + getTplMobileSolution();
	}
     /**
 	 * 获得模板方案路径。如：/WEB-INF/t/cms/www/default
 	 * 
 	 * @return
 	 */
 	public String getSolutionPath() {
 		return TPL_BASE + "/" + getTplSolution();
 	}
    
    public String[] getDomainAliasArray(){
        String s = getDomainAlias();
        if(!StringUtils.isBlank(s)){
            return s.split(",");
        }else{
            return null;
        }
    }

}
