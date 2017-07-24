package com.jspgou.cms.app;

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

import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;

@Controller
public class BrandAppAct {

	/**
	 * 品牌列表
	 * 
	 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 * 
	 */
	@RequestMapping(value = "/api/brandList.jspx")
	public void brandList(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Website web = SiteUtils.getWeb(request);
		String firstResult = request.getParameter("firsrResult");
		String maxResults = request.getParameter("maxResults");
		String callback=request.getParameter("callback");
		int first_result = 0, max_results = 0;
		String result = "00";
		if (StringUtils.isNotBlank(firstResult)) {
			first_result = Integer.parseInt(firstResult);
		}
		if (StringUtils.isNotBlank(maxResults)) {
			max_results = Integer.parseInt(maxResults);
		}
		List<Brand> list = brandMng.getListForTag(web.getId(), first_result,
				max_results);
		if (list != null) {
			if (list.size() > 0) {
				result = "01";
				map.put("pd", getBrandListJson(list));
			} else {
				result = "02";
			}
		} else {
			   result = "03";
		}
		map.put("result", result);
		if(!StringUtils.isBlank(callback)){
			ResponseUtils.renderJson(response, callback+"(" + ConvertMapToJson.buildJsonBody(map, 0, false) + ")");
		}else{
			ResponseUtils.renderJson(response, ConvertMapToJson.buildJsonBody(map, 0, false));
		}

	}

	private String getBrandListJson(List<Brand> beanList) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		for (Brand brand : beanList) {
			o.put("id", brand.getId());// 品牌id
			o.put("name", brand.getName());// 品牌名称
			o.put("pcImgUrl", brand.getLogoPath());// 名牌LOGO
		}

		return arr.toString();
	}

	@Autowired
	private BrandMng brandMng;
}