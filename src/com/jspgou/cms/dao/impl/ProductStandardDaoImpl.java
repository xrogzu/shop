package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.ProductStandardDao;
import com.jspgou.cms.entity.ProductStandard;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ProductStandardDaoImpl extends HibernateBaseDao<ProductStandard, Long> implements ProductStandardDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	@Override
	public ProductStandard findById(Long id) {
		ProductStandard entity = get(id);
		return entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductStandard> findByProductIdAndStandardId(Long productId,Long standardId){
		Finder f=Finder.create("from ProductStandard bean where 1=1");
		f.append(" and bean.product.id=:productId").setParam("productId", productId);
		f.append(" and bean.standard.id=:standardId").setParam("standardId", standardId);
		return find(f);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductStandard> findByProductId(Long productId){
		Finder f=Finder.create("from ProductStandard bean where 1=1");
		f.append(" and bean.product.id=:productId").setParam("productId", productId);
		return find(f);
	}

	@Override
	public ProductStandard save(ProductStandard bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ProductStandard deleteById(Long id) {
		ProductStandard entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ProductStandard> getEntityClass() {
		return ProductStandard.class;
	}
}