package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.WebserviceDao;
import com.jspgou.cms.entity.Webservice;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

@Repository
public class WebserviceDaoImpl extends HibernateBaseDao<Webservice, Integer> implements
		WebserviceDao {

	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		String hql="from Webservice bean";
		Finder f=Finder.create(hql);
		return find(f,pageNo,pageSize);
	}

	@Override
	public List<Webservice> getList(String type) {
		String hql="from Webservice bean where bean.type=:type";
		Finder f=Finder.create(hql).setParam("type", type);
		f.setCacheable(true);
		return find(f);
	}

	@Override
	public Webservice findById(Integer id) {
		Webservice entity=get(id);
		return entity;
	}

	@Override
	public Webservice save(Webservice bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Webservice deleteById(Integer id) {
		Webservice entity=super.get(id);
		if(entity!=null){
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Webservice> getEntityClass() {
		return Webservice.class;
	}

}
