package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.ShopPlugDao;
import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.cms.manager.ShopPlugMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
@Service
@Transactional
public class ShopPlugMngImpl implements ShopPlugMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	public List<ShopPlug> getList(String author,Boolean used){
		return dao.getList(author,used);
	}

	@Transactional(readOnly = true)
	public ShopPlug findById(Long id) {
		ShopPlug entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ShopPlug findByPath(String plugPath){
		return dao.findByPath(plugPath);
	}

	public ShopPlug save(ShopPlug bean) {
		dao.save(bean);
		return bean;
	}

	public ShopPlug update(ShopPlug bean) {
		Updater<ShopPlug> updater = new Updater<ShopPlug>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	@Override
	public ShopPlug deleteById(Long id) {
		ShopPlug bean = dao.deleteById(id);
		return bean;
	}
	@Override
	public ShopPlug[] deleteByIds(Long[] ids) {
		ShopPlug[] beans = new ShopPlug[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	

	private ShopPlugDao dao;

	@Autowired
	public void setDao(ShopPlugDao dao) {
		this.dao = dao;
	}
}
