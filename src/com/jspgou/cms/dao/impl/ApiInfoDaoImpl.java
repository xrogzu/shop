package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiInfoDao;
import com.jspgou.cms.entity.ApiInfo;

@Repository
public class ApiInfoDaoImpl extends HibernateBaseDao<ApiInfo, Long> implements ApiInfoDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public ApiInfo findById(Long id) {
		ApiInfo entity = get(id);
		return entity;
	}
	
		
	public ApiInfo findByApiUrl(String apiUrl){
		return findUniqueByProperty("apiUrl",apiUrl);
	}

	public ApiInfo save(ApiInfo bean) {
		getSession().save(bean);
		return bean;
	}

	public ApiInfo deleteById(Long id) {
		ApiInfo entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ApiInfo> getEntityClass() {
		return ApiInfo.class;
	}
}