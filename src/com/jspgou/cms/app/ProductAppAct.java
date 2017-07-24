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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Product;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.util.Apputil;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.util.StrUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class ProductAppAct {

	/**
	 * 商品-接口
	 * 
	 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 */	
	@RequestMapping(value = "/api/product.jspx")
	public void product(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String callback = request.getParameter("callback");
		Product product = null;
		String result = "00";
		if (StringUtils.isNotBlank(productId)) {
			product = productMng.findById(Long.parseLong(productId));
		}
		if (product != null) {
			map.put("product", beantoJson(product));
			result = (null == product) ? "02" : "01";
		} else {
			result = "02";
		}
           map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			ResponseUtils.renderJson(response, callback + "("
					+ ConvertMapToJson.buildJsonBody(map, 0, false) + ")");
		} else {
			ResponseUtils.renderJson(response,
					ConvertMapToJson.buildJsonBody(map, 0, false));
		}
	}

	/**
	 * 商品详情-接口
	 * 
	 * 
	 */
	@RequestMapping("/api/producttext.jspx")
	public void producttext(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String callback = request.getParameter("callback");
		Product product = null;
		String result = "00";
		if (StringUtils.isNotBlank(productId)) {
			product = productMng.findById(Long.parseLong(productId));
		}
		if (product != null) {
			map.put("product", beantext(product));
			result = (null == result) ? "02" : "01";
		} else {

			result = "02";
		}
		map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			ResponseUtils.renderJson(response, callback + "("
					+ ConvertMapToJson.buildJsonBody(map, 0, false) + ")");
		} else {
			ResponseUtils.renderJson(response,
					ConvertMapToJson.buildJsonBody(map, 0, false));
		}

	}

	/**
	 * 商品参数-接口
	 */
	@RequestMapping("/api/producttext1.jspx")
	public void producttext1(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String callback = request.getParameter("callback");
		Product product = null;
		String result = "00";
		if (StringUtils.isNotBlank("productId")) {
			product = productMng.findById(Long.parseLong(productId));
		}
		if (product != null) {
			map.put("product", beantext1(product));
			result = (null == product) ? "02" : "01";
		} else {
			result = "02";
		}
		map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			ResponseUtils.renderJson(response, callback + "("
					+ ConvertMapToJson.buildJsonBody(map, 0, false) + ")");
		} else {
			ResponseUtils.renderJson(response,
					ConvertMapToJson.buildJsonBody(map, 0, false));
		}
	}

	private String beantoJson(Product product) {
		JSONObject o = new JSONObject();
		try {
			o.put("id", product.getId());
			o.put("name", product.getName());
			o.put("coverImg", product.getCoverImg());
			// 商品品牌
			if (product.getBrand() != null) {
				o.put("brandname", product.getBrandName());
				o.put("brandId", product.getBrandId());
			} else {
				o.put("brandname", "");
				o.put("brandId", "");
			}
			// 商品图片
			if (product.getPictures() != null) {
				o.put("picture", product.getPictures());
			} else {
				o.put("picture", "");
			}
			// 商品款式
			if (product.getFashions() != null) {
				o.put("salePrice", product.getProductFashion().getSalePrice());// 销售价
				o.put("marketPrice", product.getProductFashion()
						.getMarketPrice());// 市场价
				o.put("saleCount", product.getProductFashion().getSaleCount());// 销量
			} else {
				o.put("sale", "");
				o.put("marketPrice", "");
				o.put("saleCount", "");
			}

			o.put("code", product.getProductExt().getCode());// 商品编号

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return o.toString();
	}

	private String beantext(Product product) {
		JSONObject o = new JSONObject();

		try {
			o.put("id", product.getId());
			// o.put("text", product.getText());
			o.put("text", StrUtils.trimHtml2Txt(product.getText()));
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return o.toString();
	}

	private String beantext1(Product product) {
		JSONObject o = new JSONObject();
		try {
			o.put("id", product.getId());
			o.put("text1", StrUtils.trimHtml2Txt(product.getText1()));//商品详情
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return o.toString();

	}

	/**
	 * 商品列表-接口 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 */
	@RequestMapping(value = "/api/productlist.jspx")
	public void productlist(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Website site = SiteUtils.getWeb(request);
		String result = "00";
		Long ctgId = Apputil.getRequestLong(request.getParameter("ctgId"));//分类Id
		Long tagId = Apputil.getRequestLong(request.getParameter("tagId"));//商品标签Id
		Boolean isRecommend = Apputil.getRequestBoolean(request
				.getParameter("isRecommend"));// 是否推荐
		Boolean isSpecial = Apputil.getRequestBoolean(request
				.getParameter("isSpecial"));// 是否特价
		Boolean isHostSale = Apputil.getRequestBoolean(request
				.getParameter("isHostSale"));// 是否热卖
		Boolean isNewProduct = Apputil.getRequestBoolean(request
				.getParameter("isNewProduct"));// 是否新品
		Integer firstResult = Apputil.getfirstResult(request
				.getParameter("firsResult"));//第一条商品Id
		Integer maxResults = Apputil.getmaxResults(request
				.getParameter("maxResults"));//查询条数
		String callback = request.getParameter("callback");

		List<Product> list = productMng.getListForTag(site.getId(), ctgId,
				tagId, isRecommend, isSpecial, isHostSale, isNewProduct,
				firstResult, maxResults);
		if (list != null) {
			if (list.size() > 0) {
				result = "01";
				map.put("pd", getproductJson(list));
			} else {
				result = "02";
			}
		} else {
			result = "02";
		}
		map.put("result", result);
		if (!StringUtils.isBlank(callback)) {
			ResponseUtils.renderJson(response, callback + "("
					+ ConvertMapToJson.buildJsonBody(map, 0, false) + ")");
		} else {
			ResponseUtils.renderJson(response,
					ConvertMapToJson.buildJsonBody(map, 0, false));
		}
	}

	private String getproductJson(List<Product> beanlist) throws JSONException {
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Product product : beanlist) {
			o.put("id", product.getId());
			o.put("name", product.getName());
			o.put("coverImg", product.getCoverImg());
			arr.put(o);
		}
		return arr.toString();
	}

	@Autowired
	private ProductMng productMng;
}