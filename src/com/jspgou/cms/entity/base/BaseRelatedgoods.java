package com.jspgou.cms.entity.base;

import java.io.Serializable;


public abstract class BaseRelatedgoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String PROP_PRODUCTIDS = "productIds";
	public static String PROP_PRODUCTID="productId";
	public static String PROP_ID = "id";


	// constructors
	public BaseRelatedgoods () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRelatedgoods (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRelatedgoods (
		java.lang.Long id,
		java.lang.Long productId,
		java.lang.Long productIds) {

		this.setId(id);
		this.setProductId(productId);
		this.setProductIds(productIds);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Long productId;
	private java.lang.Long productIds;




	public java.lang.Long getProductId() {
		return productId;
	}

	public void setProductId(java.lang.Long productId) {
		this.productId = productId;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
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
	 * Return the value associated with the column: name
	 */
	public java.lang.Long getProductIds () {
		return productIds;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setProductIds (java.lang.Long productIds) {
		this.productIds = productIds;
	}

	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Address)) return false;
		else {
			com.jspgou.cms.entity.Address address = (com.jspgou.cms.entity.Address) obj;
			if (null == this.getId() || null == address.getId()) return false;
			else return (this.getId().equals(address.getId()));
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