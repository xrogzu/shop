package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.CustomerServiceDao;
import com.jspgou.cms.entity.CustomerService;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class CustomerServiceDaoImpl extends HibernateBaseDao<CustomerService, Long> 
    implements CustomerServiceDao{

	@Override
	public CustomerService findById(Long id) {
		CustomerService entity = get(id);
		return entity;
	}

	@Override
	public Pagination getPagination(Boolean disable,int pageNo, int pageSize){
		Finder f = Finder.create("from CustomerService bean where 1=1");
		if(disable!=null){
			f.append(" and bean.disable=:disable").setParam("disable", disable);
		}
		f.append(" order by bean.priority");
		return find(f, pageNo, pageSize);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CustomerService> getList(Boolean disable){
		Finder f = Finder.create("from CustomerService bean where 1=1");
		
		if(disable!=null){
			f.append(" and bean.disable=:disable").setParam("disable", disable);
		}
		f.append(" order by bean.priority");
		return find(f);
	}

	@Override
	public CustomerService save(CustomerService bean) {
		getSession().save(bean);
		return bean;
	}
	
	@Override
	public CustomerService deleteById(Long id) {
		CustomerService entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	
	

	@Override
	protected Class<CustomerService> getEntityClass() {
		return CustomerService.class;
	}

}

