package com.jspgou.cms.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiUserLogin;

public interface ApiUserLoginDao {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiUserLogin findById(Long id);

	public ApiUserLogin save(ApiUserLogin bean);

	public ApiUserLogin updateByUpdater(Updater<ApiUserLogin> updater);

	public ApiUserLogin deleteById(Long id);
	
	public ApiUserLogin findBySessionKey(String sessionKey);
	
	public ApiUserLogin findByUsername(String username);
	
	
}