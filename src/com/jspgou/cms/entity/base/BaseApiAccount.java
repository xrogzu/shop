package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_api_account table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_api_account"
 */

public abstract class BaseApiAccount  implements Serializable {

	public static String REF = "ApiAccount";
	public static String PROP_AES_KEY = "aesKey";
	public static String PROP_DISABLED = "disabled";
	public static String PROP_IV_KEY = "ivKey";
	public static String PROP_APP_ID = "appId";
	public static String PROP_ID = "id";
	public static String PROP_APP_KEY = "appKey";


	// constructors
	public BaseApiAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseApiAccount (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseApiAccount (
		java.lang.Long id,
		java.lang.String appId,
		java.lang.String appKey,
		java.lang.String aesKey,
		boolean disabled) {

		this.setId(id);
		this.setAppId(appId);
		this.setAppKey(appKey);
		this.setAesKey(aesKey);
		this.setDisabled(disabled);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String appId;
	private java.lang.String appKey;
	private java.lang.String aesKey;
	private boolean disabled;
	private java.lang.String ivKey;



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
	 * Return the value associated with the column: app_id
	 */
	public java.lang.String getAppId () {
		return appId;
	}

	/**
	 * Set the value related to the column: app_id
	 * @param appId the app_id value
	 */
	public void setAppId (java.lang.String appId) {
		this.appId = appId;
	}


	/**
	 * Return the value associated with the column: app_key
	 */
	public java.lang.String getAppKey () {
		return appKey;
	}

	/**
	 * Set the value related to the column: app_key
	 * @param appKey the app_key value
	 */
	public void setAppKey (java.lang.String appKey) {
		this.appKey = appKey;
	}


	/**
	 * Return the value associated with the column: aes_key
	 */
	public java.lang.String getAesKey () {
		return aesKey;
	}

	/**
	 * Set the value related to the column: aes_key
	 * @param aesKey the aes_key value
	 */
	public void setAesKey (java.lang.String aesKey) {
		this.aesKey = aesKey;
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
	 * Return the value associated with the column: iv_key
	 */
	public java.lang.String getIvKey () {
		return ivKey;
	}

	/**
	 * Set the value related to the column: iv_key
	 * @param ivKey the iv_key value
	 */
	public void setIvKey (java.lang.String ivKey) {
		this.ivKey = ivKey;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.ApiAccount)) return false;
		else {
			com.jspgou.cms.entity.ApiAccount apiAccount = (com.jspgou.cms.entity.ApiAccount) obj;
			if (null == this.getId() || null == apiAccount.getId()) return false;
			else return (this.getId().equals(apiAccount.getId()));
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