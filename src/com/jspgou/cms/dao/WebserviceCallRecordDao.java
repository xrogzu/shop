package com.jspgou.cms.dao;

import com.jspgou.cms.entity.WebserviceCallRecord;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

public interface WebserviceCallRecordDao {
	public Pagination getPage(int pageNo, int pageSize);

	public WebserviceCallRecord findById(Integer id);

	public WebserviceCallRecord save(WebserviceCallRecord bean);

	public WebserviceCallRecord updateByUpdater(Updater<WebserviceCallRecord> updater);

	public WebserviceCallRecord deleteById(Integer id);
}