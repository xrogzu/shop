package com.jspgou.cms.dao;

import com.jspgou.cms.entity.Cart;

/**
* This class should preserve.
* @preserve
*/
public interface CartDao {
	public Cart findById(Long id);

	public Cart saveOrUpdate(Cart bean);

	public Cart update(Cart bean);

	public Cart deleteById(Long id);
	
}