package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.CartItemDao;
import com.jspgou.cms.entity.CartItem;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class CartItemDaoImpl extends HibernateBaseDao<CartItem, Long> implements CartItemDao {
	
	@Override
	public CartItem findById(Long id) {
		CartItem entity = get(id);
		return entity;
	}
	
	@Override
	public CartItem deleteById(Long id) {
		CartItem entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	public List<CartItem> getlist(Long cartId,Long popularityGroupId) {//会员中心购买记录
		Finder f = Finder.create("select bean from CartItem bean");
		f.append(" where bean.cart.id=:cartId and bean.popularityGroup.id=:popularityGroupId");
		f.setParam("cartId", cartId).setParam("popularityGroupId",popularityGroupId);
		f.append(" order by bean.id desc");
		return find(f);
	}
	
	@Override
	public int deleteByProductId(Long productId) {
		String hql = " delete CartItem bean where bean.product.id=:productId";
		return getSession().createQuery(hql).setParameter("productId", productId).executeUpdate();
	}
	
	@Override
	public int deleteByProductFactionId(Long productFacId) {
		String hql = " delete CartItem bean where bean.productFash.id=:productFash";
		return getSession().createQuery(hql).setParameter("productFash", productFacId).executeUpdate();
	}
	
	@Override
	protected Class<CartItem> getEntityClass() {
		return CartItem.class;
	}
}

