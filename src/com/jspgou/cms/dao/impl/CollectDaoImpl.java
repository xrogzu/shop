package com.jspgou.cms.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.CollectDao;
import com.jspgou.cms.entity.Collect;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class CollectDaoImpl extends HibernateBaseDao<Collect, Integer> implements
		CollectDao {

	@Override
	public Pagination getList(Integer pageSize,Integer pageNo,Long memberId){
		String hql="from Collect bean where 1=1 and bean.member.id=:id";
		Finder f = Finder.create(hql).setParam("id", memberId);
		return this.find(f, pageNo, pageSize);
	}	

	@Override
	public Collect findById(Integer id) {
		Collect entity = get(id);
		return entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Collect> findByProductId(Long productId){//通过productId查收藏
		Finder f = Finder.create("from Collect bean where bean.product.id=:id").setParam("id", productId);
		return find(f);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collect findByProductFashionId(Long id) {//通过fashid查收藏
		Iterator<Collect> list=this.getSession().createQuery("from Collect bean where bean.fashion.id=:id").setParameter("id", id).iterate();
		if(list.hasNext()){
			return list.next();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Collect> getList(Long memberId, int firstResult, int maxResults){
		Finder f = Finder.create("from Collect bean");
		f.append(" where bean.user.id=:memberId").setParam("memberId", memberId);
		f.setCacheable(true);
		f.setFirstResult(firstResult);
		f.setMaxResults(maxResults);
		return find(f);
	}
	
	@SuppressWarnings("unchecked")
	public List<Collect> getList(Long productId,Long memberId){
		Finder f = Finder.create("select bean from Collect bean where 1=1");
		f.append(" and bean.user.id=:memberId").setParam("memberId", memberId);
		f.append(" and bean.product.id=:productId").setParam("productId", productId);
		return find(f);
	}

	@Override
	public Collect save(Collect bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Collect deleteById(Integer id) {
		Collect entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Collect> getEntityClass() {
		return Collect.class;
	}
}