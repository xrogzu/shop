package com.jspgou.plug.weixin.dao.impl;

import java.util.List;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.weixin.dao.WeixinMessageDao;
import com.jspgou.plug.weixin.entity.WeixinMessage;

public class WeixinMessageDaoImpl extends HibernateBaseDao<WeixinMessage, Integer> implements WeixinMessageDao {
	
	public Pagination getPage(Long siteId,int pageNo,int pageSize){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=false").setParam("siteId", siteId);
		return find(f,pageNo,pageSize);
	}
	
	public List<WeixinMessage> getList(Long siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=false order by bean.number");
		f.setParam("siteId", siteId);
		return find(f);
	}
	
	public WeixinMessage getWelcome(Long siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.welcome=true order by bean.number");
		f.setParam("siteId", siteId);
		List<WeixinMessage> lists = find(f);
		if(lists!=null && lists.size()>0){
			return lists.get(0);
		}
		return null;
	}
	
	public WeixinMessage findByNumber(String number,Long siteId){
		Finder f = Finder.create(" from WeixinMessage bean where bean.site.id=:siteId and bean.number=:number order by bean.id desc");
		f.setParam("number", number);
		f.setParam("siteId", siteId);
		List<WeixinMessage> lists = find(f);
		if(lists!=null && lists.size()>0){
			return lists.get(0);
		}
		return null;
	}
	
	public WeixinMessage findById(Integer id){
		return get(id);
	}
	
	public WeixinMessage save(WeixinMessage bean){
		getSession().save(bean);
		return bean;
	}
	
	public WeixinMessage deleteById(Integer id){
		WeixinMessage entity = get(id);
		if(entity!=null){
			getSession().delete(entity);
			return entity;
		}
		return null;
	}

	@Override
	protected Class<WeixinMessage> getEntityClass() {
		return WeixinMessage.class;
	}

}
