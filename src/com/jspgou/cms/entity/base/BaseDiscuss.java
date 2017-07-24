package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_shop_diss table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_shop_discuss"
 * This class should preserve.
 * @preserve
 */

public abstract class BaseDiscuss  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String REF = "Discuss";
	public static String PROP_MEMBER = "member";
	public static String PROP_TIME = "time";
	public static String PROP_PRODUCT = "product";
	public static String PROP_ID = "id";
	public static String PROP_CONTENT = "content";


	// constructors
	public BaseDiscuss () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDiscuss (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseDiscuss (
		java.lang.Long id,
		com.jspgou.cms.entity.ShopMember member,
		com.jspgou.cms.entity.Product product,
		java.lang.String content) {

		this.setId(id);
		this.setMember(member);
		this.setProduct(product);
		this.setContent(content);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String content;
	private java.util.Date time;
	//* 添加字段，评价回复
	private java.lang.String replay;
	private java.lang.String discussType;


	// many to one
	private com.jspgou.cms.entity.ShopMember member;
	private com.jspgou.cms.entity.Product product;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
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
	 * Return the value associated with the column: content
	 */
	public java.lang.String getContent () {
		return content;
	}

	/**
	 * Set the value related to the column: content
	 * @param content the content value
	 */
	public void setContent (java.lang.String content) {
		this.content = content;
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


	public java.lang.String getDiscussType(){
		return discussType;
	}
	
	public void setDiscussType(java.lang.String discussType){
		this.discussType=discussType;
	}
	
	public java.lang.String getReplay(){
		return replay;
	}
	
	public void setReplay(java.lang.String replay){
		this.replay=replay;	
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



	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Discuss)) return false;
		else {
			com.jspgou.cms.entity.Discuss discuss = (com.jspgou.cms.entity.Discuss) obj;
			if (null == this.getId() || null == discuss.getId()) return false;
			else return (this.getId().equals(discuss.getId()));
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