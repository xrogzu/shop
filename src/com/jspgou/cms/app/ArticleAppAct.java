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

import com.jspgou.cms.entity.ShopArticle;
import com.jspgou.cms.manager.ShopArticleMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.util.StrUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class ArticleAppAct {

	/**
	 * 文章列表
	 * 
	 * 相关参数协议：
	 * 
	 * 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 */
	@RequestMapping(value = "/api/articleList.jspx")
	public void articleList(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Website web = SiteUtils.getWeb(request);
		String channelId = request.getParameter("channelId");//栏目Id
		String firstResult = request.getParameter("firstResult");//第一条
		String maxResults = request.getParameter("maxResult");//查询条数
		String callback = request.getParameter("callback");
		String result = "00";
		Long channel_id = null;
		int first_result = 0, max_result;
		if (StringUtils.isNotBlank(channelId)) {
			channel_id = Long.parseLong(channelId);
		}
		if (StringUtils.isNotBlank(firstResult)) {
			first_result = Integer.parseInt(firstResult);

		}
		if (StringUtils.isNotBlank(maxResults)) {
			max_result = Integer.parseInt(maxResults);
		}
		List<ShopArticle> list = shopArticleMng.getListForTag(web.getId(),
				Long.parseLong(channelId), Integer.parseInt(firstResult),
				Integer.parseInt(maxResults));
		if (list != null) {
			if (list.size() > 0) {
				result = "01";
				map.put("pd", getarticleJson(list));
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

	private String getarticleJson(List<ShopArticle> beanList)
			throws JSONException {
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		for (ShopArticle article : beanList) {
			o.put("id", article.getId());
			o.put("title", article.getTitle());// 标题
			o.put("time", article.getPublishTime());// 发布时间
			o.put("content", StrUtils.trimHtml2Txt(article.getArticleContent().getContent()));// 内容
			arr.put(o);
		}
		return arr.toString();

	}

	/**
	 * 文章-接口 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 */
	public void article(HttpServletRequest request, HttpServletResponse response) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String articleId = request.getParameter("articleId");
		String result = "00";
		if (StringUtils.isNotBlank(articleId)) {
			ShopArticle article = shopArticleMng.findById(Long
					.parseLong(articleId));
			if (article != null) {
				result = "01";
				map.put("pd", getArticleJson(article));
			} else {
				result = "02";
			}
		} else {
			result = "02";
		}
		map.put("result", result);
	}
	
	private String getArticleJson(ShopArticle article) throws JSONException{
		JSONObject o=new JSONObject();
		o.put("id", article.getId());
		o.put("title", article.getTitle());
		o.put("time", article.getPublishTime());
		o.put("content", StrUtils.trimHtml2Txt(article.getArticleContent().getContent()));
		return o.toString();
		
	}

	@Autowired
	private ShopArticleMng shopArticleMng;

}