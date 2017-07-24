package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.WebserviceCallRecordDao;
import com.jspgou.cms.entity.WebserviceCallRecord;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

@Repository
public class WebserviceCallRecordDaoImpl extends HibernateBaseDao<WebserviceCallRecord, Integer> implements WebserviceCallRecordDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	@Override
	public WebserviceCallRecord findById(Integer id) {
		WebserviceCallRecord entity = get(id);
		return entity;
	}

	@Override
	public WebserviceCallRecord save(WebserviceCallRecord bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public WebserviceCallRecord deleteById(Integer id) {
		WebserviceCallRecord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<WebserviceCallRecord> getEntityClass() {
		return WebserviceCallRecord.class;
	}
}