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

import com.jspgou.cms.entity.PaymentPlugins;
import com.jspgou.cms.manager.PaymentPluginsMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class PaymentAppAct {

	/**
	 * 支付方式列表-接口 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整
	 * 
	 */
	@RequestMapping(value = "/api/paymentList.jspx")
	public void paymentList(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Website web = SiteUtils.getWeb(request);
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "00";
		String callback = request.getParameter("callback");
		List<PaymentPlugins> paylist = paymentMng.getList1("mobile");
		if (paylist != null) {
			if (paylist.size() > 0) {
				result = "01";
				map.put("pd", paymentListJson(paylist));
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

	private String paymentListJson(List<PaymentPlugins> paylist)
			throws JSONException {
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		for (PaymentPlugins payment : paylist) {
			o.put("id", payment.getId());
			o.put("name", payment.getName());
			o.put("imgPath", payment.getImgPath());
			arr.put(o);
		}
		return arr.toString();
	}

	@Autowired
	private PaymentPluginsMng paymentMng;
}