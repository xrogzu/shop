package com.jspgou.plug.store.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.security.encoder.PwdEncoder;
import com.jspgou.plug.store.dao.PlugStoreConfigDao;
import com.jspgou.plug.store.entity.PlugStoreConfig;
import com.jspgou.plug.store.manager.PlugStoreConfigMng;


@Service
@Transactional
public class PlugStoreConfigMngImpl implements PlugStoreConfigMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public PlugStoreConfig findById(Integer id) {
		return dao.findById(id);
	}
	
	@Transactional(readOnly = true)
	public PlugStoreConfig getDefault(){
		return findById(1);
	}

	public PlugStoreConfig save(PlugStoreConfig bean) {
		dao.save(bean);
		return bean;
	}

	public PlugStoreConfig update(PlugStoreConfig bean,String password) {
		Updater<PlugStoreConfig> updater = new Updater<PlugStoreConfig>(bean);
		if (StringUtils.isNotBlank(password)) {
			bean.setPassword(pwdEncoder.encodePassword(password));
		}else{
			updater.exclude("password");
		}
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public PlugStoreConfig deleteById(Integer id) {
		PlugStoreConfig bean = dao.deleteById(id);
		return bean;
	}
	
	public PlugStoreConfig[] deleteByIds(Integer[] ids) {
		PlugStoreConfig[] beans = new PlugStoreConfig[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Autowired
	private PlugStoreConfigDao dao;
	@Autowired
	private PwdEncoder pwdEncoder;
}