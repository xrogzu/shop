package com.jspgou.cms.dao;

import java.util.List;

import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

public interface ShopPlugDao { 
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<ShopPlug> getList(String author,Boolean used);

	public ShopPlug findById(Long id);
	
	public ShopPlug findByPath(String plugPath);

	public ShopPlug save(ShopPlug bean);

	public ShopPlug updateByUpdater(Updater<ShopPlug> updater);

	public ShopPlug deleteById(Long id);
}
