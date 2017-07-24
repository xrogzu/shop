package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiInfoDao;
import com.jspgou.cms.entity.ApiInfo;
import com.jspgou.cms.manager.ApiInfoMng;

@Service
@Transactional
public class ApiInfoMngImpl implements ApiInfoMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiInfo findById(Long id) {
		ApiInfo entity = dao.findById(id);
		return entity;
	}


	public ApiInfo findByApiUrl(String apiUrl){
		return dao.findByApiUrl(apiUrl);
	}

	
	
	public ApiInfo save(ApiInfo bean) {
		bean.init();
		dao.save(bean);
		return bean;
	}

	public ApiInfo update(ApiInfo bean) {
		Updater<ApiInfo> updater = new Updater<ApiInfo>(bean);
		ApiInfo entity = dao.updateByUpdater(updater);
		return entity;
	}

	public ApiInfo deleteById(Long id) {
		ApiInfo bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiInfo[] deleteByIds(Long[] ids) {
		ApiInfo[] beans = new ApiInfo[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ApiInfoDao dao;

	@Autowired
	public void setDao(ApiInfoDao dao) {
		this.dao = dao;
	}
}