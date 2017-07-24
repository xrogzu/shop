package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ShopMemberAddressDao;
import com.jspgou.cms.entity.ShopMemberAddress;
import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.manager.CartMng;
import com.jspgou.cms.manager.ShopMemberAddressMng;
import com.jspgou.cms.manager.ShopMemberMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopMemberAddressMngImpl implements ShopMemberAddressMng {
	@Override
	@Transactional(readOnly = true)
	public List<ShopMemberAddress> getList(Long memberId) {
		return dao.getList(memberId);
	}
	
	@Override
	public ShopMemberAddress findByMemberDefault(Long memberId,Boolean isDefault){
		List<ShopMemberAddress> list=dao.findByMemberDefault(memberId,isDefault);
		if(list.size()>=1){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public List<ShopMemberAddress> findByMemberId(Long memberId){
		return dao.findByMemberDefault(memberId,null);
	}

	@Override
	@Transactional(readOnly = true)
	public ShopMemberAddress findById(Long id) {
		ShopMemberAddress entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopMemberAddress save(ShopMemberAddress bean) {
		return dao.save(bean);
	}

	@Override
	public ShopMemberAddress updateByUpdater(ShopMemberAddress bean) {
		 Updater<ShopMemberAddress> updater = new Updater<ShopMemberAddress>(bean);
			return dao.updateByUpdater(updater);
	}

	@Override
	public ShopMemberAddress deleteById(Long id, Long memberId) {
		// 删除购物车中的收货地址
		Cart cart = cartMng.findById(memberId);
//		ShopMemberAddress ca = cart.getAddress();
//		if (ca != null && ca.getId().equals(id)) {
//			cart.setAddress(null);
//		}
		ShopMemberAddress bean = dao.deleteById(id);
		return bean;
	}

	private ShopMemberAddressDao dao;
	private ShopMemberMng shopMemberMng;
	private CartMng cartMng;

	@Autowired
	public void setDao(ShopMemberAddressDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setShopMemberMng(ShopMemberMng shopMemberMng) {
		this.shopMemberMng = shopMemberMng;
	}

	@Autowired
	public void setCartMng(CartMng cartMng) {
		this.cartMng = cartMng;
	}
}