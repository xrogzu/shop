package com.jspgou.cms.app;

import static com.jspgou.cms.Constants.LUCENE_JSPGOU;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.KeyWord;
import com.jspgou.cms.lucene.LuceneProduct;
import com.jspgou.cms.lucene.LuceneProductSvc;
import com.jspgou.cms.manager.KeyWordMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.util.Apputil;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

public class SearchAppAct {

	/**
	 * 关键词检索-接口 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws JSONException
	 * 
	 * 
	 */
	@RequestMapping(value = "/api/search.jspx")
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException, JSONException {
		Website web = SiteUtils.getWeb(request);
		String result = "00";
		Map<String, Object> map = new HashMap<String, Object>();
		String query = request.getParameter("query");//关键字
		Long brandId = Apputil.getRequestLong(request.getParameter("brandId"));//品牌Id
		Long ctgId = Apputil.getRequestLong(request.getParameter("ctgId"));//分类Id
		Date start = Apputil.getRequestDate(request.getParameter("StartDate"));//开始时间
		Date end = Apputil.getRequestDate(request.getParameter("EndDate"));//结束时间
		Integer orderBy = Apputil.getRequestInteger(request
				.getParameter("orderBy"));//排序顺序
		Integer firstResult = Apputil.getfirstResult(request
				.getParameter("firstResult"));//第一条
		Integer maxResults = Apputil.getmaxResults(request
				.getParameter("maxResults"));//最后一条
		String path = servletContext.getRealPath(LUCENE_JSPGOU);
		List<LuceneProduct> list = luceneProductSvc.getlist(path, query,
				web.getId(), ctgId, brandId, start, end, orderBy, firstResult,
				maxResults);
		if (list != null) {
			if (list.size() > 0) {
				result = "01";
				map.put("pd", getLuceneProductJson(list));
			} else {
				result = "02";
			}

		} else {
			result = "02";
		}
		map.put("result", result);
	}

	private String getLuceneProductJson(List<LuceneProduct> beanList)
			throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		for (LuceneProduct product : beanList) {
			o.put("id", product.getId());
			o.put("coverImgUrl", product.getCoverImgUrl());// 商品图片
			o.put("name", product.getName());// 商品名称
			o.put("salePrice", product.getSalePrice());// 商品销售价格
			o.put("marketPrice", product.getMarketPrice());//商品的市场价格
			o.put("brandId", product.getBrandId());// 品牌Id
			o.put("brandName", product.getBrandName());//商品品牌名称
			arr.put(o);

		}
		return arr.toString();
	}

	/**
	 * 热门关键字-接口
	 * 
	 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 */
	@RequestMapping(value = "/api/keyword.jspx")
	public void keyword(HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "00";
		String callback = request.getParameter("callback");
		Integer count = Apputil
				.getRequestInteger(request.getParameter("count"));//显示个数
		List<KeyWord> beanList = keywordMng.findKeyWord(count);
		if (beanList != null) {
			if (beanList.size() > 0) {
				result = "01";
				map.put("pd", getkeywordJson(beanList));
			} else {
				result = "02";
			}
		} else {
			result = "02";
		}
		if (StringUtils.isBlank(callback)) {
			ResponseUtils.renderJson(response, callback + "("
					+ ConvertMapToJson.buildJsonBody(map, 0, false) + ")");

		} else {
			ResponseUtils.renderJson(response,
					ConvertMapToJson.buildJsonBody(map, 0, false));
		}
	}

	private String getkeywordJson(List<KeyWord> beanList) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		for (KeyWord keyWord : beanList) {
			o.put("keyword", keyWord.getKeyword());// 关键字
			arr.put(o);

		}
		return arr.toString();

	}

	@Autowired
	private ServletContext servletContext;
	@Autowired
	private LuceneProductSvc luceneProductSvc;
	@Autowired
	private KeyWordMng keywordMng;

}