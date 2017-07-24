package com.jspgou.cms.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiAccount;

public interface ApiAccountDao {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiAccount findById(Long id);
	
	public ApiAccount findByAppId(String appId);
	
	public ApiAccount save(ApiAccount bean);

	public ApiAccount updateByUpdater(Updater<ApiAccount> updater);

	public ApiAccount deleteById(Long id);
}