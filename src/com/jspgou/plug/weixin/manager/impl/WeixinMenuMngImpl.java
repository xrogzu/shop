package com.jspgou.plug.weixin.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.dao.WeixinMenuDao;
import com.jspgou.plug.weixin.entity.WeixinMenu;
import com.jspgou.plug.weixin.manager.WeixinMenuMng;

@Service
@Transactional
public class WeixinMenuMngImpl implements WeixinMenuMng {
	
	@Transactional(readOnly=true)
	public Pagination getPage(Long siteId,Integer parentId,int pageNo,int pageSize){
		return dao.getPage(siteId,parentId,pageNo,pageSize);
	}
	
	@Transactional(readOnly=true)
	public List<WeixinMenu> getList(Long siteId,Integer count){
		return dao.getList(siteId,count);
	}
	
	@Transactional(readOnly=true)
	public WeixinMenu findById(Integer id){
		return dao.findById(id);
	}
	
	public WeixinMenu save(WeixinMenu bean){
		return dao.save(bean);
	}
	
	public WeixinMenu update(WeixinMenu bean){
		Updater<WeixinMenu> updater = new Updater<WeixinMenu>(bean);
		return dao.updateByUpdater(updater);
	}
	
	public WeixinMenu deleteById(Integer id){
		return dao.deleteById(id);
	}

	public WeixinMenu[] deleteByIds(Integer[] ids){
		WeixinMenu[] beans = new WeixinMenu[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private WeixinMenuDao dao;
}
