package com.jspgou.cms.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.jspgou.cms.web.CmsThreadVariable;
import com.jspgou.core.entity.Global;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.GlobalMng;
import com.jspgou.cms.Constants;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.service.WeixinSvc;
import com.jspgou.common.upload.FileUpload;
import com.jspgou.common.util.PropertyUtils;
import com.jspgou.cms.service.WeixinTokenCache;

import com.jspgou.common.web.springmvc.RealPathResolver;
import com.jspgou.plug.weixin.entity.Weixin;
import com.jspgou.plug.weixin.manager.WeixinMng;


/**
 * @preserve 
 */
@Service
public class WeixinSvcImpl implements WeixinSvc {
	private static final Logger log = LoggerFactory.getLogger(WeixinSvcImpl.class);
	//微信token地址key
	public static final String TOKEN_KEY="weixin.address.token";
	//微信公众号关注用户地址key
	public static final String USERS_KEY="weixin.address.users";
	//微信发送消息地址key
	public static final String SEND_KEY="weixin.address.send";
	//微信上传地址key
	public static final String UPLOAD_KEY="weixin.address.upload";
	//微信创建菜单key
	public static final String MENU_KEY="weixin.address.menu";
	//微信上传临时图文素材
	public static final String UPLOAD_NEWS="weixin.address.uploadnews";
	//群发消息
	public static final String SEND_ALL_MESSAGE="weixin.address.sendAllMessage";
	//微信图文消息上传图片地址
	public static final String UPLOAD_IMG_URL="weixin.address.uploadimg";
	//微信新增永久素材接口地址
	public static final String ADD_NEWS="weixin.address.addNews";
	//微信永久素材图片上传接口地址
	public static final String UPLOAD_MATERIAL_IMG_URL="weixin.address.addMaterial";
	//每次抽取关注号数量
	public static final Integer USERS_QUERY_MAX=10000;
	//微信支付时获取微信用户的oppenid前一步code地址
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getCodeUrl";
	//微信登陆token地址KEY
	public static final String ACCESSTOKEN_KEY="weixin.auth.getAccessTokenUrl";
	//检验token是否有效地址
	public static final String TESTTOKEN_KEY="weixinLogin.address.testtoken";
	//获取用户信息地址key
	public static final String USERINFO_KEY="weixinLogin.address.userinfo";
	
