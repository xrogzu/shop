package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.StandardTypeDao;
import com.jspgou.cms.entity.StandardType;
import com.jspgou.cms.manager.StandardTypeMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class StandardTypeMngImpl implements StandardTypeMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public StandardType getByField(String field){
		return dao.getByField(field);
	}

	@Override
	@Transactional(readOnly = true)
	public StandardType findById(Long id) {
		StandardType entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<StandardType> getList(){
		return dao.getList();
	}
	
	@Override
	public List<StandardType> getList(Long categoryId){
		return dao.getList(categoryId);
	}

	@Override
	public StandardType save(StandardType bean) {
		bean=dao.save(bean);
		return bean;
	}

	@Override
	public StandardType update(StandardType bean) {
		Updater<StandardType> updater = new Updater<StandardType>(bean);
		StandardType entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public StandardType deleteById(Long id) {
		StandardType bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public StandardType[] deleteByIds(Long[] ids) {
		StandardType[] beans = new StandardType[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public StandardType[] updatePriority(Long[] wids, Integer[] priority){
		int len = wids.length;
		StandardType[] beans = new StandardType[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(wids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	private StandardTypeDao dao;

	@Autowired
	public void setDao(StandardTypeDao dao) {
		this.dao = dao;
	}
}