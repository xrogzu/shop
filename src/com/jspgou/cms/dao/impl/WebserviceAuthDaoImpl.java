package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.WebserviceAuthDao;
import com.jspgou.cms.entity.WebserviceAuth;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;

@Repository
public class WebserviceAuthDaoImpl extends HibernateBaseDao<WebserviceAuth, Integer> implements WebserviceAuthDao {
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@Override
	public WebserviceAuth findByUsername(String username){
		String hql="from WebserviceAuth bean where bean.username=:username";
		Finder f=Finder.create(hql).setParam("username", username);
		f.setCacheable(true);
		List<WebserviceAuth>list=find(f);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public WebserviceAuth findById(Integer id) {
		WebserviceAuth entity = get(id);
		return entity;
	}

	@Override
	public WebserviceAuth save(WebserviceAuth bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public WebserviceAuth deleteById(Integer id) {
		WebserviceAuth entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<WebserviceAuth> getEntityClass() {
		return WebserviceAuth.class;
	}
}