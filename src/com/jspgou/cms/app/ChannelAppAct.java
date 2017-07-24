package com.jspgou.cms.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.ShopChannel;
import com.jspgou.cms.manager.ShopChannelMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.core.entity.Website;

@Controller 
public class ChannelAppAct{
	
	
	/**
	 * 栏目列表
	 * 相关参数协议：
	 * 00	请求失败
	 * 01	请求成功
	 * 02	返回空值
	 * 03	请求协议参数不完整
	 * @throws JSONException 
	 * 
	 */
	@RequestMapping(value="/api/channelList.jspx")
	public void channelList(HttpServletRequest request ,HttpServletResponse response) throws JSONException{
		Website web=SiteUtils.getWeb(request);
		Map<String,Object> map=new HashMap<String, Object>();
		String parentId=request.getParameter("parentId");//父级栏目Id
		String count=request.getParameter("count");//查询条数
		ShopChannel channel;
		List<ShopChannel> list = null ;
		String result="00";
		if(StringUtils.isNotBlank(parentId)){
			channel=channelMng.findById(Long.parseLong(parentId));
			if(channel!=null){
				list=new ArrayList<ShopChannel>(channel.getChild());
			}else{
				list=new ArrayList<ShopChannel>(); 
			}
		}else{
			if(StringUtils.isNotBlank(count)){
				list=channelMng.getTopListForTag(web.getId(),Integer.parseInt(count));	
			}
		}
		if(list!=null){
			if(list.size()>0){
				result="01";
				map.put("pd", getShopChannelJson(list));
			}else{
				result="02";
			}
		}else{
			    result="02";
		}
		map.put("result", result);
	}
	
	
	private String getShopChannelJson(List<ShopChannel> beanList) throws JSONException{
		JSONObject o=new JSONObject();
		JSONArray arr=new JSONArray();
		for(ShopChannel channel:beanList){
			o.put("id", channel.getId());
			o.put("name", channel.getName());
			arr.put(o);
		}
		
		return arr.toString();
	}
	
	@Autowired
	private ShopChannelMng channelMng;
	
}