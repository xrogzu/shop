package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.ProductStandardDao;
import com.jspgou.cms.entity.ProductStandard;
import com.jspgou.cms.manager.ProductStandardMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductStandardMngImpl implements ProductStandardMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ProductStandard findById(Long id) {
		ProductStandard entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public ProductStandard findByProductIdAndStandardId(Long productId,Long standardId){
		List<ProductStandard> list=dao.findByProductIdAndStandardId(productId, standardId);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public List<ProductStandard> findByProductId(Long productId){
		return dao.findByProductId(productId);
	}

	@Override
	public ProductStandard save(ProductStandard bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ProductStandard update(ProductStandard bean) {
		Updater<ProductStandard> updater = new Updater<ProductStandard>(bean);
		ProductStandard entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ProductStandard deleteById(Long id) {
		ProductStandard bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public ProductStandard[] deleteByIds(Long[] ids) {
		ProductStandard[] beans = new ProductStandard[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ProductStandardDao dao;

	@Autowired
	public void setDao(ProductStandardDao dao) {
		this.dao = dao;
	}
}