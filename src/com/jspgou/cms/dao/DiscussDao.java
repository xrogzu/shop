package com.jspgou.cms.dao;

import java.util.Date;
import java.util.List;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Discuss;

/**
* This class should preserve.
* @preserve
*/
public interface DiscussDao {
	public Discuss findById(Long id);
	
	public Discuss saveOrUpdate(Discuss bean);
	
	public Discuss updateByUpdater(Updater<Discuss> updater);

	public Discuss update(Discuss bean);

	public Discuss deleteById(Long id);
	
	public Pagination getPageByProduct(Long memberId,Long productId,String discussType,String userName,String productName, 

			Date startTime,Date endTime,int pageNo,int pageSize,boolean cache);
	
	public Pagination getPageByMember(Long memberId,int pageNo,int pageSize,boolean cache);
	
	public List<Discuss> findByType(Long productId,String discussType);
	
}