package com.jspgou.cms.app;

import java.util.Date;
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
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.entity.ApiUserLogin;
import com.jspgou.cms.entity.Collect;
import com.jspgou.cms.entity.Coupon;
import com.jspgou.cms.entity.MemberCoupon;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ApiAccountMng;
import com.jspgou.cms.manager.ApiUserLoginMng;
import com.jspgou.cms.manager.ApiUtilMng;
import com.jspgou.cms.manager.CollectMng;
import com.jspgou.cms.manager.CouponMng;
import com.jspgou.cms.manager.MemberCouponMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.common.util.AES128Util;
import com.jspgou.common.util.Apputil;
import com.jspgou.common.util.ConvertMapToJson;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.session.SessionProvider;
import com.jspgou.core.entity.User;
import com.jspgou.core.manager.UserMng;

@Controller
public class UserAppAct {

	/**
	 * 用户登录-接口 相关参数协议： 00 请求协议参数不完整 01 未找到该用户 02 密码错误 03 登陆成功
	 * 
	 */
	@RequestMapping("/api/user/login.jspx")
	public void userLogin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		JSONObject o = new JSONObject();
		String username = request.getParameter("username");
		String appId = request.getParameter("appId");
		String encryptPass = null;
		Boolean decryptionFailure = true;
		encryptPass = apiUtilMng.getEncryptpass(request);
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(appId)
				&& StringUtils.isNotBlank(encryptPass)) {
			User user = userMng.register(username, null, null, null);
			ApiAccount apiAccount = apiAccountMng.findByAppId(appId);
			if (user != null) {
				// 验证用户密码
				if (userMng.isPasswordValid(user.getId(), encryptPass)) {
					String sessionKey = session.getSessionId(request, response);
					ApiUserLogin apiUserLogin = apiUserLoginMng
							.findByUsername(username);
					if (apiUserLogin != null) {
						apiUserLoginMng
								.updateLoginSuccess(username, sessionKey);
					} else {
						apiUserLoginMng.saveLoginSuccess(username, sessionKey);
					}
					String aesKey = apiAccount.getAesKey();
					String ivVal = apiAccount.getIvKey();
					o.put("message", "03");
					o.put("sessionKey",
							AES128Util.encrypt(sessionKey, aesKey, ivVal));

				} else {
					o.put("mesage", "02");
				}

			} else {
				o.put("message", "01");
			}
		} else {
			o.put("message", "00");
		}

		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				o.toString(), "/api/user/login.jspx", decryptionFailure,
				request));

	}

	/**
	 * 检查用户名是否唯一接口 相关参数协议： 00 请求失败 01 请求成功 02 请求协议参数不完整 pd 用户名存在返回false，否则true
	 */
	@RequestMapping(value = "/api/user/username_unique.jspx")
	public void checkUsername(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String callback = request.getParameter("callback");
		String result = "00";
		if (StringUtils.isBlank(username)) {
			result = "02";
		} else {
			result = "01";
			if (userMng.usernameExist(username)) {
				map.put("pd", "false");
			} else {
				map.put("pd", "true");
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

	/**
	 * 用户注册-接口
	 * 
	 * 相关参数协议： 00 请求协议参数不完整 01 用户邮箱已存在 02 注册成功
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/api/user/register.jspx")
	public void register(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		JSONObject o = new JSONObject();
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (apiUtilMng.verify(request)) {
			if (StringUtils.isNotBlank(username)
					&& StringUtils.isNotBlank(password)
					&& StringUtils.isNotBlank(email)) {
				if (!userMng.usernameExist(username)
						&& !userMng.emailExist(email)) {
					User user = userMng.register(username, password, email,
							RequestUtils.getIpAddr(request));
					o.put("message", "02");
					o.put("userId", user.getId());
				} else {
					o.put("message", "01");
				}
			} else {
				o.put("message", "00");
			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				o.toString(), "/api/user/register.jspx", request));

	}

	/**
	 * 用户退出接口 相关参数协议： 00 退出失败 01 退出成功
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/api/user/logout.jspx")
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		JSONObject o = new JSONObject();
		String sessionKey = request.getParameter("sessionKey");
		ShopMember user = apiUtilMng.findbysessionKey(request);
		if (user != null) {
			ApiUserLogin apiUserLogin = apiUserLoginMng
					.findBySessionKey(sessionKey);
			if (apiUserLogin != null) {
				apiUserLoginMng.deleteById(apiUserLogin.getId());
				o.put("message", "01");
			} else {
				o.put("message", "00");
			}
		} else {
			o.put("message", "00");
		}

		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				o.toString(), "/api/user/logout.jspx", request));

	}

	/**
	 * 用户个人资料 username用户名 email邮箱 realName 真实姓名 mobile 手机
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/api/user/profile.jspx")
	public void profile(HttpServletRequest request,
			HttpServletResponse response, ModelMap madel) throws JSONException {

		ShopMember user = apiUtilMng.findbysessionKey(request);
		JSONObject o = new JSONObject();
		if (user != null) {
			Boolean protocol = Apputil.getRequestBoolean(request
					.getParameter("protocol"));
			o.put("id", user.getId());

			if (user.getGroup() != null) {
				o.put("groupId", user.getGroup().getId());
				o.put("groupName", user.getGroup().getName());
			}
			o.put("username", user.getUsername());// 用户名
			o.put("email", user.getEmail()); // 邮箱
			o.put("realName", user.getRealName()); // 真实姓名
			o.put("gender", user.getGender()); // 性别
			o.put("birthday", user.getBirthday());// 生日
			o.put("score", user.getScore());// 积分
			o.put("money", user.getMoney());// 账户余额
			o.put("company", user.getCompany());// 公司
			o.put("marriage", user.getMarriage());// 婚姻状况
			o.put("position", user.getPosition());// 职位
			o.put("address", user.getAddress());// 地址
			o.put("mobile", user.getMobile());// 手机
			o.put("tel", user.getTel());// 电话
			o.put("avatar", user.getAvatar());// 会员头像
			o.put("lastLoginTime", user.getLastLoginTime());// 最后登录时间
			o.put("LoginCount", user.getLoginCount());// 登录次数
			o.put("schoolTag", user.getSchoolTag());// 毕业学校
			o.put("schoolTagDate", user.getSchoolTagDate());// 毕业时间
			o.put("favoriteBrand", user.getFavoriteBrand());// 最喜爱的品牌
			o.put("favoriteStar", user.getFavoriteStar());// 最喜爱的明星
			o.put("favoriteMovie", user.getFavoriteMovie());// 最喜爱的电影
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				o.toString(), "/api/user/profile.jspx", request));

	}

	/**
	 * 用户个人资料修改
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "/api/user/profileupdate.jspx")
	public void profileupdate(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		ShopMember user = apiUtilMng.findbysessionKey(request);
		String message = "00";
		if (user != null) {
			String realName = request.getParameter("realName");// realName 真实姓名
			String mobile = request.getParameter("mobile"); // mobile 手机
			String tel = request.getParameter("tel"); // tel电话
			String gender = request.getParameter("gender");// 性别
			Date birthday = Apputil.getRequestDate(request
					.getParameter("birthday"));// 生日
			String address = request.getParameter("address");// 地址
			String marriage = request.getParameter("marriage");// 婚姻状况
			String company = request.getParameter("company");// 单位名称
			String position = request.getParameter("position");// 职位
			String avatar = request.getParameter("avater");// 会员头像
			String schoolTag = request.getParameter("schoolTag");// 毕业学校
			Date schoolTagDate = Apputil.getRequestDate(request
					.getParameter("schoolTagDate"));// 毕业时间
			String favoriteBrand = request.getParameter("favoriteBrand");// 最喜爱的品牌
			String favoriteStar = request.getParameter("favoriteStar");// 最喜爱的明星
			String favoriteMovie = request.getParameter("favoriteMovie");// 最喜爱的电影
			if (StringUtils.isNotBlank(realName)) {
				user.setRealName(realName);
			}
			if (StringUtils.isNotBlank(mobile)) {
				user.setMobile(mobile);
			}
			if (StringUtils.isNotBlank(tel)) {
				user.setTel(tel);
			}
			if (StringUtils.isNotBlank(address)) {
				user.setAddress(address);
			}
			if (StringUtils.isNotBlank(marriage)) {
				user.setMarriage(Boolean.parseBoolean(marriage));
			}
			if (StringUtils.isNotBlank(gender)) {
				user.setGender(Boolean.parseBoolean(gender));
			}
			if (birthday != null) {
				user.setBirthday(birthday);
			}
			if (StringUtils.isNotBlank(company)) {
				user.setCompany(company);
			}
			if (StringUtils.isNotBlank(position)) {
				user.setPosition(position);
			}
			if (StringUtils.isNotBlank(avatar)) {
				user.setAvatar(avatar);
			}
			if (StringUtils.isNotBlank(schoolTag)) {
				user.setSchoolTag(schoolTag);
			}
			if (schoolTagDate != null) {
				user.setSchoolTagDate(schoolTagDate);
			}
			if (StringUtils.isNotBlank(favoriteBrand)) {
				user.setFavoriteBrand(favoriteBrand);
			}
			if (StringUtils.isNotBlank(favoriteStar)) {
				user.setFavoriteStar(favoriteStar);
			}
			if (StringUtils.isNotBlank(favoriteMovie)) {
				user.setFavoriteMovie(favoriteMovie);
			}
			shopMemberMng.update(user);
			message = "01";
		}

		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(message,
				"/api/user/profileupdate.jspx", request));

	}

	/**
	 * 用户密码修改
	 * 
	 * 相关参数协议： 00 请求失败 01 请求成功 02 请求协议参数不完整 03 sign验证失败 04 参数异常
	 */
	@RequestMapping(value = "/api/user/pwd.jspx")
	public void pwd(HttpServletRequest request, HttpServletResponse response) {
		ShopMember user = apiUtilMng.findbysessionKey(request);
		String result = "00";
		if (user != null) {
			String origPwd = request.getParameter("origPwd");
			String newPwd = request.getParameter("newPwd");
			String email = request.getParameter("email");
			if (StringUtils.isNotBlank(newPwd) && StringUtils.isNotBlank(email)) {
				userMng.updateUser(user.getId(), newPwd, email);
				result = "01";

			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(result,
				"/api/user/profileupdate.jspx", request));

	}

	/**
	 * 用户收藏夹列表
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/api/user/collectList.jspx")
	public void collectList(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject o = new JSONObject();
		ShopMember user = apiUtilMng.findbysessionKey(request);
		Integer firstResult = Apputil.getfirstResult(request
				.getParameter("firstResult"));
		Integer maxResults = Apputil.getmaxResults(request
				.getParameter("maxResults"));
		if (user != null) {
			List<Collect> list = collectMng.getList(user.getId(), firstResult,
					maxResults);
			for (Collect collect : list) {
				o.put("id", collect.getId());// 收藏id
				if (collect.getFashion() != null) {
					o.put("fashionId", collect.getFashion().getId());// 款式Id
				}
				o.put("productCoverImg", collect.getProduct().getProductExt()
						.getCode());// 商品图片
				o.put("productName", collect.getProduct().getName());// 商品名称
				o.put("productSalePrice", collect.getProduct().getSalePrice());// 商品价格
				o.put("productId", collect.getProduct().getId());// 商品Id
				o.put("time", collect.getTime());// 时间
				o.put("userId", user.getId());// 用户Id
				arr.put(o);
			}
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				arr.toString(), "/api/user/collectList.jspx", request));
	}

	/**
	 * 加入收藏夹
	 * 
	 * 00 请求失败 01 请求成功
	 */
	@RequestMapping(value = "/api/user/add_to_collect.jspx")
	public void addToCollect(Long productId, Long productFashId,
			HttpServletRequest request, HttpServletResponse response) {
		ShopMember user = apiUtilMng.findbysessionKey(request);
		Integer pd = 0;
		if (user != null && productId != null) {
			Collect collect = null;
			if (productFashId == null) {
				List<Collect> clist = collectMng.getList(productId,
						user.getId());
				if (clist.size() > 0) {
					collect = collectMng.save(user, productId, null);
				}
			} else {
				collect = collectMng.findByProductFashionId(productFashId);
				if (collect == null) {
					collect = collectMng.save(user, productId, productFashId);
				}
			}
			pd = 1;
		} else {
			pd = 0;
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				pd.toString(), "api/user/add_to_collect.jspx", request));
	}

	/**
	 * 领取用户券 00 请求失败 01 请求成功 02 请求协议参数不完整
	 */
	@RequestMapping(value = "/api/user/getcoupon.jspx")
	public void coupon(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject o = new JSONObject();
		Date newdate = new Date();
		Integer pd = 0;
		if (id != null) {
			Coupon coupon = couponMng.findById(id);
			ShopMember user = apiUtilMng.findbysessionKey(request);
			if (coupon != null && user != null) {
				if (memberCouponMng.findByCoupon(user.getId(), id) != null
						&& newdate.before(couponMng.findById(id)
								.getCouponEndTime())) {
					MemberCoupon memberCoupon = new MemberCoupon();
					memberCoupon.setCoupon(coupon);
					memberCoupon.setMember(user);
					memberCoupon.setIsuse(false);
					coupon.setCouponCount(coupon.getCouponCount() - 1);
					memberCouponMng.save(memberCoupon);
					couponMng.update(coupon);

					pd = 1;

				} else {

					pd = 2;
				}

			} else {
				pd = 0;
			}

		} else {

			pd = 0;
		}

		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				pd.toString(), "/api/user/getcoupon.jspx", request));

	}

	/**
	 * 用户优惠券列表
	 * 
	 * 00 请求失败 01 请求成功 02 请求协议参数不完整
	 */
	@RequestMapping(value = "api/user/myCouponList.jspx")
	public void myCouponList(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		Map<String, Object> map = new HashMap<>();
		String result = "00";
		Integer pd = 0;
		ShopMember user = apiUtilMng.findbysessionKey(request);
		if (user != null) {
			List<MemberCoupon> list = memberCouponMng.getList(user.getId());
			if (list != null) {
				if (list.size() > 0) {
					
					map.put("pd", getMyCouponJson(list));
				}
			} 
		}
		ResponseUtils.renderJson(response, apiUtilMng.getJsonStrng(
				pd.toString(), "/api/user/myCouponList.jspx", request));
	}

	private String getMyCouponJson(List<MemberCoupon> beanlist)
			throws JSONException {
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		for (MemberCoupon coupon : beanlist) {
			o.put("id", coupon.getId());
			o.put("name", coupon.getCoupon().getCouponName());// 优惠券名称
			o.put("couponPrice", coupon.getCoupon().getCouponPrice());// 优惠券值
			o.put("leastPrice", coupon.getCoupon().getLeastPrice());// 最低消费
			o.put("couponBeginTime", coupon.getCoupon().getCouponTime());// 起始时间
			o.put("couponEndTime", coupon.getCoupon().getCouponEndTime());// 结束时间
			arr.put(o);

		}
		return arr.toString();
	}

	@Autowired
	private ApiUtilMng apiUtilMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private UserMng userMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private CollectMng collectMng;
	@Autowired
	private CouponMng couponMng;
	@Autowired
	private MemberCouponMng memberCouponMng;

}