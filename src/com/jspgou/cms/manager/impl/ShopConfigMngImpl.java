package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ShopConfigDao;
import com.jspgou.cms.entity.ShopConfig;
import com.jspgou.cms.manager.ShopConfigMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopConfigMngImpl implements ShopConfigMng {
	@Override
	@Transactional(readOnly = true)
	public ShopConfig findById(Long id) {
		ShopConfig entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopConfig save(ShopConfig bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopConfig update(ShopConfig bean) {
		Updater<ShopConfig> updater = new Updater<ShopConfig>(bean);
		ShopConfig entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopConfig deleteById(Long id) {
		ShopConfig bean = dao.deleteById(id);
		return bean;
	}

	private ShopConfigDao dao;

	@Autowired
	public void setDao(ShopConfigDao dao) {
		this.dao = dao;
	}
}