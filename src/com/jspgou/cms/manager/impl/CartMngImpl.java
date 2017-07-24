package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.CartDao;
import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.manager.CartMng;
import com.jspgou.cms.manager.GiftMng;
import com.jspgou.cms.manager.PopularityGroupMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.core.manager.MemberMng;
import com.jspgou.core.manager.WebsiteMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class CartMngImpl implements CartMng {
	@Override
	@Transactional(readOnly = true)
	public Cart findById(Long id) {
		Cart entity = dao.findById(id);
		return entity;
	}
	
	//购物车
	//（增加了商品,收藏加入购物车）
	@Override
	public Cart collectAddItem(Product product,Long fashId,Long popularityId,int count,boolean isAdd,Long memberId, Long webId){
		Cart cart = findById(memberId);
		if (cart == null) {
			cart = new Cart();
			cart.setMember(memberMng.findById(memberId));
			cart.setWebsite(websiteMng.findById(webId));
			cart.init();
		}
		if(fashId!=null){
			ProductFashion productFashion=fashionMng.findById(fashId);
			cart.addItem(productFashion.getProductId(),productFashion,popularityGroupMng.findById(popularityId), count, isAdd);
		}else{
			cart.addItem(product,null,popularityGroupMng.findById(popularityId),count,isAdd);
		}
		cart=dao.saveOrUpdate(cart);
		return cart;
	}
	
	@Override
	public Cart addGift(Long giftId, int count, boolean isAdd,
			Long memberId, Long webId) {
		Cart cart = findById(memberId);
		if (cart == null) {
			cart = new Cart();
			cart.setMember(memberMng.findById(memberId));
			cart.setWebsite(websiteMng.findById(webId));
		}
		cart.addGift(giftMng.findById(giftId), count, isAdd);
		dao.saveOrUpdate(cart);
		return cart;
	}

	@Override
	public Cart update(Cart cart) {
		return dao.update(cart);
	}

	@Override
	public Cart deleteById(Long id) {
		Cart bean = dao.deleteById(id);
		return bean;
	}
	
	@Autowired
	private PopularityGroupMng popularityGroupMng;
	@Autowired
	private CartDao dao;
	@Autowired
	private GiftMng giftMng;
	@Autowired
	private MemberMng memberMng;
	@Autowired
	private ProductMng productMng;
	@Autowired
	private WebsiteMng websiteMng;
	@Autowired
	private ProductFashionMng fashionMng;
}