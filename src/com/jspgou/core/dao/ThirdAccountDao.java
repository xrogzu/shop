package com.jspgou.core.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.ThirdAccount;



public interface ThirdAccountDao {
	public Pagination getPage(String username,String source,int pageNo, int pageSize);

	public ThirdAccount findById(Long id);
	
	public ThirdAccount findByKey(String key);

	public ThirdAccount save(ThirdAccount bean);

	public ThirdAccount updateByUpdater(Updater<ThirdAccount> updater);

	public ThirdAccount deleteById(Long id);
}