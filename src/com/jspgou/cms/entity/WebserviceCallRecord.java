package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseWebserviceCallRecord;



public class WebserviceCallRecord extends BaseWebserviceCallRecord {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public WebserviceCallRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public WebserviceCallRecord (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public WebserviceCallRecord (
		java.lang.Integer id,
		com.jspgou.cms.entity.WebserviceAuth auth,
		java.lang.String serviceCode,
		java.util.Date recordTime) {

		super (
			id,
			auth,
			serviceCode,
			recordTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}