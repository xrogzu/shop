package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseReceiverMessage;
import com.jspgou.common.util.StrUtils;



public class ReceiverMessage extends BaseReceiverMessage {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ReceiverMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ReceiverMessage (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ReceiverMessage (
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
	public String getTitleHtml() {
		return StrUtils.txt2htm(getMsgTitle());
	}
	public String getContentHtml() {
		return StrUtils.txt2htm(getMsgContent());
	}

/*[CONSTRUCTOR MARKER END]*/


}