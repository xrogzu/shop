package com.jspgou.cms.manager;

import java.util.Date;
import java.util.List;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ReceiverMessage;

public interface ReceiverMessageMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public Pagination getPage(Long sendMemberId,int pageNo,Integer box, int pageSize);
	
	public Pagination getPage(Long sendMemberId,
			Long receiverMemberId, String title, Date sendBeginTime,
			Date sendEndTime, Boolean status, Integer box, Boolean cacheable,
			int pageNo, int pageSize);
	
	public List<ReceiverMessage> getList(Long sendMemberId,
			Long receiverMemberId, String title, Date sendBeginTime,
			Date sendEndTime, Boolean status, Integer box, Boolean cacheable);
	
	public Pagination getUnreadPage(Long sendMemberId,int pageNo, int pageSize);

	public ReceiverMessage findById(Long id);

	public ReceiverMessage save(ReceiverMessage bean);

	public ReceiverMessage update(ReceiverMessage bean);

	public ReceiverMessage deleteById(Long id);
	
	public ReceiverMessage[] deleteByIds(Long[] ids);
}