package com.jspgou.cms.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ShopAdmin;

/**
* This class should preserve.
* @preserve
*/
public interface ShopAdminDao {
	public ShopAdmin getByUsername(String username);
	
	public Pagination getPage(Long webId, int pageNo, int pageSize);

	public ShopAdmin findById(Long id);

	public ShopAdmin save(ShopAdmin bean);

	public ShopAdmin updateByUpdater(Updater<ShopAdmin> updater);

	public ShopAdmin deleteById(Long id);
	

	//*添加方法,findByName
	public ShopAdmin findByName(String name);
}