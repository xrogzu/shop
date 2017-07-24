package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.PopularityGroupDao;
import com.jspgou.cms.entity.PopularityGroup;
import com.jspgou.cms.manager.PopularityGroupMng;
import com.jspgou.cms.manager.ProductMng;

@Service
@Transactional
public class PopularityGroupMngImpl implements PopularityGroupMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public PopularityGroup findById(Long id) {
		PopularityGroup entity = null ;
		if(id!=null){
			entity = dao.findById(id);
		}
		return entity;
	}

	@Override
	public PopularityGroup save(PopularityGroup bean) {
		bean.init();
		dao.save(bean);
		return bean;
	}

	@Override
	public PopularityGroup update(PopularityGroup bean) {
		Updater<PopularityGroup> updater = new Updater<PopularityGroup>(bean);
		PopularityGroup entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public PopularityGroup deleteById(Long id) {
		PopularityGroup bean = findById(id);
		bean.getProducts().clear();
		dao.deleteById(id);
		return bean;
	}
	
	@Override
	public PopularityGroup[] deleteByIds(Long[] ids) {
		PopularityGroup[] beans = new PopularityGroup[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	 @Override
	public void addProduct(PopularityGroup bean,Long productIds[]){
		double price = 0.0;
	    	if (productIds != null) {
				for (Long productId : productIds) {
					bean.addToProducts(productMng.findById(productId));
					price = price + productMng.findById(productId).getPrice();
				}
			}
	    bean.setPrice(price); 
	  }
	
	  @Override
	public void updateProduct(PopularityGroup bean,Long productIds[]){
		  double price = 0.0;
		  bean.getProducts().clear();
	    	if (productIds != null) {
	    		for (Long productId : productIds) {
					bean.addToProducts(productMng.findById(productId));
					price = price + productMng.findById(productId).getPrice();
				}
			}
	      bean.setPrice(price);   
	    }

	private PopularityGroupDao dao;

	@Autowired
	private ProductMng productMng;
	@Autowired
	public void setDao(PopularityGroupDao dao) {
		this.dao = dao;
	}
}