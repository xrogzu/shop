package com.jspgou.cms.manager;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.entity.ApiRecord;

public interface ApiRecordMng {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiRecord findById(Long id);

	public ApiRecord save(ApiRecord bean);

	public ApiRecord update(ApiRecord bean);

	public ApiRecord deleteById(Long id);
	
	public ApiRecord[] deleteByIds(Long[] ids);
	
	public void callApiRecord(String ipAddr, String appId, String apiUrl,String sign);
	
	public ApiRecord findBySign(String sign,String appId);
}