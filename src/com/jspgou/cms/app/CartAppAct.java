package com.jspgou.cms.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.CartItem;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberAddress;
import com.jspgou.cms.manager.AddressMng;
import com.jspgou.cms.manager.ApiUtilMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ShopMemberAddressMng;
import com.jspgou.cms.service.ShoppingSvc;
import com.jspgou.common.util.Apputil;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class CartAppAct {

	/**
	 * 用户购物车列表查询——接口
	 * 
	 * 商品图片 商品名称 原价格 实际价格 数量
	 * 
	 * coverImg name marketPrice costPrice count
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/api/user/cart.jspx")
	public void shoppingCart(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		ShopMember member = apiUtilMng.findbysessionKey(request);
		if (member != null) {
			Cart cart = shoppingSvc.getCart(member, request, response);
			for (CartItem item : cart.getItems()) {
				o.put("name", item.getProduct().getName());// 商品名称
				o.put("imgUrl", item.getProduct().getCoverImgUrl());//商品图片
				o.put("marketPrice", item.getProduct().getMarketPrice());// 市场价
				o.put("costPrice", item.getProduct().getCostPrice());// 成本价
				o.put("onSale", item.getProduct().getOnSale());// 是否上架
				arr.put(o);
			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				arr.toString(), "/api/user/cart.jspx", request));
	}

	/**
	 * 加入购物车接口 * 相关参数协议： 00 请求失败 01 请求成功 02 请求协议参数不完整 03 sign验证失败 04 签名数据不可重复利用
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/api/user/addcarttem.jspx")
	public void addcarttem(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		JSONObject json = new JSONObject();
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember user = apiUtilMng.findbysessionKey(request);
		if (user != null) {
			Long productId = Apputil.getRequestLong(request
					.getParameter("productId"));
			Integer productAmount = Apputil.getRequestInteger("productAmount");// 数量
			Long fashId = Apputil.getRequestLong("fashId");// 商品款式
			Product product = productMng.findById(productId);
			// 商品上架，直接跳转到商品页面
			if (product.getOnSale()) {
				if (fashId == null) {
					if (productAmount > product.getStockCount()) { // 数量大于库存
						json.put("status", 2);
					} else {
						shoppingSvc.collectAddToCart(product, fashId, null,
								productAmount, true, user, web, request,
								response);
						json.put("status", 1);
					}
				} else {
					ProductFashion productFashion = productFashionMng
							.findById(fashId);
					if (productAmount > product.getStockCount()) {
						json.put("status", 2);
					} else {
						shoppingSvc.collectAddToCart(product, fashId, null,
								productAmount, true, user, web, request,
								response);
						json.put("status", 1);
					}

				}
				// 商品下架
			} else if (!product.getOnSale()) {
				json.put("status", 3);
			}

		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				json.toString(), "/api/user/cart.jspx", request));
	}

	/**
	 * 用户收货地址列表
	 * 
	 * 相关参数协议： 00 请求失败 01 请求成功 02 请求协议参数不完整
	 */

	@RequestMapping(value = "/api/user/addresslist.jspx")
	public void addresslist(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		ShopMember user = apiUtilMng.findbysessionKey(request);
		if (user != null) {
			List<ShopMemberAddress> list = shopMemberAddressMng.getList(user
					.getId());
			for (ShopMemberAddress address : list) {
				o.put("username", address.getUsername());
				o.put("detailaddress",address.getProvince()+address.getCity()+address.getCountry()+address.getDetailaddress());
				o.put("tel", address.getTel());
				arr.put(o);

			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				arr.toString(), "/api/user/addresslist.jspx", request));
	}

	/**
	 * 默认收货地址
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/api/user/addressdefault.jspx")
	public void addressdefault(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		ShopMember user = apiUtilMng.findbysessionKey(request);
		JSONObject o = new JSONObject();
		if (user != null) {
			ShopMemberAddress address = shopMemberAddressMng
					.findByMemberDefault(user.getId(), true);
			o.put("username", address.getUsername());
			o.put("tel", address.getTel());
			o.put("detailaddress",address.getProvince()+address.getCity()+address.getCountry()+address.getDetailaddress());


		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				o.toString(), "/api/user/addressdefault.jspx", request));

	}

	/**
	 * 用户保存收货地址
	 */
	@RequestMapping(value = "/api/user/addresssave.jspx")
	public void addressSave(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject o =new JSONObject();
		ShopMember user = apiUtilMng.findbysessionKey(request);
		String username = request.getParameter("username");// 收货人
		String detailaddress = request.getParameter("detailaddress");// 详细地址
		String tel = request.getParameter("tel");// 手机号码
		String postCode = request.getParameter("postCode");// 邮编
		String provinceId = request.getParameter("provinceId");// 省份Id
		String cityId = request.getParameter("cityId");// 城市Id
		String countryId = request.getParameter("countryId");// 县级Id

		if (user != null) {
			ShopMemberAddress bean = new ShopMemberAddress();
			bean.setUsername(username);
			bean.setProvinceId(addressMng.findById(Long.parseLong(provinceId))
					.getId());
			bean.setProvince(addressMng.findById(Long.parseLong(provinceId))
					.getName());
			bean.setCity(addressMng.findById(Long.parseLong(cityId)).getName());
			bean.setCityId(addressMng.findById(Long.parseLong(cityId)).getId());
			bean.setCountry(addressMng.findById(Long.parseLong(countryId))
					.getName());
			bean.setCountryId(addressMng.findById(Long.parseLong(countryId))
					.getId());
			bean.setDetailaddress(detailaddress);
			bean.setTel(tel);
			bean.setPostCode(postCode);
			shopMemberAddressMng.save(bean);
		}
        ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(o.toString(),"/api/user/addresssave.jspx", request));
	}

	@Autowired
	private ApiUtilMng apiUtilMng;
	@Autowired
	private ShoppingSvc shoppingSvc;
	@Autowired
	private ProductMng productMng;
	@Autowired
	private ProductFashionMng productFashionMng;
	@Autowired
	private ShopMemberAddressMng shopMemberAddressMng;
	@Autowired
	private AddressMng addressMng;

}