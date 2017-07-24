package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_shop_order_return table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_shop_order_return"
 * This class should preserve.
 * @preserve
 */

public abstract class BaseOrderReturnPicture  implements Serializable {

	public static String REF = "OrderReturnPicture";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_IMG_PATH = "imgPath";


	// constructors
	public BaseOrderReturnPicture () {
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseOrderReturnPicture (
		java.lang.String imgPath) {

		this.setImgPath(imgPath);
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.String imgPath;
	private java.lang.String description;






	/**
	 * Return the value associated with the column: img_path
	 */
	public java.lang.String getImgPath () {
		return imgPath;
	}

	/**
	 * Set the value related to the column: img_path
	 * @param imgPath the img_path value
	 */
	public void setImgPath (java.lang.String imgPath) {
		this.imgPath = imgPath;
	}


	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}






	@Override
	public String toString () {
		return super.toString();
	}


}