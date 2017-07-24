package com.jspgou.cms.entity;

import org.apache.commons.lang.StringUtils;

import com.jspgou.cms.entity.base.BaseShopMemberAddress;

/**
* This class should preserve.
* @preserve
*/
public class ShopMemberAddress extends BaseShopMemberAddress implements AddressInterface{
	private static final long serialVersionUID = 1L;
				
	//获得固定电话
 	public String getMobile() {
 		String mobile="";
 		if (StringUtils.isNotBlank(getPhone())) {
 			if(StringUtils.isNotBlank(getAreaCode())){
 				mobile=getAreaCode()+"-";
 			}
 			mobile+=getPhone();
 			if(StringUtils.isNotBlank(getExtNumber())){
 				mobile+="-"+getExtNumber();
 			}
 			return mobile;
 		}else{ 
 			return mobile;
 		}
 	}
	
	/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopMemberAddress () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopMemberAddress (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ShopMemberAddress (
		java.lang.Long id,
		com.jspgou.cms.entity.ShopMember member,
		java.lang.String province,
		java.lang.String city,
		java.lang.String country,
		java.lang.Long provinceId,
		java.lang.Long cityId,
		java.lang.Long countryId,
		java.lang.String username,
		java.lang.String detailaddress,
		boolean isDefault) {

		super (
			id,
			member,
			province,
			city,
			country,
			provinceId,
			cityId,
			countryId,
			username,
			detailaddress,
			isDefault);
	}

/*[CONSTRUCTOR MARKER END]*/
}