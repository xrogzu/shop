package com.jspgou.cms.dao;

import java.util.Date;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Message;

public interface MessageDao {
	public Pagination getPage(Long sendMemberId,int pageNo, int pageSize);

	public Message findById(Long id);

	public Message save(Message bean);

	public Message updateByUpdater(Updater<Message> updater);

	public Message deleteById(Long id);
}     