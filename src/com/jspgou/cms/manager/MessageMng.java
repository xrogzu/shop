package com.jspgou.cms.manager;

import java.util.Date;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Message;

public interface MessageMng {
	public Pagination getPage(Long sendMemberId,int pageNo, int pageSize);

	public Message findById(Long id);

	public Message save(Message bean);

	public Message update(Message bean);

	public Message deleteById(Long id);
	
	public Message[] deleteByIds(Long[] ids);
}