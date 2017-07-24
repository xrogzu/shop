package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiAccountDao;
import com.jspgou.cms.entity.ApiAccount;

@Repository
public class ApiAccountDaoImpl extends HibernateBaseDao<ApiAccount, Long> implements ApiAccountDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public ApiAccount findById(Long id) {
		ApiAccount entity = get(id);
		return entity;
	}

	public ApiAccount save(ApiAccount bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiAccount deleteById(Long id) {
		ApiAccount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	
	public ApiAccount findByAppId(String appId){
		return findUniqueByProperty("appId",appId);
	}
	
	
	@Override
	protected Class<ApiAccount> getEntityClass() {
		return ApiAccount.class;
	}
}