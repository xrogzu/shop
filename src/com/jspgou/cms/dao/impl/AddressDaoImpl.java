package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.AddressDao;
import com.jspgou.cms.entity.Address;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class AddressDaoImpl extends HibernateBaseDao<Address, Long> implements AddressDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<Address> getListById(Long parentId){
		Finder f=Finder.create("from Address bean where 1=1 ");
		if(parentId==null){
			f.append(" and bean.parent.id is null");
		}else{
			f.append(" and bean.parent.id=:parentId");
			f.setParam("parentId", parentId);
		}
		return find(f);
	}
	
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@Override
	public Pagination getPageByParentId(Long parentId,int pageNo, int pageSize){
		Finder f=Finder.create("from Address bean where 1=1 ");
		if(parentId!=null){
			f.append(" and bean.parent.id=:id");
			f.setParam("id", parentId);
		}else{
			f.append(" and bean.parent.id is null");
		}
		f.append(" order by bean.priority");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Address findById(Long id) {
		Address entity = get(id);
		return entity;
	}

	@Override
	public Address save(Address bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Address deleteById(Long id) {
		Address entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<Address> getEntityClass() {
		return Address.class;
	}
}