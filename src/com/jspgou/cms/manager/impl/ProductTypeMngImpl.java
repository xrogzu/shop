package com.jspgou.cms.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ProductTypeDao;
import com.jspgou.cms.entity.ProductType;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.ProductTypeMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductTypeMngImpl implements ProductTypeMng {
	@Override
	@Transactional(readOnly = true)
	public List<ProductType> getList(Long webId) {
		return dao.getList(webId);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductType findById(Long id) {
		ProductType entity = dao.findById(id);
		return entity;
	}

	@Override
	public ProductType save(ProductType bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ProductType update(ProductType bean) {
		ProductType entity = dao.updateByUpdater(new Updater<ProductType>(bean));
		return entity;
	}

	@Override
	public ProductType deleteById(Long id) {
		ProductType entity = dao.deleteById(id);
		return entity;
	}

	@Override
	public ProductType[] deleteByIds(Long[] ids) {
		ProductType[] beans = new ProductType[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BrandMng brandMng;
	private ProductTypeDao dao;

	@Autowired
	public void setBrandMng(BrandMng brandMng) {
		this.brandMng = brandMng;
	}

	@Autowired
	public void setDao(ProductTypeDao dao) {
		this.dao = dao;
	}
}