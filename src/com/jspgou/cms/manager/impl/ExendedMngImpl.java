package com.jspgou.cms.manager.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jspgou.cms.dao.ExendedDao;
import com.jspgou.cms.entity.Exended;
import com.jspgou.cms.entity.ProductType;
import com.jspgou.cms.manager.ExendedMng;
import com.jspgou.cms.manager.ProductTypeMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ExendedMngImpl implements ExendedMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Exended findById(Long id) {
		Exended entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public List<Exended> getList(){
		return dao.getList();
	}
	
	@Override
	public List<Exended> getList(Long typeId){
	   return dao.getList(typeId);
	}

	@Override
	public Exended save(Exended bean,Long[] typeIds) {
		bean=dao.save(bean);	
		if(typeIds!=null&&typeIds.length>0){
			for(Long tid:typeIds){
				productTypeMng.findById(tid).addToexendeds(bean);
			}
		}
		return bean;
	}

	@Override
	public Exended update(Exended bean,Long[] typeIds) {
		Updater<Exended> updater = new Updater<Exended>(bean);
		Exended entity = dao.updateByUpdater(updater);
		Set<ProductType> types = entity.getProductTypes();
		for (ProductType type : types) {
			type.removeFromExendeds(entity);
		}
		types.clear();
		if(typeIds!=null&&typeIds.length>0){
			for(Long tid:typeIds){
				productTypeMng.findById(tid).addToexendeds(entity);
			}
		}
		return entity;
	}

	@Override
	public Exended deleteById(Long id) {
		Exended bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Exended[] deleteByIds(Long[] ids) {
		Exended[] beans = new Exended[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public Exended[] updatePriority(Long[] wids, Integer[] priority){
		int len = wids.length;
		Exended[] beans = new Exended[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(wids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	private ExendedDao dao;
	@Autowired
	private ProductTypeMng productTypeMng;
	

	@Autowired
	public void setDao(ExendedDao dao) {
		this.dao = dao;
	}

}