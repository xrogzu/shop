package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.RelatedgoodsDao;
import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.cms.manager.RelatedgoodsMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class RelatedgoodsMngImpl implements RelatedgoodsMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	public List<Relatedgoods> findByIdProductId(Long productId){
		List<Relatedgoods> entity = dao.findByIdProductId(productId);
		return entity;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Relatedgoods findById(Long id) {
		Relatedgoods entity = dao.findById(id);
		return entity;
	}

	@Override
	public Relatedgoods save(Relatedgoods bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Relatedgoods update(Relatedgoods bean) {
		Updater<Relatedgoods> updater = new Updater<Relatedgoods>(bean);
		Relatedgoods entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Relatedgoods deleteById(Long id) {
		Relatedgoods bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Relatedgoods[] deleteByIds(Long[] ids) {
		Relatedgoods[] beans = new Relatedgoods[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public void addProduct(Long id,Long productIds[]){
		if(productIds!=null){
			for(Long productId:productIds){
				if(productId!=null){					
					Relatedgoods r=new Relatedgoods();
					r.setProductId(id);
					r.setProductIds(productId);
					dao.save(r);
				}
			}
		}
	}
	
	@Override
	public void updateProduct(Long id,Long productIds[]){
		List<Relatedgoods> Relatedgoods=dao.findByIdProductId(id);
		for(int i=0;i<Relatedgoods.size();i++){
			deleteById(Relatedgoods.get(i).getId());
		}
		if(productIds!=null){
			for(Long productId:productIds){
				if(productId!=null){
					Relatedgoods r=new Relatedgoods();
					r.setProductId(id);
					r.setProductIds(productId);
					dao.save(r);
				}
			}
		}
	}
	
	@Override
	public void deleteProduct(Long productid){
		List<Relatedgoods> Relatedgoods=dao.findByIdProductId(productid);
		for(int i=0;i<Relatedgoods.size();i++){
			deleteById(Relatedgoods.get(i).getId());
		}
	}

	private RelatedgoodsDao dao;

	@Autowired
	public void setDao(RelatedgoodsDao dao) {
		this.dao = dao;
	}
}