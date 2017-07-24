package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseApiInfo;



public class ApiInfo extends BaseApiInfo {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiInfo () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiInfo (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiInfo (
		java.lang.Long id,
		java.lang.String apiName,
		java.lang.String apiUrl,
		java.lang.String apiCode,
		boolean disabled,
		java.lang.Integer limitCallDay,
		java.lang.Integer callTotalCount,
		java.lang.Integer callMonthCount,
		java.lang.Integer callWeekCount,
		java.lang.Integer callDayCount) {

		super (
			id,
			apiName,
			apiUrl,
			apiCode,
			disabled,
			limitCallDay,
			callTotalCount,
			callMonthCount,
			callWeekCount,
			callDayCount);
	}
   
	

	public void init() {
		if (getCallTotalCount() == null) {
			setCallTotalCount(0);
		}
		if (getCallMonthCount() == null) {
			setCallMonthCount(0);
		}
		if (getCallWeekCount() == null) {
			setCallWeekCount(0);
		}
		if (getCallDayCount() == null) {
			setCallDayCount(0);
		}
	}
/*[CONSTRUCTOR MARKER END]*/


}