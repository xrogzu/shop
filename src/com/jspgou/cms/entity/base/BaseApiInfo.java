package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_api_info table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_api_info"
 */

public abstract class BaseApiInfo  implements Serializable {

	public static String REF = "ApiInfo";
	public static String PROP_LAST_CALL_TIME = "lastCallTime";
	public static String PROP_API_URL = "apiUrl";
	public static String PROP_API_NAME = "apiName";
	public static String PROP_API_CODE = "apiCode";
	public static String PROP_LIMIT_CALL_DAY = "limitCallDay";
	public static String PROP_DISABLED = "disabled";
	public static String PROP_ID = "id";
	public static String PROP_CALL_MONTH_COUNT = "callMonthCount";
	public static String PROP_CALL_DAY_COUNT = "callDayCount";
	public static String PROP_CALL_WEEK_COUNT = "callWeekCount";
	public static String PROP_CALL_TOTAL_COUNT = "callTotalCount";


	// constructors
	public BaseApiInfo () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseApiInfo (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseApiInfo (
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

		this.setId(id);
		this.setApiName(apiName);
		this.setApiUrl(apiUrl);
		this.setApiCode(apiCode);
		this.setDisabled(disabled);
		this.setLimitCallDay(limitCallDay);
		this.setCallTotalCount(callTotalCount);
		this.setCallMonthCount(callMonthCount);
		this.setCallWeekCount(callWeekCount);
		this.setCallDayCount(callDayCount);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String apiName;
	private java.lang.String apiUrl;
	private java.lang.String apiCode;
	private boolean disabled;
	private java.lang.Integer limitCallDay;
	private java.lang.Integer callTotalCount;
	private java.lang.Integer callMonthCount;
	private java.lang.Integer callWeekCount;
	private java.lang.Integer callDayCount;
	private java.util.Date lastCallTime;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="Id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: api_name
	 */
	public java.lang.String getApiName () {
		return apiName;
	}

	/**
	 * Set the value related to the column: api_name
	 * @param apiName the api_name value
	 */
	public void setApiName (java.lang.String apiName) {
		this.apiName = apiName;
	}


	/**
	 * Return the value associated with the column: api_url
	 */
	public java.lang.String getApiUrl () {
		return apiUrl;
	}

	/**
	 * Set the value related to the column: api_url
	 * @param apiUrl the api_url value
	 */
	public void setApiUrl (java.lang.String apiUrl) {
		this.apiUrl = apiUrl;
	}


	/**
	 * Return the value associated with the column: api_code
	 */
	public java.lang.String getApiCode () {
		return apiCode;
	}

	/**
	 * Set the value related to the column: api_code
	 * @param apiCode the api_code value
	 */
	public void setApiCode (java.lang.String apiCode) {
		this.apiCode = apiCode;
	}


	/**
	 * Return the value associated with the column: disabled
	 */
	public boolean isDisabled () {
		return disabled;
	}

	/**
	 * Set the value related to the column: disabled
	 * @param disabled the disabled value
	 */
	public void setDisabled (boolean disabled) {
		this.disabled = disabled;
	}


	/**
	 * Return the value associated with the column: limit_call_day
	 */
	public java.lang.Integer getLimitCallDay () {
		return limitCallDay;
	}

	/**
	 * Set the value related to the column: limit_call_day
	 * @param limitCallDay the limit_call_day value
	 */
	public void setLimitCallDay (java.lang.Integer limitCallDay) {
		this.limitCallDay = limitCallDay;
	}


	/**
	 * Return the value associated with the column: call_total_count
	 */
	public java.lang.Integer getCallTotalCount () {
		return callTotalCount;
	}

	/**
	 * Set the value related to the column: call_total_count
	 * @param callTotalCount the call_total_count value
	 */
	public void setCallTotalCount (java.lang.Integer callTotalCount) {
		this.callTotalCount = callTotalCount;
	}


	/**
	 * Return the value associated with the column: call_month_count
	 */
	public java.lang.Integer getCallMonthCount () {
		return callMonthCount;
	}

	/**
	 * Set the value related to the column: call_month_count
	 * @param callMonthCount the call_month_count value
	 */
	public void setCallMonthCount (java.lang.Integer callMonthCount) {
		this.callMonthCount = callMonthCount;
	}


	/**
	 * Return the value associated with the column: call_week_count
	 */
	public java.lang.Integer getCallWeekCount () {
		return callWeekCount;
	}

	/**
	 * Set the value related to the column: call_week_count
	 * @param callWeekCount the call_week_count value
	 */
	public void setCallWeekCount (java.lang.Integer callWeekCount) {
		this.callWeekCount = callWeekCount;
	}


	/**
	 * Return the value associated with the column: call_day_count
	 */
	public java.lang.Integer getCallDayCount () {
		return callDayCount;
	}

	/**
	 * Set the value related to the column: call_day_count
	 * @param callDayCount the call_day_count value
	 */
	public void setCallDayCount (java.lang.Integer callDayCount) {
		this.callDayCount = callDayCount;
	}


	/**
	 * Return the value associated with the column: last_call_time
	 */
	public java.util.Date getLastCallTime () {
		return lastCallTime;
	}

	/**
	 * Set the value related to the column: last_call_time
	 * @param lastCallTime the last_call_time value
	 */
	public void setLastCallTime (java.util.Date lastCallTime) {
		this.lastCallTime = lastCallTime;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.ApiInfo)) return false;
		else {
			com.jspgou.cms.entity.ApiInfo apiInfo = (com.jspgou.cms.entity.ApiInfo) obj;
			if (null == this.getId() || null == apiInfo.getId()) return false;
			else return (this.getId().equals(apiInfo.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}