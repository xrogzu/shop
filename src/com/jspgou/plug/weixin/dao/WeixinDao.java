package com.jspgou.plug.weixin.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.Weixin;

public interface WeixinDao {
	
	public Pagination getPage(Integer siteId,int pageNo,int pageSize);
	
	public Weixin save(Weixin bean);
	
	public Weixin deleteById(Integer id);
	
	public Weixin findById(Integer id);
	
	public Weixin find(Long siteId);

	public Weixin updateByUpdater(Updater<Weixin> updater);
	
	public Weixin  findBy();
}
