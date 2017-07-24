package com.jspgou.cms.manager;

import com.jspgou.cms.entity.WebserviceCallRecord;
import com.jspgou.common.page.Pagination;

public interface WebserviceCallRecordMng {
	public Pagination getPage(int pageNo, int pageSize);

	public WebserviceCallRecord findById(Integer id);
	
	public WebserviceCallRecord save(String clientUsername,String serviceCode);

	public WebserviceCallRecord save(WebserviceCallRecord bean);

	public WebserviceCallRecord update(WebserviceCallRecord bean);

	public WebserviceCallRecord deleteById(Integer id);
	
	public WebserviceCallRecord[] deleteByIds(Integer[] ids);
}