package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseRelatedgoods;


/**
* This class should preserve.
* @preserve
*/
public class Relatedgoods extends BaseRelatedgoods {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Relatedgoods () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Relatedgoods (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Relatedgoods (
		java.lang.Long id,
		java.lang.Long productId,
		java.lang.Long productIds) {

		super (
			id,
			productId,
			productIds);
	}

/*[CONSTRUCTOR MARKER END]*/


}