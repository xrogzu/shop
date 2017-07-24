package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseApiAccount;



public class ApiAccount extends BaseApiAccount {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiAccount (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiAccount (
		java.lang.Long id,
		java.lang.String appId,
		java.lang.String appKey,
		java.lang.String aesKey,
		boolean disabled) {

		super (
			id,
			appId,
			appKey,
			aesKey,
			disabled);
	}

/*[CONSTRUCTOR MARKER END]*/


}