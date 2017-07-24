package com.jspgou.cms.dao.impl;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.CartDao;
import com.jspgou.cms.entity.Cart;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class CartDaoImpl extends HibernateBaseDao<Cart, Long> implements
		CartDao {
	@Override
	public Cart findById(Long id) {
		Cart entity = get(id);
		return entity;
	}

	@Override
	public Cart saveOrUpdate(Cart bean) {
		getSession().saveOrUpdate(bean);
		return bean;
	}

	@Override
	public Cart update(Cart bean) {
		getSession().update(bean);
		return bean;
	}

	@Override
	public Cart deleteById(Long id) {
		Cart entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Cart> getEntityClass() {
		return Cart.class;
	}
}