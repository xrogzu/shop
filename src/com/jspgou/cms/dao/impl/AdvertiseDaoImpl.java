package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.AdvertiseDao;
import com.jspgou.cms.entity.Advertise;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class AdvertiseDaoImpl extends
		HibernateBaseDao<Advertise, Integer> implements AdvertiseDao {
	@Override
	public Pagination getPage( Integer adspaceId,
			Boolean enabled, int pageNo, int pageSize) {
		Finder f = Finder.create("from Advertise bean where 1=1");
		if (adspaceId != null) {
			f.append(" and bean.adspace.id=:adspaceId");
			f.setParam("adspaceId", adspaceId);
		}
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Advertise> getList(Integer adspaceId, Boolean enabled) {
		Finder f = Finder.create("from Advertise bean where 1=1");
		if (adspaceId != null) {
			f.append(" and bean.adspace.id=:adspaceId");
			f.setParam("adspaceId", adspaceId);
		}
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		return find(f);
	}

	@Override
	public Advertise findById(Integer id) {
		Advertise entity = get(id);
		return entity;
	}

	@Override
	public Advertise save(Advertise bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Advertise deleteById(Integer id) {
		Advertise entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Advertise> getEntityClass() {
		return Advertise.class;
	}
}