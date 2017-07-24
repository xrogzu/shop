package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.CollectDao;
import com.jspgou.cms.entity.Collect;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.CollectMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class CollectMngImpl implements CollectMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getList(Integer pageSize,Integer pageNo,Long memberId){
		Pagination list = dao.getList(pageSize, pageNo, memberId);
		return list;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collect findById(Integer id) {
		Collect entity = dao.findById(id);
		return entity;
	}

	@Override
	public Collect save(ShopMember bean,Long productId,Long fashionId) {
		Collect entity = new Collect();
		entity.setProduct(productMng.findById(productId));
		if(fashionId!=null){
		entity.setFashion(fashionMng.findById(fashionId));
		}
		entity.setMember(bean);
		entity.setTime(new Date());
		dao.save(entity);
		return entity;
	}
	
	@Override
	public List<Collect> getList(Long memberId, int firstResult, int maxResults){
		return dao.getList(memberId, firstResult, maxResults);
	}
	
	@Override
	public List<Collect> getList(Long productId,Long memberId){
		return dao.getList(productId, memberId);
	}

	@Override
	public Collect update(Collect bean,Long pTypeid) {
		Updater<Collect> updater = new Updater<Collect>(bean);
		Collect entity = dao.updateByUpdater(updater);
		
		return entity;
	}
	
	@Override
	public List<Collect> findByProductId(Long productId){
		return dao.findByProductId(productId);
	}
	
	@Override
	public Collect findByProductFashionId(Long id) {
		return dao.findByProductFashionId(id);
	}
	

	@Override
	public Collect deleteById(Integer id) {
		
		Collect entity = findById(id);
		
		entity = dao.deleteById(id);
		return entity;
	}

	@Override
	public Collect[] deleteByIds(Integer[] ids) {
		Collect[] beans = new Collect[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private ProductMng productMng;
	@Autowired
	private ProductFashionMng fashionMng;
	@Autowired
	private CollectDao dao;
}