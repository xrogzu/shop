package com.jspgou.plug.weixin.dao;

import java.util.List;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.WeixinMessage;

public interface WeixinMessageDao {
	
	public Pagination getPage(Long siteId,int pageNo,int pageSize);
	
	public List<WeixinMessage> getList(Long siteId);
	
	public WeixinMessage getWelcome(Long siteId);
	
	public WeixinMessage findByNumber(String number,Long siteId);
	
	public WeixinMessage save(WeixinMessage bean);
	
	public WeixinMessage findById(Integer id);
	
	public WeixinMessage deleteById(Integer id);

	public WeixinMessage updateByUpdater(Updater<WeixinMessage> updater);
}
