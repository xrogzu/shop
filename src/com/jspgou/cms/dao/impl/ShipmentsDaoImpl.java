package com.jspgou.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShipmentsDao;
import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.entity.Shipments;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShipmentsDaoImpl extends HibernateBaseDao<Shipments, Long> implements ShipmentsDao {
	@Override
	public Pagination getPage(Boolean isPrint,int pageNo, int pageSize) {
	/*	Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);*/
		Finder f=Finder.create("from Shipments bean where 1=1");
		
		if (isPrint!=null){
			f.append(" and bean.isPrint=:isPrint");
			f.setParam("isPrint", isPrint);
		}
		return find(f, pageNo, pageSize);
		//return page;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Logistics> getAllList() {
		Criteria crit = createCriteria();
		crit.addOrder(Order.asc(Logistics.PROP_PRIORITY));
		List<Logistics> list = crit.list();
		return list;
	}

	@Override
	public List<Shipments> getlist(Long orderId) {
		Finder f = Finder.create("from Shipments bean where bean.indent.id=:id");
        f.setParam("id", orderId);
		return find(f);
	}

	@Override
	public Shipments findById(Long id) {
		Shipments entity = get(id);
		return entity;
	}

	@Override
	public Shipments save(Shipments bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Shipments deleteById(Long id) {
		Shipments entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<Shipments> getEntityClass() {
		return Shipments.class;
	}
}