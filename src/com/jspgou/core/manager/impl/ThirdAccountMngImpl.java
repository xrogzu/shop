package com.jspgou.core.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.ThirdAccount;
import com.jspgou.core.manager.ThirdAccountMng;
import com.jspgou.core.dao.ThirdAccountDao;



@Service
@Transactional
public class ThirdAccountMngImpl implements ThirdAccountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(String username,String source,int pageNo, int pageSize) {
		Pagination page = dao.getPage(username,source,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ThirdAccount findById(Long id) {
		ThirdAccount entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ThirdAccount findByKey(String key){
		return dao.findByKey(key);
	}

	public ThirdAccount save(ThirdAccount bean) {
		dao.save(bean);
		return bean;
	}

	public ThirdAccount update(ThirdAccount bean) {
		Updater<ThirdAccount> updater = new Updater<ThirdAccount>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ThirdAccount deleteById(Long id) {
		ThirdAccount bean = dao.deleteById(id);
		return bean;
	}
	
	public ThirdAccount[] deleteByIds(Long[] ids) {
		ThirdAccount[] beans = new ThirdAccount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ThirdAccountDao dao;

	@Autowired
	public void setDao(ThirdAccountDao dao) {
		this.dao = dao;
	}
}