package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LazyInitializationException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.BrandDao;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.base.BaseBrand;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class BrandDaoImpl extends HibernateBaseDao<Brand, Long> implements
		BrandDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<Brand> getAllList() {
		Criteria crit = createCriteria();
		crit.addOrder(Order.asc(BaseBrand.PROP_PRIORITY));
		List<Brand> list = crit.list();
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Brand> getList(Long webId, int firstResult, int maxResults,
			boolean cacheable) {
		Finder f = Finder.create("select bean from Brand bean where bean.website.id=:webId order by bean.priority");
		f.setParam("webId", webId);
		f.setFirstResult(firstResult);
		f.setMaxResults(maxResults);
		f.setCacheable(cacheable);
		return find(f);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Brand> getList() {
		Finder f = Finder.create("from Brand bean where bean.disabled=false");
		f.append(" order by bean.priority");
		return find(f);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Brand> getListByCate(Long categoryId){//获得某类型的品牌
		Finder f = Finder.create("select bean from Brand bean");
		if(categoryId!=null){
			f.append(" join bean.categorys cate where cate.id=:categoryId");
			f.setParam("categoryId",categoryId);
		}
		return find(f);
	}
	
	@Override
	public Brand getsiftBrand(){//获得精选品牌
		return (Brand)this.getSession().createQuery("from Brand bean where bean.sift=true order by bean.id desc")
		.setMaxResults(1).uniqueResult();
	}

	@Override
	public Brand findById(Long id) {
		Brand entity = get(id);
		return entity;
	}

	@Override
	public Brand save(Brand bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Brand deleteById(Long id) {
		Brand entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}			
		return entity;
	}
	
	@Override
	public int countByBrandName(String brandName){
		String hql = "select count(*) from Brand bean where bean.name=:brandName";
		Query query = getSession().createQuery(hql);
		query.setParameter("brandName", brandName);
		return ((Number) query.iterate().next()).intValue();
	}

	@Override
	protected Class<Brand> getEntityClass() {
		return Brand.class;
	}

	@Override
	public Pagination getPage(String name,String brandtype,int pageNo, int pageSize) {
		Finder f = Finder.create("from Brand bean where 1=1");
		if(name!=null){
			f.append(" and bean.name like :name");
			f.setParam("name", "%"+name+"%");
		}
		if(brandtype != null){
			f.append(" and bean.brandtype like :brandtype");
			f.setParam("brandtype", "%"+brandtype+"%");
		}
		f.append(" order by bean.priority asc, bean.id asc");
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}

	@Override
	public List<Brand> getBrandList(String name) {
		Finder f = Finder.create("from Brand bean where 1=1");
		if(name!=null){
			f.append(" and bean.name like :name");
			f.setParam("name", "%"+name+"%");
		}
		return find(f);
	}
}