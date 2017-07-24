package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ProductTextDao;
import com.jspgou.cms.entity.ProductText;
import com.jspgou.cms.manager.ProductTextMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductTextMngImpl implements ProductTextMng {
	@Override
	public ProductText update(ProductText bean) {
		Updater<ProductText> updater = new Updater<ProductText>(bean);
		ProductText entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ProductText save(ProductText bean) {
		return dao.save(bean);
	}

	private ProductTextDao dao;

	@Autowired
	public void setDao(ProductTextDao dao) {
		this.dao = dao;
	}
}