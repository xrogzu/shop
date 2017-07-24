package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.ExendedItemDao;
import com.jspgou.cms.entity.ExendedItem;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ExendedItemDaoImpl extends HibernateBaseDao<ExendedItem, Long>
		implements ExendedItemDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	@Override
	public ExendedItem findById(Long id) {
		ExendedItem entity = get(id);
		return entity;
	}

	@Override
	public ExendedItem save(ExendedItem bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ExendedItem deleteById(Long id) {
		ExendedItem entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ExendedItem> getEntityClass() {
		return ExendedItem.class;
	}
}