package com.jspgou.plug.weixin.manager;

import java.util.List;

import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.entity.WeixinMenu;

public interface WeixinMenuMng {
	
	public Pagination getPage(Long siteId,Integer parentId,int pageNo,int pageSize);
	
	public List<WeixinMenu> getList(Long siteId,Integer count);
	
	public WeixinMenu findById(Integer id);
	
	public WeixinMenu save(WeixinMenu bean);
	
	public WeixinMenu update(WeixinMenu bean);
	
	public WeixinMenu deleteById(Integer id);
	
	public WeixinMenu[] deleteByIds(Integer[] ids);
}
