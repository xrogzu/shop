package com.jspgou.core.entity;

import com.jspgou.core.entity.base.BaseThirdAccount;



public class ThirdAccount extends BaseThirdAccount {
	private static final long serialVersionUID = 1L;

	
	public static final String QQ_KEY="openId";
	public static final String SINA_KEY="uid";
	public static final String QQ_PLAT="QQ";
	public static final String SINA_PLAT="SINA";
	public static final String QQ_WEBO_KEY="weboOpenId";
	public static final String QQ_WEBO_PLAT="QQWEBO";
	public static final String WECHAT_KEY="weChatId";
	public static final String WECHAT_PLAT="WECHAT";

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ThirdAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ThirdAccount (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ThirdAccount (
		java.lang.Long id,
		java.lang.String accountKey,
		java.lang.String username,
		java.lang.String source) {

		super (
			id,
			accountKey,
			username,
			source);
	}

/*[CONSTRUCTOR MARKER END]*/


}