package com.jspgou.plug.weixin.dao;

import java.util.List;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.WeixinMenu;

public interface WeixinMenuDao {
	
	public Pagination getPage(Long siteId,Integer parentId,int pageNo,int pageSize);
	
	public List<WeixinMenu> getList(Long siteId,Integer count);
	
	public WeixinMenu findById(Integer id);
	
	public WeixinMenu save(WeixinMenu bean);

	public WeixinMenu updateByUpdater(Updater<WeixinMenu> updater);
	
	public WeixinMenu deleteById(Integer id);
}
