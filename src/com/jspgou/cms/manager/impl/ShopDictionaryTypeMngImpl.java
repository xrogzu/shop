package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopDictionaryTypeDao;
import com.jspgou.cms.entity.ShopDictionaryType;
import com.jspgou.cms.manager.ShopDictionaryTypeMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopDictionaryTypeMngImpl implements ShopDictionaryTypeMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopDictionaryType findById(Long id) {
		ShopDictionaryType entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<ShopDictionaryType> findAll(){
		return dao.findAll();
	}

	@Override
	public ShopDictionaryType save(ShopDictionaryType bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopDictionaryType update(ShopDictionaryType bean) {
		Updater<ShopDictionaryType> updater = new Updater<ShopDictionaryType>(bean);
		ShopDictionaryType entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopDictionaryType deleteById(Long id) {
		ShopDictionaryType bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public ShopDictionaryType[] deleteByIds(Long[] ids) {
		ShopDictionaryType[] beans = new ShopDictionaryType[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopDictionaryTypeDao dao;

	@Autowired
	public void setDao(ShopDictionaryTypeDao dao) {
		this.dao = dao;
	}
}