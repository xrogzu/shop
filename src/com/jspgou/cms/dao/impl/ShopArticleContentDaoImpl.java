package com.jspgou.cms.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopArticleContentDao;
import com.jspgou.cms.entity.ShopArticleContent;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShopArticleContentDaoImpl extends HibernateBaseDao<ShopArticleContent, Long> 
    implements ShopArticleContentDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	@Override
	public ShopArticleContent findById(Long id) {
		ShopArticleContent entity = get(id);
		return entity;
	}

	@Override
	public ShopArticleContent save(ShopArticleContent bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ShopArticleContent deleteById(Long id) {
		ShopArticleContent entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ShopArticleContent> getEntityClass() {
		return ShopArticleContent.class;
	}
}