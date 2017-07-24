package com.jspgou.cms.manager;

import java.util.List;

import com.jspgou.cms.entity.CartItem;

/**
* This class should preserve.
* @preserve
*/
public interface CartItemMng {
	
	public List<CartItem> getlist(Long cartId,Long popularityGroupId);
	
	public CartItem findById(Long id);
	
	public CartItem updateByUpdater(CartItem updater);
	
	public CartItem deleteById(Long id);
	
	public CartItem[] deleteByIds(Long[] ids);
	
	public int deleteByProductId(Long productId);
	
	public int deleteByProductFactionId(Long productFacId);
}

