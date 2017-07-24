package com.jspgou.cms.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.WebserviceAuthDao;
import com.jspgou.cms.entity.WebserviceAuth;
import com.jspgou.cms.manager.WebserviceAuthMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.security.encoder.PwdEncoder;

@Service
@Transactional
public class WebserviceAuthMngImpl implements WebserviceAuthMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isPasswordValid(String username, String password){
		WebserviceAuth auth=findByUsername(username);
		if(auth!=null&&auth.getEnable()){
			return pwdEncoder.isPasswordValid(auth.getPassword(), password);
		}else{
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public WebserviceAuth findByUsername(String username) {
		WebserviceAuth entity = dao.findByUsername(username);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = true)
	public WebserviceAuth findById(Integer id) {
		WebserviceAuth entity = dao.findById(id);
		return entity;
	}

	@Override
	public WebserviceAuth save(WebserviceAuth bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public WebserviceAuth update(WebserviceAuth bean) {
		Updater<WebserviceAuth> updater = new Updater<WebserviceAuth>(bean);
		WebserviceAuth entity = dao.updateByUpdater(updater);
		return entity;
	}
	
	@Override
	public WebserviceAuth update(Integer id,String username,String password,String system,Boolean enable){
		WebserviceAuth entity =findById(id);
		if(StringUtils.isNotBlank(username)){
			entity.setUsername(username);
		}
		if(StringUtils.isNotBlank(password)){
			entity.setPassword(pwdEncoder.encodePassword(password));
		}
		if(StringUtils.isNotBlank(system)){
			entity.setSystem(system);
		}
		if(enable!=null){
			entity.setEnable(enable);
		}
		return entity;
	}

	@Override
	public WebserviceAuth deleteById(Integer id) {
		WebserviceAuth bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public WebserviceAuth[] deleteByIds(Integer[] ids) {
		WebserviceAuth[] beans = new WebserviceAuth[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private PwdEncoder pwdEncoder;
	private WebserviceAuthDao dao;

	@Autowired
	public void setDao(WebserviceAuthDao dao) {
		this.dao = dao;
	}
}