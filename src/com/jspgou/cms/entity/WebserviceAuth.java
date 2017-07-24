package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseWebserviceAuth;



public class WebserviceAuth extends BaseWebserviceAuth {
	private static final long serialVersionUID = 1L;


	public boolean getEnable() {
		return super.isEnable();
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public WebserviceAuth () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public WebserviceAuth (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public WebserviceAuth (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.lang.String system,
		boolean enable) {

		super (
			id,
			username,
			password,
			system,
			enable);
	}

/*[CONSTRUCTOR MARKER END]*/


}