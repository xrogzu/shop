package com.jspgou.cms.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiUserLoginDao;
import com.jspgou.cms.entity.ApiUserLogin;
import com.jspgou.cms.manager.ApiUserLoginMng;

@Service
@Transactional
public class ApiUserLoginMngImpl implements ApiUserLoginMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiUserLogin findById(Long id) {
		ApiUserLogin entity = dao.findById(id);
		return entity;
	}

	public ApiUserLogin save(ApiUserLogin bean) {
		dao.save(bean);
		return bean;
	}

	public ApiUserLogin update(ApiUserLogin bean) {
		Updater<ApiUserLogin> updater = new Updater<ApiUserLogin>(bean);
		ApiUserLogin entity = dao.updateByUpdater(updater);
		return entity;
	}

	public ApiUserLogin deleteById(Long id) {
		ApiUserLogin bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiUserLogin[] deleteByIds(Long[] ids) {
		ApiUserLogin[] beans = new ApiUserLogin[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	

    public ApiUserLogin findBySessionKey(String sessionKey){
				
    	 return dao.findBySessionKey(sessionKey);
	 }
	
     
    public ApiUserLogin findByUsername(String username){
    	 return dao.findByUsername(username);
     }

    
    
    public void updateLoginSuccess(String username,String sessionKey) {
		 ApiUserLogin apiUserLogin = findByUsername(username);
		 Date now = new Timestamp(System.currentTimeMillis());
		 apiUserLogin.setLoginTime(now);
		 apiUserLogin.setSessionKey(sessionKey);
        apiUserLogin.setLoginCount(apiUserLogin.getLoginCount() + 1);
		 update(apiUserLogin);	 
	 }
    
    
	public void saveLoginSuccess(String username,String sessionKey){
		ApiUserLogin apiUserLogin = new ApiUserLogin();
		Date now = new Timestamp(System.currentTimeMillis());
		apiUserLogin.setLoginTime(now);
		apiUserLogin.setSessionKey(sessionKey);
		apiUserLogin.setLoginCount(1);
		apiUserLogin.setUsername(username);
		save(apiUserLogin);
	}
    
    
    
	private ApiUserLoginDao dao;

	@Autowired
	public void setDao(ApiUserLoginDao dao) {
		this.dao = dao;
	}
}