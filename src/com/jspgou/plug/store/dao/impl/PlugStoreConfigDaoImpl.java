package com.jspgou.plug.store.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;


import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.store.dao.PlugStoreConfigDao;
import com.jspgou.plug.store.entity.PlugStoreConfig;

@Repository
public class PlugStoreConfigDaoImpl extends HibernateBaseDao<PlugStoreConfig, Integer> implements PlugStoreConfigDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public PlugStoreConfig findById(Integer id) {
	/*	PlugStoreConfig entity = get(id);
		return entity;*/
		return get(id);
	}

	public PlugStoreConfig save(PlugStoreConfig bean) {
		getSession().save(bean);
		return bean;
	}

	public PlugStoreConfig deleteById(Integer id) {
		PlugStoreConfig entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	public Class<PlugStoreConfig> getEntityClass() {
		return PlugStoreConfig.class;
	}
}