package com.jspgou.cms.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.MessageDao;
import com.jspgou.cms.entity.Message;
import com.jspgou.cms.manager.MessageMng;

@Service
@Transactional
public class MessageMngImpl implements MessageMng {
	
	@Transactional(readOnly = true)
	public Pagination getPage(Long sendMemberId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(sendMemberId,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public Message findById(Long id) {
		Message entity = dao.findById(id);
		return entity;
	}

	public Message save(Message bean) {
		dao.save(bean);
		return bean;
	}

	public Message update(Message bean) {
		Updater<Message> updater = new Updater<Message>(bean);
		Message entity = dao.updateByUpdater(updater);
		return entity;
	}

	public Message deleteById(Long id) {
		Message bean = dao.deleteById(id);
		return bean;
	}
	
	public Message[] deleteByIds(Long[] ids) {
		Message[] beans = new Message[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private MessageDao dao;

	@Autowired
	public void setDao(MessageDao dao) {
		this.dao = dao;
	}
}