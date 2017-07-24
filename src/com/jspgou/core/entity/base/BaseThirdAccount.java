package com.jspgou.core.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_third_account table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_third_account"
 */

public abstract class BaseThirdAccount  implements Serializable {

	public static String REF = "ThirdAccount";
	public static String PROP_SOURCE = "source";
	public static String PROP_ACCOUNT_KEY = "accountKey";
	public static String PROP_THIRD_LOGIN_NAME = "thirdLoginName";
	public static String PROP_USERNAME = "username";
	public static String PROP_ID = "id";


	// constructors
	public BaseThirdAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseThirdAccount (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseThirdAccount (
		java.lang.Long id,
		java.lang.String accountKey,
		java.lang.String username,
		java.lang.String source) {

		this.setId(id);
		this.setAccountKey(accountKey);
		this.setUsername(username);
		this.setSource(source);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String accountKey;
	private java.lang.String username;
	private java.lang.String source;
	private java.lang.String thirdLoginName;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="account_id"
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
	 * Return the value associated with the column: account_key
	 */
	public java.lang.String getAccountKey () {
		return accountKey;
	}

	/**
	 * Set the value related to the column: account_key
	 * @param accountKey the account_key value
	 */
	public void setAccountKey (java.lang.String accountKey) {
		this.accountKey = accountKey;
	}


	/**
	 * Return the value associated with the column: username
	 */
	public java.lang.String getUsername () {
		return username;
	}

	/**
	 * Set the value related to the column: username
	 * @param username the username value
	 */
	public void setUsername (java.lang.String username) {
		this.username = username;
	}


	/**
	 * Return the value associated with the column: source
	 */
	public java.lang.String getSource () {
		return source;
	}

	/**
	 * Set the value related to the column: source
	 * @param source the source value
	 */
	public void setSource (java.lang.String source) {
		this.source = source;
	}


	/**
	 * Return the value associated with the column: third_login_name
	 */
	public java.lang.String getThirdLoginName () {
		return thirdLoginName;
	}

	/**
	 * Set the value related to the column: third_login_name
	 * @param thirdLoginName the third_login_name value
	 */
	public void setThirdLoginName (java.lang.String thirdLoginName) {
		this.thirdLoginName = thirdLoginName;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.core.entity.ThirdAccount)) return false;
		else {
			com.jspgou.core.entity.ThirdAccount thirdAccount = (com.jspgou.core.entity.ThirdAccount) obj;
			if (null == this.getId() || null == thirdAccount.getId()) return false;
			else return (this.getId().equals(thirdAccount.getId()));
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