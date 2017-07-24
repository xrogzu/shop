package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.AdspaceDao;
import com.jspgou.cms.entity.Adspace;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class AdspaceDaoImpl extends HibernateBaseDao<Adspace, Integer>
		implements AdspaceDao {
	
	@Override
	public Adspace findById(Integer id) {
		Adspace entity = get(id);
		return entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Adspace> getList(){
	return	this.getSession().createQuery("from Adspace bean ").list();
	}

	@Override
	public Adspace save(Adspace bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Adspace deleteById(Integer id) {
		Adspace entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Adspace> getEntityClass() {
		return Adspace.class;
	}

}