package com.jspgou.plug.weixin.manager;

import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.Weixin;

public interface WeixinMng {

	public Pagination getPage(Integer siteId,int pageNo,int pageSize);
	
	public Weixin findById(Integer id);
	
	public Weixin find(Long siteId);
	
	public Weixin save(Weixin bean);
	
	public Weixin update(Weixin bean);
	
	public Weixin deleteById(Integer id);
	
	public Weixin[] delete(Integer[] id);
	
	public Weixin findBy();
}