	public String getToken() {
		String tokenGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),TOKEN_KEY);
		String appid="";
		String secret="";
		Website site=CmsThreadVariable.getSite();
       Weixin weixin=weixinMng.findBy();
		if(site!=null){
			appid=weixin.getAppKey();
			secret=weixin.getAppSecret();
		}
		JSONObject tokenJson=new JSONObject();
		if(StringUtils.isNotBlank(appid)&&StringUtils.isNotBlank(secret)){
			tokenGetUrl+="&appid="+appid+"&secret="+secret;
			tokenJson=getUrlResponse(tokenGetUrl);
			try {
				return (String) tokenJson.get("access_token");
			} catch (JSONException e) {
				return null;
			}
		}else{
			return null;
		}
	}
	
	public Set<String> getUsers(String access_token) {
		String usersGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),USERS_KEY);
		usersGetUrl+="?access_token="+access_token;
		JSONObject data=getUrlResponse(usersGetUrl);
		Set<String>openIds=new HashSet<String>();
		Integer total=0,count=0;
		try {
			total=(Integer) data.get("total");
			count=(Integer) data.get("count");
			//总关注用户数超过默认一万
			if(count<total){
				openIds.addAll(getUsers(openIds,usersGetUrl, access_token, (String)data.get("next_openid")));
			}else{
				//有关注者 json才有data参数
				if(count>0){
					JSONObject openIdData=(JSONObject) data.get("data");
					JSONArray openIdArray= (JSONArray) openIdData.get("openid");
					for(int i=0;i<openIdArray.length();i++){
						openIds.add((String) openIdArray.get(i));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openIds;
	}
	
	public String uploadFile(String access_token,String filePath,String type){
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),UPLOAD_KEY);
		String url = sendGetUrl+"?access_token=" + access_token;
		String result = null;
		String mediaId="";
		FileUpload fileUpload = new FileUpload();
		try {
			result = fileUpload.uploadFile(url,filePath, type);
			if(result.startsWith("{")&&result.contains("media_id")){
				JSONObject json=new JSONObject(result);
				mediaId=json.getString("media_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return mediaId;
	}

	public void sendText(String access_token,String content) {
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),SEND_KEY);
        String url = sendGetUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		content=filterCharacters(content);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"text\",";
	        strJson += "\"text\":{";
	        strJson += "\"content\":\""+content+"\"";
	        strJson += "}}";
		    post(url, strJson,"application/json");
		}
	}
	
	public void sendContent(String access_token,String title, String description, String url,
			String picurl) {
		String sendUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),SEND_KEY);
        sendUrl = sendUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		if(description==null){
			description="";
		}
		title=filterCharacters(title);
		description=filterCharacters(description);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"news\",";
	        strJson += "\"news\":{";
	        strJson += "\"articles\": [{";
	        strJson +="\"title\":\""+title+"\",";    
	        strJson +="\"description\":\""+description+"\",";  
	        strJson +="\"url\":\""+url+"\",";  
	        strJson +="\"picurl\":\""+picurl+"\"";  
	        strJson += "}]}}";
		    post(sendUrl, strJson,"application/json");
		}
	}

	public void sendVedio(String access_token,String title, String description, String media_id) {
		String sendGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),SEND_KEY);
        String url = sendGetUrl+"?access_token=" + access_token;
		Set<String> openIds=getUsers(access_token);
		if(description==null){
			description="";
		}
		title=filterCharacters(title);
		description=filterCharacters(description);
	    //发送给所有关注者消息
		for(String openId:openIds){
		    String strJson = "{\"touser\" :\""+openId+"\",";
	        strJson += "\"msgtype\":\"video\",";
	        strJson += "\"video\":{";
	        strJson += "\"media_id\":\""+media_id+"\",";
	        strJson += "\"title\":\""+title+"\",";
	        strJson += "\"description\":\""+description+"\"";
	        strJson += "}}";
		    post(url, strJson,"application/json");
		}
	}
	
	private Set<String> getUsers(Set<String>openIds,String url,String access_token,String next_openid) {
		JSONObject data=getUrlResponse(url);
		try {
			Integer count=(Integer) data.get("count");
			String nextOpenId=(String) data.get("next_openid");
			if(count>0){
				JSONObject openIdData=(JSONObject) data.get("data");
				JSONArray openIdArray= (JSONArray) openIdData.get("openid");
				for(int i=0;i<openIdArray.length();i++){
					openIds.add((String) openIdArray.get(i));
				}
			}
			if(StringUtils.isNotBlank(nextOpenId)){
				return getUsers(openIds,url, access_token, nextOpenId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return openIds;
	}
	
	/**
	 * 创建自定义菜单
	 */
	public void createMenu(String menus){
		String token=weixinTokenCache.getToken();
		String createMenuUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),MENU_KEY);
		String url = createMenuUrl+"?access_token=" + token;
		post(url, menus,"application/json");
	}
	
	
	/**
	 * 群发
	 */
	public void sendTextToAllUser(Product[] beans){
		String token=weixinTokenCache.getToken();
		//上传内容到微信
		String articalUploadUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),UPLOAD_NEWS);
		String url = articalUploadUrl+"?access_token=" + token;
		
		String[] str = articalUpload(token, beans);
		Integer contentCount=0;
		contentCount=Integer.parseInt(str[1]);
		if(contentCount>0){
			DefaultHttpClient client = new DefaultHttpClient();
		       client = (DefaultHttpClient) wrapClient(client);
		       HttpPost post = new HttpPost(url);
		       try{
		           StringEntity s = new StringEntity(str[0],"utf-8");
		           s.setContentType("application/json");
		           post.setEntity(s);
		           HttpResponse res = client.execute(post);
		           HttpEntity entity = res.getEntity();
		           String contentString=EntityUtils.toString(entity, "utf-8");
		           JSONObject json=new JSONObject(contentString);	   
		           //输出返回消息
		           String media_id="";
		           if(contentString.contains("media_id")){
		        	   media_id  = (String) json.get("media_id");
		           }
		           if(StringUtils.isNotBlank(media_id)){
		        	   String sendAllMessageUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),SEND_ALL_MESSAGE);
			   		   String url_send = sendAllMessageUrl+"?access_token=" + token;
			   		   String str_send = "{\"filter\":{\"is_to_all\":true},\"mpnews\":{\"media_id\":\""+media_id+"\"},\"msgtype\":\"mpnews\"}";
			   		   post(url_send, str_send,"application/json");
		           }
		       }catch (Exception e){
		    	   e.printStackTrace();
		       }
		}
	}
	
	private String[] articalUpload(String token,Product[] beans){
		Integer count=0;
		//图文消息，一个图文消息支持1到8条图文【必传：是】
		String str = "{\"articles\":[";
		for(int i=0;i<beans.length;i++){
			Product bean = beans[i];
			String author = bean.getWebsite().getName();  //站点名字
        	String sourceUrl=bean.getWeixinProductUrl();  //地址
			String mediaId ="";
			if(!StringUtils.isBlank(bean.getCoverImgUrl())){  //类型图
				String typeImg=bean.getCoverImgUrl();
				String contextPath=bean.getWebsite().getGlobal().getContextPath();   //部署路径
				if(StringUtils.isNotBlank(contextPath)&&typeImg.startsWith(contextPath)){
					typeImg=realPathResolver.get(typeImg.substring(contextPath.length()));
				}else{
					typeImg=realPathResolver.get(typeImg);
				}
				mediaId = uploadFile(token,realPathResolver.get(bean.getCoverImgUrl()), "image");
				str = str+"{"+
                        "\"thumb_media_id\":\""+mediaId+"\","+ //图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得【必传：是】
                        "\"author\":\""+author+"\","+    //图文消息的作者【必传：否】
						 "\"title\":\""+bean.getName()+"\","+   //图文消息的标题【必传：是】
						 "\"content_source_url\":\""+sourceUrl+"\","+   //在图文消息页面点击“阅读原文”后的页面，受安全限制，如需跳转Appstore，可以使用itun.es或appsto.re的短链服务，并在短链后增加 #wechat_redirect 后缀。【必传：否】
						 "\"content\":\""+bean.getText()+"\","+  //图文消息页面的内容，支持HTML标签。具备微信支付权限的公众号，可以使用a标签，其他公众号不能使用【必传：是】
						 "\"show_cover_pic\":\"0\""+"}";      //是否显示封面，1为显示，0为不显示【必传：否】
				if(i!=beans.length-1){
					str = str+",";
				}
				count++;
			}
		}
		str = str +"]}";	
		String[]result=new String[2];
		result[0]=str;
		result[1]=count.toString();
		return result;
	}
	
	
	
	public String getAccesstoken(String code){
		String tokenGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),ACCESSTOKEN_KEY);
		String content="";
		Global global=globalMng.findIdkey();
		String appid = null;
		String secret = null;
		if(global!=null){
			appid=global.getWeixinID();
			secret=global.getWeixinKey();
		}
		try{
			StringBuffer sb=new StringBuffer();
			sb.append(tokenGetUrl);
			sb.append("&appid=").append(appid);
			sb.append("&secret=").append(secret);
			sb.append("&code=").append(code); 
			JSONObject tokenJson=getUrlResponse(sb.toString());	
			content= tokenJson.toString();
		}catch(Exception e){
			 e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 检验accesstoken是否有效
	 */
	public String testToken(String accesstoken,String openid){
		String testTokenUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),TESTTOKEN_KEY);
		String content="";
		try{
			StringBuffer sb=new StringBuffer();
			sb.append(testTokenUrl);
			sb.append("access_token=").append(accesstoken);
			sb.append("&openid=").append(openid);
			JSONObject tokenJson=getUrlResponse(sb.toString());
			content=tokenJson.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
	
	
	
	
	public String getUserInfo(String accesstoken,String openid){
		String userInfoGetUrl=PropertyUtils.getPropertyValue(new File(realPathResolver.get(Constants.JSPGOU_CONFIG)),USERINFO_KEY);
		String content="";
		try{
			StringBuffer sb=new StringBuffer();
			sb.append(userInfoGetUrl);
			sb.append("access_token=").append(accesstoken);
			sb.append("&openid=").append(openid);
			JSONObject tokenJson=getUrlResponse(sb.toString());
			content=tokenJson.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
	
	private JSONObject getUrlResponse(String url){
		CharsetHandler handler = new CharsetHandler("UTF-8");
		try {
			HttpGet httpget = new HttpGet(new URI(url));
		    DefaultHttpClient client = new DefaultHttpClient();
	        client = (DefaultHttpClient) wrapClient(client);
			return new JSONObject(client.execute(httpget, handler));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
   
	private void post(String url, String json,String contentType){
       DefaultHttpClient client = new DefaultHttpClient();
       client = (DefaultHttpClient) wrapClient(client);
       HttpPost post = new HttpPost(url);
       try{
           StringEntity s = new StringEntity(json,"utf-8");
           if(StringUtils.isBlank(contentType)){
        	   s.setContentType("application/json");
           }
           s.setContentType(contentType);
           post.setEntity(s);
           HttpResponse res = client.execute(post);
           HttpEntity entity = res.getEntity();
           String str=EntityUtils.toString(entity, "utf-8");
           log.info(str);
       }catch (Exception e){
    	   e.printStackTrace();
       }
    }
	
	private String filterCharacters(String txt){
		if(StringUtils.isNotBlank(txt)){
			txt=txt.replace("&ldquo;", "“").replace("&rdquo;", "”").replace("&nbsp;", " ");
		}
		return txt;
	}
	 
	private HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }
 
                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }
 
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
	
	private class CharsetHandler implements ResponseHandler<String> {
		private String charset;

		public CharsetHandler(String charset) {
			this.charset = charset;
		}

		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (!StringUtils.isBlank(charset)) {
					return EntityUtils.toString(entity, charset);
				} else {
					return EntityUtils.toString(entity);
				}
			} else {
				return null;
			}
		}
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private WeixinTokenCache weixinTokenCache;
	@Autowired
	private WeixinMng weixinMng;
	@Autowired
	private GlobalMng globalMng;

}
