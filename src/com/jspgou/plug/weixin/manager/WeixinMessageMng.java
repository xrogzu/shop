package com.jspgou.plug.weixin.manager;

import java.util.List;

import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.WeixinMessage;

public interface WeixinMessageMng {
	
	public Pagination getPage(Long siteId,int pageNo,int pageSize);
	
	public List<WeixinMessage> getList(Long siteId);
	
	public WeixinMessage getWelcome(Long siteId);
	
	public WeixinMessage findByNumber(String number,Long siteId);
	
	public WeixinMessage findById(Integer id);
	
	public WeixinMessage save(WeixinMessage bean);
	
	public WeixinMessage update(WeixinMessage bean);
	
	public WeixinMessage deleteById(Integer id);
	
	public WeixinMessage[] deleteByIds(Integer[] ids);
	
}
