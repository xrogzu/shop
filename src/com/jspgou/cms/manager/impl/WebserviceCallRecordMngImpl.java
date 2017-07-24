package com.jspgou.cms.manager.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.WebserviceCallRecordDao;
import com.jspgou.cms.entity.WebserviceCallRecord;
import com.jspgou.cms.manager.WebserviceAuthMng;
import com.jspgou.cms.manager.WebserviceCallRecordMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

@Service
@Transactional
public class WebserviceCallRecordMngImpl implements WebserviceCallRecordMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public WebserviceCallRecord findById(Integer id) {
		WebserviceCallRecord entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public WebserviceCallRecord save(String clientUsername,String serviceCode){
		WebserviceCallRecord record=new WebserviceCallRecord();
		record.setAuth(webserviceAuthMng.findByUsername(clientUsername));
		record.setRecordTime(Calendar.getInstance().getTime());
		record.setServiceCode(serviceCode);
		return save(record);
	}

	@Override
	public WebserviceCallRecord save(WebserviceCallRecord bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public WebserviceCallRecord update(WebserviceCallRecord bean) {
		Updater<WebserviceCallRecord> updater = new Updater<WebserviceCallRecord>(bean);
		WebserviceCallRecord entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public WebserviceCallRecord deleteById(Integer id) {
		WebserviceCallRecord bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public WebserviceCallRecord[] deleteByIds(Integer[] ids) {
		WebserviceCallRecord[] beans = new WebserviceCallRecord[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Autowired
	private WebserviceAuthMng webserviceAuthMng;
	private WebserviceCallRecordDao dao;

	@Autowired
	public void setDao(WebserviceCallRecordDao dao) {
		this.dao = dao;
	}
}