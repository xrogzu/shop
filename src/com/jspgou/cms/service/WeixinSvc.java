package com.jspgou.cms.service;

import java.util.Set;

import com.jspgou.cms.entity.Product;


/**
* This class should preserve.
* @preserve
*/
public interface WeixinSvc {
	/**
	 * 获取公众号的token
	 * @return  access_token
	 * access_token需要有缓存 微信限制2000次/天
	 */
	public String getToken();
	
	/**
	 * 获取公众号的订阅用户
	 * @return
	 */
	public Set<String> getUsers(String access_token);
	/**
	 * 发送纯文本消息
	 * @param title
	 * @param content
	 */
	public void sendText(String access_token,String content);
	/**
	 * 上传多媒体视频
	 * @param access_token
	 * @param filePath
	 */
	public String uploadFile(String access_token,String filePath,String type);
	/**
	 * 发送视频消息
	 * @param media_id
	 * @param title
	 * @param description
	 */
	public void sendVedio(String access_token,String title,String description,String media_id);
	/**
	 * 发送图文消息
	 * @param url
	 * @param picurl
	 * @param title
	 * @param description
	 */
	public void sendContent(String access_token,String title,String description,String url,String picurl);
	
	/**
	 * 创建自定义菜单
	 * @param menus 自定义菜单内容
	 */
	public void createMenu(String menus);
	/**
	 * 微信群发消息
	 * @param beans
	 */
	public void sendTextToAllUser(Product[] beans);
	
	/**
	 * 获取微信登陆时的accesstoken
	 * 通过code换取access_token频率限制在1万/分钟
	 * code用微信登录时，通过api返回的临时code凭据		
	 */
	public String getAccesstoken(String code);
	
	/**
	 * 检验accesstoken是否有效
	 */
	public String testToken(String accesstoken,String openid);
	
	/**
	 * 获取登录的用户信息
	 */
	public String getUserInfo(String accesstoken,String openid);
	
	
}
