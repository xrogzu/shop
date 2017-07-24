package com.jspgou.cms.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.LogisticsDao;
import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.manager.LogisticsMng;
import com.jspgou.cms.manager.LogisticsTextMng;
import com.jspgou.common.hibernate3.Updater;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class LogisticsMngImpl implements LogisticsMng {
	@Override
	@Transactional(readOnly = true)
	public List<Logistics> getAllList() {
		List<Logistics> list = dao.getAllList();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Logistics findById(Long id) {
		Logistics entity = dao.findById(id);
		return entity;
	}

	@Override
	public Logistics save(Logistics bean, String text) {
		Logistics entity = dao.save(bean);
		logisticsTextMng.save(entity, text);
		return entity;
	}

	@Override
	public Logistics update(Logistics bean, String text) {
		Updater<Logistics> updater = new Updater<Logistics>(bean);
		Logistics entity = dao.updateByUpdater(updater);
		entity.getLogisticsText().setText(text);
		return entity;
	}

	@Override
	public Logistics deleteById(Long id) {
		return dao.deleteById(id);
	}

	@Override
	public Logistics[] deleteByIds(Long[] ids) {
		Logistics[] beans = new Logistics[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public Logistics[] updatePriority(Long[] ids, Integer[] priority) {
		Logistics[] beans = new Logistics[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}
	
	
	private LogisticsTextMng logisticsTextMng;
	private LogisticsDao dao;

	@Autowired
	public void setLogisticsTextMng(LogisticsTextMng logisticsTextMng) {
		this.logisticsTextMng = logisticsTextMng;
	}

	@Autowired
	public void setDao(LogisticsDao dao) {
		this.dao = dao;
	}
}