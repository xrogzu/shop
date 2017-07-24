package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.ExendedItemDao;
import com.jspgou.cms.entity.ExendedItem;
import com.jspgou.cms.manager.ExendedItemMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ExendedItemMngImpl implements ExendedItemMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ExendedItem findById(Long id) {
		ExendedItem entity = dao.findById(id);
		return entity;
	}

	@Override
	public ExendedItem save(ExendedItem item) {
		return dao.save(item);
	}

	@Override
	public ExendedItem update(ExendedItem item) {
		Updater<ExendedItem> updater = new Updater<ExendedItem>(item);
		ExendedItem entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ExendedItem deleteById(Long id) {
		ExendedItem bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ExendedItem[] deleteByIds(Long[] ids) {
		ExendedItem[] beans = new ExendedItem[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ExendedItemDao dao;

	@Autowired
	public void setDao(ExendedItemDao dao) {
		this.dao = dao;
	}
}