package com.jspgou.core.manager.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.dao.LogDao;
import com.jspgou.core.entity.Log;
import com.jspgou.core.entity.User;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.LogMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class LogMngImpl implements LogMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Log findById(Long id) {
		Log entity = dao.findById(id);
		return entity;
	}

	@Override
	public Log save(Log bean) {
		dao.save(bean);
		return bean;
	}
	
	@Override
	public void save(String versions, String updatelog){
		Date date = new Date();
		Log bean = new Log();
		bean.setContent(updatelog);
		bean.setTitle(versions);
		bean.setCategory(1);
		bean.setTime(date);
		dao.save(bean);
	}
	
	@Override
	public Log update(Log bean) {
		Updater<Log> updater = new Updater<Log>(bean);
		Log entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Log deleteById(Long id) {
		Log bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Log[] deleteByIds(Long[] ids) {
		Log[] beans = new Log[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public Log save(Integer category, Website site, User user,
			String url, String ip, Date date, String title, String content) {
		Log log = new Log();
		log.setSite(site);
		log.setUser(user);
		log.setCategory(category);
		log.setIp(ip);
		log.setTime(date);
		log.setUrl(url);
		log.setTitle(title);
		log.setContent(content);
		save(log);
		return log;
	}
	
	@Override
	public Log loginFailure(HttpServletRequest request,String content){
		String ip=RequestUtils.getIpAddr(request);
		UrlPathHelper helper = new UrlPathHelper();
		String uri=helper.getOriginatingRequestUri(request);
		Date date=new Date();
		Log log = save(Log.LOGIN_FAILURE, null, null, uri, ip, date,Log.LOGIN_FAILURE_TITLE, content);
		return log;
	}
	
	@Override
	public Log loginSuccess(HttpServletRequest request,User user){
		String ip=RequestUtils.getIpAddr(request);
		UrlPathHelper helper=new UrlPathHelper();
		String uri=helper.getOriginatingRequestUri(request);
		Date date=new Date();
		Log log=save(Log.LOGIN_SUCCESS, null, user, uri, ip, date,Log.LOGIN_SUCCESS_TITLE, null);
		return log;
	}
	
	private LogDao dao;

	@Autowired
	public void setDao(LogDao dao) {
		this.dao = dao;
	}


	public Log operating(HttpServletRequest request, String title,
			String content) {
		
		String ip = RequestUtils.getIpAddr(request);
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		Date date = new Date();
		Log log = save(Log.OPERATING, null, null, uri, ip, date,
				MessageResolver.getMessage(request, title), content);
		return log;
	}
}