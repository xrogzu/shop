package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_api_record table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_api_record"
 */

public abstract class BaseApiRecord  implements Serializable {

	public static String REF = "ApiRecord";
	public static String PROP_API_ACCOUNT = "apiAccount";
	public static String PROP_CALL_TIME_STAMP = "callTimeStamp";
	public static String PROP_SIGN = "sign";
	public static String PROP_API_INFO = "apiInfo";
	public static String PROP_ID = "id";
	public static String PROP_API_IP = "apiIp";
	public static String PROP_API_CALL_TIME = "apiCallTime";


	// constructors
	public BaseApiRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseApiRecord (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseApiRecord (
		java.lang.Long id,
		com.jspgou.cms.entity.ApiAccount apiAccount,
		java.util.Date apiCallTime,
		java.lang.Long callTimeStamp,
		java.lang.String sign) {

		this.setId(id);
		this.setApiAccount(apiAccount);
		this.setApiCallTime(apiCallTime);
		this.setCallTimeStamp(callTimeStamp);
		this.setSign(sign);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String apiIp;
	private java.util.Date apiCallTime;
	private java.lang.Long callTimeStamp;
	private java.lang.String sign;

	// many to one
	private com.jspgou.cms.entity.ApiAccount apiAccount;
	private com.jspgou.cms.entity.ApiInfo apiInfo;



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
	 * Return the value associated with the column: api_ip
	 */
	public java.lang.String getApiIp () {
		return apiIp;
	}

	/**
	 * Set the value related to the column: api_ip
	 * @param apiIp the api_ip value
	 */
	public void setApiIp (java.lang.String apiIp) {
		this.apiIp = apiIp;
	}


	/**
	 * Return the value associated with the column: api_call_time
	 */
	public java.util.Date getApiCallTime () {
		return apiCallTime;
	}

	/**
	 * Set the value related to the column: api_call_time
	 * @param apiCallTime the api_call_time value
	 */
	public void setApiCallTime (java.util.Date apiCallTime) {
		this.apiCallTime = apiCallTime;
	}


	/**
	 * Return the value associated with the column: call_time_stamp
	 */
	public java.lang.Long getCallTimeStamp () {
		return callTimeStamp;
	}

	/**
	 * Set the value related to the column: call_time_stamp
	 * @param callTimeStamp the call_time_stamp value
	 */
	public void setCallTimeStamp (java.lang.Long callTimeStamp) {
		this.callTimeStamp = callTimeStamp;
	}


	/**
	 * Return the value associated with the column: sign
	 */
	public java.lang.String getSign () {
		return sign;
	}

	/**
	 * Set the value related to the column: sign
	 * @param sign the sign value
	 */
	public void setSign (java.lang.String sign) {
		this.sign = sign;
	}


	/**
	 * Return the value associated with the column: api_account
	 */
	public com.jspgou.cms.entity.ApiAccount getApiAccount () {
		return apiAccount;
	}

	/**
	 * Set the value related to the column: api_account
	 * @param apiAccount the api_account value
	 */
	public void setApiAccount (com.jspgou.cms.entity.ApiAccount apiAccount) {
		this.apiAccount = apiAccount;
	}


	/**
	 * Return the value associated with the column: api_info_id
	 */
	public com.jspgou.cms.entity.ApiInfo getApiInfo () {
		return apiInfo;
	}

	/**
	 * Set the value related to the column: api_info_id
	 * @param apiInfo the api_info_id value
	 */
	public void setApiInfo (com.jspgou.cms.entity.ApiInfo apiInfo) {
		this.apiInfo = apiInfo;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.ApiRecord)) return false;
		else {
			com.jspgou.cms.entity.ApiRecord apiRecord = (com.jspgou.cms.entity.ApiRecord) obj;
			if (null == this.getId() || null == apiRecord.getId()) return false;
			else return (this.getId().equals(apiRecord.getId()));
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