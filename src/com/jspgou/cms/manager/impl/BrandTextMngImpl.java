package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.BrandTextDao;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.BrandText;
import com.jspgou.cms.manager.BrandTextMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class BrandTextMngImpl implements BrandTextMng {
	@Override
	public BrandText save(Brand brand, String text) {
		BrandText bean = new BrandText();
		bean.setBrand(brand);
		bean.setText(text);
		dao.save(bean);
		return bean;
	}

	@Override
	public BrandText update(BrandText bean) {
		BrandText entity = dao.updateByUpdater(new Updater<BrandText>(bean));
		return entity;
	}

	private BrandTextDao dao;

	@Autowired
	public void setDao(BrandTextDao dao) {
		this.dao = dao;
	}
}