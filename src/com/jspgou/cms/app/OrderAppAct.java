package com.jspgou.cms.app;
import java.util.Date;
import java.util.Enumeration;
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

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.entity.ApiRecord;
import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.PaymentPlugins;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ApiAccountMng;
import com.jspgou.cms.manager.ApiRecordMng;
import com.jspgou.cms.manager.ApiUtilMng;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.service.ShoppingSvc;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.util.AlipayAPIClientFactory;
import com.jspgou.common.util.Apputil;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.util.PayUtil;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class OrderAppAct {

	/**
	 * 生成订单-接口
	 * 
	 * 
	 */
	@RequestMapping(value = "/api/createorders.jspx")
	public void createorders(HttpServletRequest request,
			HttpServletResponse response) {
		Website web = SiteUtils.getWeb(request);
		ShopMember user = apiUtilMng.findbysessionKey(request);
		String cartItemId = request.getParameter("cartItemId");//选择购物车项列表
		String shippingMethodId = request.getParameter("shippingMethodId");
		String deliveryInfo = request.getParameter("deliveryInfo");//收货人信息
		String paymentMethodId = request.getParameter("paymentMethodId");//支付方式
		String comments = request.getParameter("comments");//订单备注
		String memberCouponId = request.getParameter("memberCouponId");//用户优惠券
		Long[] cartItemIds = null;
		Long shippingMethodIds = Long.parseLong(shippingMethodId);
		Long deliveryInfos = Long.parseLong(deliveryInfo);
		Long paymentMethodIds = Long.parseLong(paymentMethodId);
		if (StringUtils.isNotBlank(cartItemId)) {
			String[] cids = cartItemId.split(",");
			cartItemIds = new Long[cids.length];
			for (int i = 0; i < cids.length; i++) {
				cartItemIds[i] = Long.parseLong(cids[i]);
			}

		}

		boolean createOrder = true;
		if (user != null) {
			if (createOrder == true) {
				Order order = null;
				Cart cart = shoppingSvc.getCart(user.getId());
				if (cart != null) {
					order = orderMng.createOrder(cart, cartItemIds,
							shippingMethodIds, deliveryInfos, paymentMethodIds,
							comments, request.getRemoteAddr(), user,
							web.getId(), memberCouponId);
				}
			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(null,
				"/api/createorders.jspx", request));
	}

	/**
	 * 订单列表—接口
	 * 
	 * @throws JSONException
	 * 
	 */
	@RequestMapping(value = "/api/myorders.jspx")
	public void myorders(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Website web = SiteUtils.getWeb(request);
		ShopMember user = apiUtilMng.findbysessionKey(request);
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		if (user != null) {
			String userName = request.getParameter("username");//用户名称
			Long code = Apputil.getRequestLong(request.getParameter("code"));// 订单编号
			String productName = request.getParameter("productName");// 商品名称
			Long paymentId = Apputil.getRequestLong(request
					.getParameter("paymentId"));// 支付方式
			Long shippingId = Apputil.getRequestLong(request
					.getParameter("shippingId"));// 物流方式
			Integer status = Apputil.getRequestInteger(request
					.getParameter("status"));// 订单状态
			Date startTime = Apputil.getRequestDate(request
					.getParameter("startTime"));
			Date endTime = Apputil.getRequestDate(request
					.getParameter("endTime"));
			Double startOrderTotal = Apputil.getRequestDouble(request
					.getParameter("startOrderTotal"));// 订单金额
			Double endOrderTotal = Apputil.getRequestDouble(request
					.getParameter("endOrderTotal"));
			Integer firstResult = Apputil.getfirstResult(request
					.getParameter("firstResult"));
			Integer maxResults = Apputil.getmaxResults(request
					.getParameter("maxResults"));

			List<Order> list = orderMng.getOrderList(web.getId(), user.getId(),
					productName, userName, paymentId, shippingId, startTime,
					endTime, startOrderTotal, endOrderTotal, status, code,
					firstResult, maxResults);
			for (Order order : list) {
				o.put("id", order.getId());
				o.put("code", order.getCode());// 订单编号
				o.put("productName", order.getProductName());// 订单商品名字的组合
				o.put("productPrice", order.getProductPrice());// 商品单价
				o.put("freight", order.getFreight());// 运费
				o.put("createTime", order.getCreateTime());// 订单时间
				o.put("shippingStatus", order.getShippingStatus());// 配送状态
				o.put("paymentStatus", order.getPaymentStatus());// 支付状态
				o.put("paymentId", order.getPayment().getId());// 支付方式
				o.put("total", order.getTotal());// 订单总额
				arr.put(o);
			}

		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				arr.toString(), "/api/myorders.jspx", request));

	}

	/**
	 * 订单购买-接口
	 * 
	 * @param id
	 *            订单ID 必选
	 * @param outOrderNum
	 *            外部订单号 必选
	 * @param orderType
	 *            1微信支付 2支付宝支付 必选
	 * @param appId
	 *            appid 必选
	 * @param nonce_str
	 *            随机字符串 必选
	 * @param sign
	 *            签名 必选
	 * @param sessionKey
	 *            会话标识 必选
	 */
	@RequestMapping(value = "/api/user/buyorder.jspx")
	public void buyorder(Long id, String appId, Integer orderType,
			String outOrderNum, String sign, String sessionKey,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "00";
		Website site = SiteUtils.getWeb(request);
		String callback = request.getParameter("callback");
		if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(sign)
				&& StringUtils.isNotBlank(sessionKey)) {
			// 签名数据是否重复
			ApiRecord record = apiRecordMng.findBySign(sign, appId);
			if (record != null) {
				result = "04";
			} else {
				ApiAccount apiAccount = apiAccountMng.findByAppId(appId);
				// 验证签名
				String validateSign = getValidateSign(apiAccount, request);
				if (sign.equals(validateSign)) {
					result = "01";
					contentOrder(id, orderType, outOrderNum);
				} else {
					result = "03";
				}
			}

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

	private void contentOrder(Long orderId, Integer orderType,
			String outOrderNum) {
		Double orderAmount = 0d;
		if (orderId != null) {
			Order order = orderMng.findById(orderId);
			if (order != null) {
				// Payment payment = order.getPayment();
				PaymentPlugins payment = new PaymentPlugins();
				orderAmount = getAlipayMobileOrderAmount(outOrderNum, payment);
			}
			if (orderAmount.equals(0d)) {
				order.setPaymentStatus(2);
				order.setTradeNo(outOrderNum);
				orderMng.updateByUpdater(order);
			}
		}

	}

	private Double getAlipayMobileOrderAmount(String outOrderNum,
			PaymentPlugins payment) {
		AlipayTradeQueryResponse res = query(
				"https://openapi.alipay.com/gateway.do", payment, null,
				outOrderNum);
		Double orderAmount = 0d;
		if (res.isSuccess() && res != null) {
			if (res.getCode().equals("10000")) {
				if ("TRADE_SUCCESS".equalsIgnoreCase(res.getTradeStatus())) {
					String totalAmout = res.getTotalAmount();
					if (StringUtils.isNotBlank(totalAmout)) {
						orderAmount = Double.parseDouble(totalAmout);
					}
				}
			}
		}
		return orderAmount;

	}

	/**
	 * 交易查询
	 * 
	 * @param out_trade_no
	 *            业务系统订单号
	 * @param trade_no
	 *            支付宝交易订单号
	 */
	public AlipayTradeQueryResponse query(String serverUrl,
			PaymentPlugins payment, final String out_trade_no,
			final String trade_no) {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
				serverUrl, payment.getPublicKey(), payment.getSellerKey(),
				payment.getSellerEmail(), "UTF-8");
		AlipayTradeQueryRequest alipayQueryRequest = new AlipayTradeQueryRequest();
		String biz_content = "{" +
		// 商户订单号
				"    \"out_trade_no\":\"" + out_trade_no + "\"," +
				// 支付宝交易订单号
				"    \"trade_no\":\"" + trade_no + "\"" + "  }";

		alipayQueryRequest.setBizContent(biz_content);
		AlipayTradeQueryResponse alipayQueryResponse = null;
		try {
			alipayQueryResponse = alipayClient.execute(alipayQueryRequest);

			if (null != alipayQueryResponse && alipayQueryResponse.isSuccess()) {
				if (alipayQueryResponse.getCode().equals("10000")) {
					if ("TRADE_SUCCESS".equalsIgnoreCase(alipayQueryResponse
							.getTradeStatus())) {

						List<TradeFundBill> fund_bill_list = alipayQueryResponse
								.getFundBillList();
						if (null != fund_bill_list) {
							doFundBillList(out_trade_no, fund_bill_list);
						}
					} else if ("TRADE_CLOSED"
							.equalsIgnoreCase(alipayQueryResponse
									.getTradeStatus())) {
						// 表示未付款关闭，或已付款的订单全额退款后关闭
					} else if ("TRADE_FINISHED"
							.equalsIgnoreCase(alipayQueryResponse
									.getTradeStatus())) {
						// 此状态，订单不可退款或撤销
					}
				} else {
					// 如果请求未成功，请重试

				}
			}
		} catch (AlipayApiException e) {

			e.printStackTrace();
		}

		return alipayQueryResponse;
	}

	public static void doFundBillList(String out_trade_no,
			List<TradeFundBill> fund_bill_list) {
		// 根据付款的资金渠道，来决定哪些是商户优惠，哪些是支付宝优惠。 对账时要注意商户优惠部分
		for (TradeFundBill tfb : fund_bill_list) {
			System.out.println("付款资金渠道：" + tfb.getFundChannel() + " 付款金额："
					+ tfb.getAmount());
		}
	}

	private String getValidateSign(ApiAccount apiAccount,
			HttpServletRequest request) {
		String appKey = apiAccount.getAppKey();
		Enumeration penum = request.getParameterNames();
		Map<String, String> param = new HashMap<String, String>();
		String validateSign;
		while (penum.hasMoreElements()) {
			String pKey = (String) penum.nextElement();
			String value = request.getParameter(pKey);
			// sign不参与 值为空也不参与
			if (StringUtils.isNotBlank(value) && !pKey.equals("sign")) {
				param.put(pKey, value);
			}
		}
		// 先验证签名
		validateSign = PayUtil.createSign(param, appKey);
		return validateSign;

	}

	@Autowired
	private ApiUtilMng apiUtilMng;
	@Autowired
	private ShoppingSvc shoppingSvc;
	@Autowired
	private OrderMng orderMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private ApiAccountMng apiAccountMng;

}