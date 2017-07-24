package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ReceiverMessageDao;
import com.jspgou.cms.entity.ReceiverMessage;
import com.jspgou.cms.manager.ReceiverMessageMng;

@Service
@Transactional
public class ReceiverMessageMngImpl implements ReceiverMessageMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	public Pagination getPage(Long sendMemberId,int pageNo,Integer box, int pageSize){
		return dao.getPage(sendMemberId, pageNo,box, pageSize);
		
	}
	
	public Pagination getPage(Long sendMemberId,
			Long receiverMemberId, String title, Date sendBeginTime,
			Date sendEndTime, Boolean status, Integer box, Boolean cacheable,
			int pageNo, int pageSize) {
		return dao.getPage(sendMemberId, receiverMemberId, title,
				sendBeginTime, sendEndTime, status, box, cacheable, pageNo,
				pageSize);
	}
	
	public List<ReceiverMessage> getList(Long sendMemberId,
			Long receiverMemberId, String title, Date sendBeginTime,
			Date sendEndTime, Boolean status, Integer box, Boolean cacheable) {
		return dao.getList(sendMemberId, receiverMemberId, title,
				sendBeginTime, sendEndTime, status, box, cacheable);
	}
	
	public Pagination getUnreadPage(Long sendMemberId, int pageNo, int pageSize) {
		return dao.getUnreadPage(sendMemberId, pageNo, pageSize);
	}
	@Transactional(readOnly = true)
	public ReceiverMessage findById(Long id) {
		ReceiverMessage entity = dao.findById(id);
		return entity;
	}

	public ReceiverMessage save(ReceiverMessage bean) {
		dao.save(bean);
		return bean;
	}

	public ReceiverMessage update(ReceiverMessage bean) {
		Updater<ReceiverMessage> updater = new Updater<ReceiverMessage>(bean);
		ReceiverMessage entity = dao.updateByUpdater(updater);
		return entity;
	}

	public ReceiverMessage deleteById(Long id) {
		ReceiverMessage bean = dao.deleteById(id);
		return bean;
	}
	
	public ReceiverMessage[] deleteByIds(Long[] ids) {
		ReceiverMessage[] beans = new ReceiverMessage[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ReceiverMessageDao dao;

	@Autowired
	public void setDao(ReceiverMessageDao dao) {
		this.dao = dao;
	}

}