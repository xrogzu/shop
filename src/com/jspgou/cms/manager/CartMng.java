package com.jspgou.cms.manager;

import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.Product;

/**
* This class should preserve.
* @preserve
*/
public interface CartMng {
	public Cart findById(Long id);

	public Cart update(Cart cart);

	public Cart deleteById(Long id);
	
	public Cart addGift(Long giftId, int count, boolean isAdd,
			Long memberId, Long webId);
	

	//isAdd是否可以叠加
	public Cart collectAddItem(Product product,Long productFashId,Long popularityId,int count,boolean isAdd,Long memberId, Long webId);
}