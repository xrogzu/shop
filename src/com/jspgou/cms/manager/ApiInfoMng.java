package com.jspgou.cms.manager;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiInfo;

public interface ApiInfoMng {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiInfo findById(Long id);
	
	public ApiInfo findByApiUrl(String apiUrl);

	public ApiInfo save(ApiInfo bean);

	public ApiInfo update(ApiInfo bean);

	public ApiInfo deleteById(Long id);
	
	public ApiInfo[] deleteByIds(Long[] ids);
}