package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseApiUserLogin;



public class ApiUserLogin extends BaseApiUserLogin {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiUserLogin () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiUserLogin (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiUserLogin (
		java.lang.Long id,
		java.lang.String username,
		java.util.Date loginTime,
		java.lang.Integer loginCount) {

		super (
			id,
			username,
			loginTime,
			loginCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}