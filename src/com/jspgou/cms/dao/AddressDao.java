package com.jspgou.cms.dao;

import java.util.List;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Address;

/**
* This class should preserve.
* @preserve
*/
public interface AddressDao {
	public List<Address> getListById(Long parentId);
	
	public Pagination getPage(int pageNo, int pageSize);
	
	public Pagination getPageByParentId(Long parentId,int pageNo, int pageSize);

	public Address findById(Long id);

	public Address save(Address bean);

	public Address updateByUpdater(Updater<Address> updater);

	public Address deleteById(Long id);
}