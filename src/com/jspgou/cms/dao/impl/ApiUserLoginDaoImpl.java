package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiUserLoginDao;
import com.jspgou.cms.entity.ApiUserLogin;

@Repository
public class ApiUserLoginDaoImpl extends HibernateBaseDao<ApiUserLogin, Long> implements ApiUserLoginDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public ApiUserLogin findById(Long id) {
		ApiUserLogin entity = get(id);
		return entity;
	}

	public ApiUserLogin save(ApiUserLogin bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiUserLogin deleteById(Long id) {
		ApiUserLogin entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public ApiUserLogin findBySessionKey(String sessionKey){
		return findUniqueByProperty("sessionKey",sessionKey);
	}
	
	
	public ApiUserLogin findByUsername(String username){
		return findUniqueByProperty("username",username);
	}
	
	@Override
	protected Class<ApiUserLogin> getEntityClass() {
		return ApiUserLogin.class;
	}
}