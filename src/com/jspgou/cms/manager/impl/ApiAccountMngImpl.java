package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiAccountDao;
import com.jspgou.cms.entity.ApiAccount;
import com.jspgou.cms.manager.ApiAccountMng;

@Service
@Transactional
public class ApiAccountMngImpl implements ApiAccountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiAccount findById(Long id) {
		ApiAccount entity = dao.findById(id);
		return entity;
	}

	public ApiAccount save(ApiAccount bean) {
		dao.save(bean);
		return bean;
	}

	public ApiAccount update(ApiAccount bean) {
		Updater<ApiAccount> updater = new Updater<ApiAccount>(bean);
		ApiAccount entity = dao.updateByUpdater(updater);
		return entity;
	}

	public ApiAccount deleteById(Long id) {
		ApiAccount bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiAccount[] deleteByIds(Long[] ids) {
		ApiAccount[] beans = new ApiAccount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public ApiAccount findByAppId(String appId){
		return dao.findByAppId(appId);
	}


	private ApiAccountDao dao;

	@Autowired
	public void setDao(ApiAccountDao dao) {
		this.dao = dao;
	}
}