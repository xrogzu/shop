package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.PopularityItemDao;
import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.PopularityItem;
import com.jspgou.cms.manager.PopularityGroupMng;
import com.jspgou.cms.manager.PopularityItemMng;

@Service
@Transactional
public class PopularityItemMngImpl implements PopularityItemMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<PopularityItem> getlist(Long cartId,Long popularityGroupId){
		return dao.getlist(cartId,popularityGroupId);
		
	}
	
	@Override
	public PopularityItem findById(Long cartId,Long popularityId){
		return dao.findById(cartId, popularityId);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public PopularityItem findById(Long id) {
		PopularityItem entity = dao.findById(id);
		return entity;
	}

	@Override
	public PopularityItem save(PopularityItem bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public PopularityItem update(PopularityItem bean) {
		Updater<PopularityItem> updater = new Updater<PopularityItem>(bean);
		PopularityItem entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public PopularityItem deleteById(Long id) {
		PopularityItem bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public PopularityItem[] deleteByIds(Long[] ids) {
		PopularityItem[] beans = new PopularityItem[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public void save(Cart cart, Long popularityId){
		if(popularityId!=null){
			PopularityItem bean =findById(cart.getId(),popularityId);
			if(bean!=null){
				bean.setCount(bean.getCount()+1);
				update(bean);
			}else{
				bean =new PopularityItem();
				bean.setCart(cart);
				bean.setPopularityGroup(popularityGroupMng.findById(popularityId));
				bean.setCount(1);
				save(bean);
			}
			
		}
	}

	private PopularityItemDao dao;

	@Autowired
	private PopularityGroupMng popularityGroupMng;
	@Autowired
	public void setDao(PopularityItemDao dao) {
		this.dao = dao;
	}
}