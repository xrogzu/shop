package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_webservice_call_record table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_webservice_call_record"
 */

public abstract class BaseWebserviceCallRecord  implements Serializable {

	public static String REF = "WebserviceCallRecord";
	public static String PROP_SERVICE_CODE = "serviceCode";
	public static String PROP_ID = "id";
	public static String PROP_RECORD_TIME = "recordTime";
	public static String PROP_AUTH = "auth";


	// constructors
	public BaseWebserviceCallRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseWebserviceCallRecord (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseWebserviceCallRecord (
		java.lang.Integer id,
		com.jspgou.cms.entity.WebserviceAuth auth,
		java.lang.String serviceCode,
		java.util.Date recordTime) {

		this.setId(id);
		this.setAuth(auth);
		this.setServiceCode(serviceCode);
		this.setRecordTime(recordTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String serviceCode;
	private java.util.Date recordTime;

	// many to one
	private com.jspgou.cms.entity.WebserviceAuth auth;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="record_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: service_code
	 */
	public java.lang.String getServiceCode () {
		return serviceCode;
	}

	/**
	 * Set the value related to the column: service_code
	 * @param serviceCode the service_code value
	 */
	public void setServiceCode (java.lang.String serviceCode) {
		this.serviceCode = serviceCode;
	}


	/**
	 * Return the value associated with the column: record_time
	 */
	public java.util.Date getRecordTime () {
		return recordTime;
	}

	/**
	 * Set the value related to the column: record_time
	 * @param recordTime the record_time value
	 */
	public void setRecordTime (java.util.Date recordTime) {
		this.recordTime = recordTime;
	}


	/**
	 * Return the value associated with the column: auth_id
	 */
	public com.jspgou.cms.entity.WebserviceAuth getAuth () {
		return auth;
	}

	/**
	 * Set the value related to the column: auth_id
	 * @param auth the auth_id value
	 */
	public void setAuth (com.jspgou.cms.entity.WebserviceAuth auth) {
		this.auth = auth;
	}



	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.WebserviceCallRecord)) return false;
		else {
			com.jspgou.cms.entity.WebserviceCallRecord cmsWebserviceCallRecord = (com.jspgou.cms.entity.WebserviceCallRecord) obj;
			if (null == this.getId() || null == cmsWebserviceCallRecord.getId()) return false;
			else return (this.getId().equals(cmsWebserviceCallRecord.getId()));
		}
	}

	@Override
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


	@Override
	public String toString () {
		return super.toString();
	}


}