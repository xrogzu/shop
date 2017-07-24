package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseApiRecord;



public class ApiRecord extends BaseApiRecord {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiRecord (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiRecord (
		java.lang.Long id,
		com.jspgou.cms.entity.ApiAccount apiAccount,
		java.util.Date apiCallTime,
		java.lang.Long callTimeStamp,
		java.lang.String sign) {

		super (
			id,
			apiAccount,
			apiCallTime,
			callTimeStamp,
			sign);
	}

/*[CONSTRUCTOR MARKER END]*/


}