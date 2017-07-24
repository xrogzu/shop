package com.jspgou.cms.manager;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiAccount;

public interface ApiAccountMng {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiAccount findById(Long id);

	public ApiAccount findByAppId(String appId);
	
	public ApiAccount save(ApiAccount bean);

	public ApiAccount update(ApiAccount bean);

	public ApiAccount deleteById(Long id);
	
	public ApiAccount[] deleteByIds(Long[] ids);
}