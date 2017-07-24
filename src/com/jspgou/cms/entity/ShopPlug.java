package com.jspgou.cms.entity;

import com.jspgou.cms.entity.base.BaseShopPlug;
 
public class ShopPlug extends BaseShopPlug {
	private static final long serialVersionUID = 1L;
	
	public boolean getUsed(){
		return isUsed();
	}
	
	public boolean getFileConflict () {
		return isFileConflict();
	}
	 
	public boolean getCanInstall(){
		if(!getUsed()&&!getFileConflict()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getCanUnInstall(){
		if(getUsed()&&!getFileConflict()){
			return true;
		}else{
			return false;
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopPlug () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopPlug (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ShopPlug (
		java.lang.Long id,
		java.lang.String name,
		java.lang.String path,
		java.util.Date uploadTime,
		boolean fileConflict,
		boolean used) {

		super (
			id,
			name,
			path,
			uploadTime,
			fileConflict,
			used);
	}

/*[CONSTRUCTOR MARKER END]*/


}