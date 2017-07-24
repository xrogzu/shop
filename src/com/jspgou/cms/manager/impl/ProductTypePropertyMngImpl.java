package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ProductTypePropertyDao;
import com.jspgou.cms.entity.ProductTypeProperty;
import com.jspgou.cms.manager.ProductTypePropertyMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductTypePropertyMngImpl implements ProductTypePropertyMng {

	@Autowired
	private ProductTypePropertyDao dao;
	
	@Override
	public ProductTypeProperty deleteById(Long id) {
		return dao.deleteById(id);
	}
	
	@Override
	public ProductTypeProperty[] deleteByIds(Long[] ids) {
		ProductTypeProperty[] beans = new ProductTypeProperty[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public ProductTypeProperty findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Pagination getList(int pageNo,int pageSize,Long typeid,
		Boolean isCategory,String searchType,String searchContent) {
		return dao.getList(pageNo,pageSize,typeid,isCategory,searchType,searchContent);
	}
	
	@Override
	public List<ProductTypeProperty> getList(Long typeId,Boolean isCategory){
		return dao.getList(typeId, isCategory);
	}
	
	@Override
	public void saveList(List<ProductTypeProperty> list) {
		for (ProductTypeProperty item : list) {
			save(item);
		}
	}
	
	@Override
	public ProductTypeProperty save(ProductTypeProperty bean) {
		return dao.save(bean);
	}
	
	@Override
	public void updatePriority(Long[] wids, Integer[] sort,
			String[] propertyName, Boolean[] single) {
		ProductTypeProperty item;
		for (int i = 0, len = wids.length; i < len; i++) {
			item = findById(wids[i]);
			item.setPropertyName(propertyName[i]);
			item.setSort(sort[i]);
			item.setSingle(single[i]);
		}
	}

	@Override
	public List<ProductTypeProperty> findBySearch(String searchType,
			String searchContent) {
		return dao.findBySearch(searchType, searchContent);
	}

	@Override
	public List<ProductTypeProperty> findListByTypeId(Long typeid) {
		return dao.findListByTypeId(typeid);
	}
	
	@Override
	public List<ProductTypeProperty> getList(Long typeId,boolean isCategory){
		return dao.getList(typeId, isCategory);
	}

	@Override
	public ProductTypeProperty update(ProductTypeProperty bean) {
		Updater<ProductTypeProperty> updater = new Updater<ProductTypeProperty>(bean);
		ProductTypeProperty entity = dao.updateByUpdater(updater);
		return entity;
	}

}

