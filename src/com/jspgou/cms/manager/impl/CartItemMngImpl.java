package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.cms.dao.CartItemDao;
import com.jspgou.cms.entity.CartItem;
import com.jspgou.cms.manager.CartItemMng;
import com.jspgou.common.hibernate3.Updater;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class CartItemMngImpl implements CartItemMng {
	
	@Override
	@Transactional(readOnly = true)
	public CartItem findById(Long id) {
		CartItem entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<CartItem> getlist(Long cartId,Long popularityGroupId){
		return dao.getlist(cartId, popularityGroupId);
	}
	
	@Override
	public CartItem updateByUpdater(CartItem bean){
		Updater<CartItem> updater = new Updater<CartItem>(bean);
		return dao.updateByUpdater(updater);
	}
	
	@Override
	public CartItem deleteById(Long id) {
		CartItem bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public CartItem[] deleteByIds(Long[] ids) {
		CartItem[] beans = new CartItem[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public int deleteByProductId(Long productId) {
		return dao.deleteByProductId(productId);
	}
	
	@Override
	public int deleteByProductFactionId(Long productFacId){
		return dao.deleteByProductFactionId(productFacId);
	}
	
	@Autowired
	private CartItemDao dao;
}

