package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseMessage;



public class Message extends BaseMessage {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Message () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Message (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Message (
		java.lang.Long id,
		com.jspgou.cms.entity.ShopMember msgReceiverUser,
		com.jspgou.cms.entity.ShopAdmin msgSendUser,
		java.lang.String msgTitle,
		boolean msgStatus,
		java.lang.Integer msgBox) {

		super (
			id,
			msgReceiverUser,
			msgSendUser,
			msgTitle,
			msgStatus,
			msgBox);
	}

/*[CONSTRUCTOR MARKER END]*/


}