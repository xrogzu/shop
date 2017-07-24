package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_shop_collect table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_shop_collect"
 * This class should preserve.
 * @preserve
 */

public abstract class BaseCollect  implements Serializable {

	public static String REF = "Collect";
	public static String PROP_MEMBER = "member";
	public static String PROP_TIME = "time";
	public static String PROP_PRODUCT = "product";
	public static String PROP_FASHION = "fashion";
	public static String PROP_ID = "id";


	// constructors
	public BaseCollect () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCollect (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCollect (
		java.lang.Integer id,
		com.jspgou.cms.entity.ShopMember member,
		com.jspgou.cms.entity.Product product) {

		this.setId(id);
		this.setMember(member);
		this.setProduct(product);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date time;

	// many to one
	private com.jspgou.cms.entity.ShopMember member;
	private com.jspgou.cms.entity.Product product;
	private com.jspgou.cms.entity.ProductFashion fashion;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
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
	 * Return the value associated with the column: time
	 */
	public java.util.Date getTime () {
		return time;
	}

	/**
	 * Set the value related to the column: time
	 * @param time the time value
	 */
	public void setTime (java.util.Date time) {
		this.time = time;
	}


	/**
	 * Return the value associated with the column: member_id
	 */
	public com.jspgou.cms.entity.ShopMember getMember () {
		return member;
	}

	/**
	 * Set the value related to the column: member_id
	 * @param member the member_id value
	 */
	public void setMember (com.jspgou.cms.entity.ShopMember member) {
		this.member = member;
	}


	/**
	 * Return the value associated with the column: product_id
	 */
	public com.jspgou.cms.entity.Product getProduct () {
		return product;
	}

	/**
	 * Set the value related to the column: product_id
	 * @param product the product_id value
	 */
	public void setProduct (com.jspgou.cms.entity.Product product) {
		this.product = product;
	}


	/**
	 * Return the value associated with the column: fashion_id
	 */
	public com.jspgou.cms.entity.ProductFashion getFashion () {
		return fashion;
	}

	/**
	 * Set the value related to the column: fashion_id
	 * @param fashion the fashion_id value
	 */
	public void setFashion (com.jspgou.cms.entity.ProductFashion fashion) {
		this.fashion = fashion;
	}



	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Collect)) return false;
		else {
			com.jspgou.cms.entity.Collect collect = (com.jspgou.cms.entity.Collect) obj;
			if (null == this.getId() || null == collect.getId()) return false;
			else return (this.getId().equals(collect.getId()));
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