package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.LogisticsTextDao;
import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.entity.LogisticsText;
import com.jspgou.cms.manager.LogisticsTextMng;
import com.jspgou.common.hibernate3.Updater;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class LogisticsTextMngImpl implements LogisticsTextMng {
	@Override
	public LogisticsText save(Logistics logistics, String text) {
		LogisticsText bean = new LogisticsText();
		bean.setLogistics(logistics);
		bean.setText(text);
		dao.save(bean);
		return bean;
	}

	@Override
	public LogisticsText update(LogisticsText bean) {
		LogisticsText entity = dao.updateByUpdater(new Updater<LogisticsText>(bean));
		return entity;
	}

	private LogisticsTextDao dao;

	@Autowired
	public void setDao(LogisticsTextDao dao) {
		this.dao = dao;
	}
}