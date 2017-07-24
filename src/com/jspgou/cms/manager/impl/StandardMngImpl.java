package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.StandardDao;
import com.jspgou.cms.entity.Standard;
import com.jspgou.cms.manager.StandardMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class StandardMngImpl implements StandardMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Standard findById(Long id) {
		Standard entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<Standard> findByTypeId(Long typeId) {
		return dao.findByTypeId(typeId);
	}

	@Override
	public Standard save(Standard bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Standard update(Standard bean) {
		Updater<Standard> updater = new Updater<Standard>(bean);
		Standard entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Standard deleteById(Long id) {
		Standard bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Standard[] deleteByIds(Long[] ids) {
		Standard[] beans = new Standard[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public Standard[] updatePriority(Long[] ids, Integer[] priority){
		int len = ids.length;
		Standard[] beans = new Standard[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
		}
		return beans;
	}

	private StandardDao dao;

	@Autowired
	public void setDao(StandardDao dao) {
		this.dao = dao;
	}
}