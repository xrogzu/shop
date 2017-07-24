package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopDictionaryDao;
import com.jspgou.cms.entity.ShopDictionary;
import com.jspgou.cms.manager.ShopDictionaryMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopDictionaryMngImpl implements ShopDictionaryMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public Pagination getPage(String name,Long typeId,int pageNo, int pageSize){
		Pagination page = dao.getPage(name,typeId,pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopDictionary findById(Long id) {
		ShopDictionary entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<ShopDictionary> getListByType(Long typeId){
	     return dao.getListByType(typeId);
	}


	@Override
	public ShopDictionary save(ShopDictionary bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopDictionary update(ShopDictionary bean) {
		Updater<ShopDictionary> updater = new Updater<ShopDictionary>(bean);
		ShopDictionary entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopDictionary deleteById(Long id) {
		ShopDictionary bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public ShopDictionary[] deleteByIds(Long[] ids) {
		ShopDictionary[] beans = new ShopDictionary[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public ShopDictionary[] updatePriority(Long[] ids, Integer[] priority){
		int len = ids.length;
		ShopDictionary[] beans = new ShopDictionary[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	private ShopDictionaryDao dao;

	@Autowired
	public void setDao(ShopDictionaryDao dao) {
		this.dao = dao;
	}
}