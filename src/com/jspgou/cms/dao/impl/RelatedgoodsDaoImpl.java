package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.RelatedgoodsDao;
import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class RelatedgoodsDaoImpl extends HibernateBaseDao<Relatedgoods, Long> implements RelatedgoodsDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<Relatedgoods> findByIdProductId(Long productId){
		Finder f=Finder.create("from Relatedgoods bean where 1=1");
		if(productId!=null){
			f.append(" and bean.productId=:productId");
			f.setParam("productId", productId);
		}
		return find(f);
	}

	@Override
	public Relatedgoods findById(Long id) {
		Relatedgoods entity = get(id);
		return entity;
	}

	@Override
	public Relatedgoods save(Relatedgoods bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Relatedgoods deleteById(Long id) {
		Relatedgoods entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<Relatedgoods> getEntityClass() {
		return Relatedgoods.class;
	}
}