package com.jspgou.cms.manager;

import java.util.Date;
import java.util.List;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Discuss;
/**
* This class should preserve.
* @preserve
*/
public interface DiscussMng { 
	public Discuss findById(Long id);
	
	public Discuss update(Discuss entity);

	public Discuss deleteById(Long id);
	
	public Discuss saveOrUpdate(Long productId,String content,Long memberId,String discussType);
	
	public Pagination getPage(Long memberId,Long productId,String discussType,String userName,String productName,

			Date startTime,Date endTime,int pageNo,int pageSize,boolean cache);
	
	public Discuss[] deleteByIds(Long[] ids);
	
	public Pagination getPageByMember(Long memberId,int pageNo,int pageSize,boolean cache);
	
	public List<Discuss> findByType(Long productId,String discussType);
	
}