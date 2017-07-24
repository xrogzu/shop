package com.jspgou.cms.dao;

import com.jspgou.cms.entity.WebserviceAuth;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

public interface WebserviceAuthDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public WebserviceAuth findByUsername(String username);

	public WebserviceAuth findById(Integer id);

	public WebserviceAuth save(WebserviceAuth bean);

	public WebserviceAuth updateByUpdater(Updater<WebserviceAuth> updater);

	public WebserviceAuth deleteById(Integer id);
}