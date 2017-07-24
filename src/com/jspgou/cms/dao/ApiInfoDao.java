package com.jspgou.cms.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiInfo;

public interface ApiInfoDao {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiInfo findById(Long id);
	
	public ApiInfo findByApiUrl(String apiUrl);;

	public ApiInfo save(ApiInfo bean);

	public ApiInfo updateByUpdater(Updater<ApiInfo> updater);

	public ApiInfo deleteById(Long id);
}