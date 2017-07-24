package com.jspgou.cms.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.PosterDao;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.Poster;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class PosterDaoImpl extends HibernateBaseDao<Poster, Integer> implements
		PosterDao {
	@Override
	public Poster findById(Integer id) {
		Poster entity = get(id);
		return entity;
	}

	@Override
	public Poster saveOrUpdate(Poster bean) {
		getSession().saveOrUpdate(bean);
		return bean;
	}

	@Override
	public Poster update(Poster bean) {
		getSession().update(bean);
		return bean;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Poster> getPage(){
		return this.getSession().createQuery("from Poster bean order by bean.time desc").list();
	}

	@Override
	public Poster deleteById(Integer id) {
		Poster entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Poster> getEntityClass() {
		return Poster.class;
	}
}