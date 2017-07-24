package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.GatheringDao;
import com.jspgou.cms.entity.Gathering;
import com.jspgou.cms.manager.GatheringMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class GatheringMngImpl implements GatheringMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Gathering findById(Long id) {
		Gathering entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<Gathering> getlist(Long orderId) {
		return dao.getlist(orderId);
	}
	
	@Override
	public void deleteByorderId(Long orderId) {
		List<Gathering> list = getlist(orderId);
		for(Gathering gathering:list){
			deleteById(gathering.getId());
		}
	}

	@Override
	public Gathering save(Gathering bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Gathering update(Gathering bean) {
		Updater<Gathering> updater = new Updater<Gathering>(bean);
		Gathering entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Gathering deleteById(Long id) {
		Gathering bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Gathering[] deleteByIds(Long[] ids) {
		Gathering[] beans = new Gathering[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private GatheringDao dao;

	@Autowired
	public void setDao(GatheringDao dao) {
		this.dao = dao;
	}
}