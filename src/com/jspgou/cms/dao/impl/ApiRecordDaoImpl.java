package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiRecordDao;
import com.jspgou.cms.entity.ApiRecord;

@Repository
public class ApiRecordDaoImpl extends HibernateBaseDao<ApiRecord, Long> implements ApiRecordDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public ApiRecord findById(Long id) {
		ApiRecord entity = get(id);
		return entity;
	}

	public ApiRecord save(ApiRecord bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiRecord deleteById(Long id) {
		ApiRecord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	
	public ApiRecord findBySign(String sign,String appId){
		String hql = "from ApiRecord bean where bean.sign=? and bean.apiAccount.appId=?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, sign).setParameter(1, appId);
		// 做一些容错处理，因为毕竟没有在数据库中限定path是唯一的。
		query.setMaxResults(1);
		return (ApiRecord) query.uniqueResult();
	}
 
	
	@Override
	protected Class<ApiRecord> getEntityClass() {
		return ApiRecord.class;
	}
}