package com.jspgou.cms.entity;

import java.util.ArrayList;
import java.util.List;
import com.jspgou.cms.entity.base.BaseWebservice;



public class Webservice extends BaseWebservice {
	private static final long serialVersionUID = 1L;
	
	public static final String SERVICE_TYPE_ADD_USER = "addUser";
	public static final String SERVICE_TYPE_UPDATE_USER = "updateUser";
	public static final String SERVICE_TYPE_DELETE_USER = "deleteUser";
	
	public void addToParams(String name, String defaultValue) {
		List<WebserviceParam> list = getParams();
		if (list == null) {
			list = new ArrayList<WebserviceParam>();
			setParams(list);
		}
		WebserviceParam param = new WebserviceParam();
		param.setParamName(name);
		param.setDefaultValue(defaultValue);
		list.add(param);
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Webservice () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Webservice (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Webservice (
		java.lang.Integer id,
		java.lang.String address) {

		super (
			id,
			address);
	}

}