package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_message table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_message"
 */

public abstract class BaseMessage  implements Serializable {

	public static String REF = "Message";
	public static String PROP_MSG_STATUS = "msgStatus";
	public static String PROP_MSG_SEND_USER = "msgSendUser";
	public static String PROP_MSG_CONTENT = "msgContent";
	public static String PROP_MSG_BOX = "msgBox";
	public static String PROP_SEND_TIME = "sendTime";
	public static String PROP_ID = "id";
	public static String PROP_MSG_RECEIVER_USER = "msgReceiverUser";
	public static String PROP_MSG_TITLE = "msgTitle";


	// constructors
	public BaseMessage () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMessage (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMessage (
		java.lang.Long id,
		com.jspgou.cms.entity.ShopMember msgReceiverUser,
		com.jspgou.cms.entity.ShopAdmin msgSendUser,
		java.lang.String msgTitle,
		boolean msgStatus,
		java.lang.Integer msgBox) {

		this.setId(id);
		this.setMsgReceiverUser(msgReceiverUser);
		this.setMsgSendUser(msgSendUser);
		this.setMsgTitle(msgTitle);
		this.setMsgStatus(msgStatus);
		this.setMsgBox(msgBox);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String msgTitle;
	private java.lang.String msgContent;
	private java.util.Date sendTime;
	private boolean msgStatus;
	private java.lang.Integer msgBox;

	// many to one
	private com.jspgou.cms.entity.ShopMember msgReceiverUser;
	private com.jspgou.cms.entity.ShopAdmin msgSendUser;
	
	// collections
	private java.util.Set<com.jspgou.cms.entity.ReceiverMessage> receiverMsgs;

	/**
	 * Return the value associated with the column: receiverMsgs
	 */
	public java.util.Set<com.jspgou.cms.entity.ReceiverMessage> getReceiverMsgs () {
		return receiverMsgs;
	}

	/**
	 * Set the value related to the column: receiverMsgs
	 * @param receiverMsgs the receiverMsgs value
	 */
	public void setReceiverMsgs (java.util.Set<com.jspgou.cms.entity.ReceiverMessage> receiverMsgs) {
		this.receiverMsgs = receiverMsgs;
	}
	
	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="msg_id"
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
	 * Return the value associated with the column: msg_title
	 */
	public java.lang.String getMsgTitle () {
		return msgTitle;
	}

	/**
	 * Set the value related to the column: msg_title
	 * @param msgTitle the msg_title value
	 */
	public void setMsgTitle (java.lang.String msgTitle) {
		this.msgTitle = msgTitle;
	}


	/**
	 * Return the value associated with the column: msg_content
	 */
	public java.lang.String getMsgContent () {
		return msgContent;
	}

	/**
	 * Set the value related to the column: msg_content
	 * @param msgContent the msg_content value
	 */
	public void setMsgContent (java.lang.String msgContent) {
		this.msgContent = msgContent;
	}


	/**
	 * Return the value associated with the column: send_time
	 */
	public java.util.Date getSendTime () {
		return sendTime;
	}

	/**
	 * Set the value related to the column: send_time
	 * @param sendTime the send_time value
	 */
	public void setSendTime (java.util.Date sendTime) {
		this.sendTime = sendTime;
	}


	/**
	 * Return the value associated with the column: msg_status
	 */
	public boolean getMsgStatus () {
		return msgStatus;
	}

	/**
	 * Set the value related to the column: msg_status
	 * @param msgStatus the msg_status value
	 */
	public void setMsgStatus (boolean msgStatus) {
		this.msgStatus = msgStatus;
	}


	/**
	 * Return the value associated with the column: msg_box
	 */
	public java.lang.Integer getMsgBox () {
		return msgBox;
	}

	/**
	 * Set the value related to the column: msg_box
	 * @param msgBox the msg_box value
	 */
	public void setMsgBox (java.lang.Integer msgBox) {
		this.msgBox = msgBox;
	}


	/**
	 * Return the value associated with the column: msg_receiver_user
	 */
	public com.jspgou.cms.entity.ShopMember getMsgReceiverUser () {
		return msgReceiverUser;
	}

	/**
	 * Set the value related to the column: msg_receiver_user
	 * @param msgReceiverUser the msg_receiver_user value
	 */
	public void setMsgReceiverUser (com.jspgou.cms.entity.ShopMember msgReceiverUser) {
		this.msgReceiverUser = msgReceiverUser;
	}


	/**
	 * Return the value associated with the column: msg_send_user
	 */
	public com.jspgou.cms.entity.ShopAdmin getMsgSendUser () {
		return msgSendUser;
	}

	/**
	 * Set the value related to the column: msg_send_user
	 * @param msgSendUser the msg_send_user value
	 */
	public void setMsgSendUser (com.jspgou.cms.entity.ShopAdmin msgSendUser) {
		this.msgSendUser = msgSendUser;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Message)) return false;
		else {
			com.jspgou.cms.entity.Message message = (com.jspgou.cms.entity.Message) obj;
			if (null == this.getId() || null == message.getId()) return false;
			else return (this.getId().equals(message.getId()));
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