package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.ShippingDao;
import com.jspgou.cms.entity.Shipping;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShippingDaoImpl extends HibernateBaseDao<Shipping, Long> implements
		ShippingDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<Shipping> getList(Long webId, boolean all, boolean cacheable) {
		Finder f = Finder.create("from Shipping bean where bean.website.id=:webId");
		f.setParam("webId", webId);
		if (!all) {
			f.append(" and bean.disabled=false");
		}
		f.append(" order by bean.priority");
		f.setCacheable(cacheable);
		return find(f);
	}

	@Override
	public Shipping findById(Long id) {
		Shipping entity = get(id);
		return entity;
	}

	@Override
	public Shipping save(Shipping bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Shipping deleteById(Long id) {
		Shipping entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Shipping> getEntityClass() {
		return Shipping.class;
	}
}