package com.jspgou.cms.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.BrandDao;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.BrandTextMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ProductTypeMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class BrandMngImpl implements BrandMng {
	@Override
	@Transactional(readOnly = true)
	public List<Brand> getAllList() {
		List<Brand> list = dao.getAllList();
		return list;
	}

	@Override
	public List<Brand> getList(){
		return dao.getList();
	}
	
	@Override
	public List<Brand> getListForTag(Long webId, int firstResult, int maxResults) {
		return dao.getList(webId, firstResult, maxResults, true);
	}
	
	@Override
	public Brand getsiftBrand(){
		return dao.getsiftBrand();
	}

	@Override
	@Transactional(readOnly = true)
	public Brand findById(Long id) {
		Brand entity = dao.findById(id);
		return entity;
	}

	@Override
	public Brand save(Brand bean, String text) {
		Brand entity = dao.save(bean);
		brandTextMng.save(entity, text);
		return entity;
	}

	@Override
	public Brand update(Brand bean, String text) {
		Updater<Brand> updater = new Updater<Brand>(bean);
		Brand entity = dao.updateByUpdater(updater);
		// 先更新BrandText
		entity.getBrandText().setText(text);
		return entity;
	}

	@Override
	public Brand deleteById(Long id) {
		Brand entity = findById(id);
		entity = dao.deleteById(id);
		return entity;
	}

	@Override
	public Brand[] deleteByIds(Long[] ids) {
		Brand[] beans = new Brand[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public Brand[] updatePriority(Long[] ids, Integer[] priority) {
		Brand[] beans = new Brand[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}
	
	@Override
	public boolean brandNameNotExist(String brandName){
		return dao.countByBrandName(brandName) <= 0;
	}

	private ProductTypeMng productTypeMng;
	private BrandTextMng brandTextMng;
	private BrandDao dao;


	@Autowired
	public void setProductTypeMng(ProductTypeMng productTypeMng) {
		this.productTypeMng = productTypeMng;
	}

	@Autowired
	public void setBrandTextMng(BrandTextMng brandTextMng) {
		this.brandTextMng = brandTextMng;
	}

	@Autowired
	public void setDao(BrandDao dao) {
		this.dao = dao;
	}

	@Override
	public Pagination getPage(String name,String brandtype,int pageNo, int pageSize) {
		Pagination page = dao.getPage(name,brandtype,pageNo, pageSize);
		return page;
	}

	@Override
	public List<Brand> getBrandList(String name) {
		
		return dao.getBrandList(name);
	}


}