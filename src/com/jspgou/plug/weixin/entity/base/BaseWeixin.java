package com.jspgou.plug.weixin.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jg_weixin table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jg_weixin"
 */

public abstract class BaseWeixin  implements Serializable {

	public static String REF = "Weixin";
	public static String PROP_SITE = "site";
	public static String PROP_ID = "id";
	public static String PROP_PIC = "pic";


	// constructors
	public BaseWeixin () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseWeixin (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;
	
	private java.lang.String welcome;
	private java.lang.String pic;
	private java.lang.String appKey;
	private java.lang.String appSecret;
	private java.lang.String token;

	// one to one
	private com.jspgou.core.entity.Website site;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="site_id"
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


	public java.lang.String getWelcome() {
		return welcome;
	}

	public void setWelcome(java.lang.String welcome) {
		this.welcome = welcome;
	}

	/**
	 * Return the value associated with the column: wx_pic
	 */
	public java.lang.String getPic () {
		return pic;
	}

	/**
	 * Set the value related to the column: wx_pic
	 * @param pic the wx_pic value
	 */
	public void setPic (java.lang.String pic) {
		this.pic = pic;
	}
	public java.lang.String getToken() {
		return token;
	}

	public void setToken(java.lang.String token) {
		this.token = token;
	}

	public java.lang.String getAppKey() {
		return appKey;
	}
	public void setAppKey(java.lang.String appKey) {
		this.appKey = appKey;
	}

	public java.lang.String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(java.lang.String appSecret) {
		this.appSecret = appSecret;
	}


	/**
	 * Return the value associated with the column: site
	 */
	public com.jspgou.core.entity.Website getSite () {
		return site;
	}

	/**
	 * Set the value related to the column: site
	 * @param site the site value
	 */
	public void setSite (com.jspgou.core.entity.Website site) {
		this.site = site;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.plug.weixin.entity.Weixin)) return false;
		else {
			com.jspgou.plug.weixin.entity.Weixin weixin = (com.jspgou.plug.weixin.entity.Weixin) obj;
			if (null == this.getId() || null == weixin.getId()) return false;
			else return (this.getId().equals(weixin.getId()));
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