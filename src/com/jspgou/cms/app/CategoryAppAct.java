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

import com.jspgou.cms.entity.Category;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;



@Controller
public class CategoryAppAct {

	/**
	 * 分类列表
	 * 
	 *   相关参数协议：
	 * 00	请求失败
	 * 01	请求成功
	 * 02	返回空值
	 * 03	请求协议参数不完整
	 * @throws JSONException 
	 * 
	 */
	@RequestMapping(value = "/api/categoryList.jspx")
	public void categoryList(HttpServletRequest request,HttpServletResponse response) throws JSONException {
		Map<String,Object> map=new HashMap<String,Object>();
		String parentId = request.getParameter("parentId");//分类Id
		List<Category> list;
		Website web=SiteUtils.getWeb(request);
		String result="00";
		// 父类别
		if (StringUtils.isNotBlank(parentId)) {
			Category category = categoryMng.findById(Long.parseLong(parentId));// 商品类别
			if (category != null) {
				list = new ArrayList<Category>(category.getChild());
			} else {
				list = new ArrayList<Category>();
			}
		} else {

			list = categoryMng.getTopListForTag(web.getId());// 获得商品类根列表
		}
        
		if(list!=null){
			if(list.size()>0){
				result="01";
			 map.put("pd", getcategoryJson(list));	
			}else{
				result="02";
			}
		}else{
			result="02";
		}
		map.put("result", result);
	}
	
	
	private String getcategoryJson(List<Category> beanList) throws JSONException{
		JSONArray arr=new JSONArray();
		JSONObject o=new JSONObject();
		for(Category category:beanList){
			o.put("id", category.getId());
			o.put("name", category.getName());//分类名称
			o.put("pcImgUrl", category.getImagePath());//分类图片
			arr.put(o);
		}
		return arr.toString();
	}

	@Autowired
	private CategoryMng categoryMng;

}