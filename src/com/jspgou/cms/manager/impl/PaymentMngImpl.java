package com.jspgou.cms.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.PaymentDao;
import com.jspgou.cms.entity.Payment;
import com.jspgou.cms.manager.PaymentMng;
import com.jspgou.cms.manager.ShippingMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class PaymentMngImpl implements PaymentMng {
	@Override
	public List<Payment> getListForCart(Long webId) {
		return dao.getListForCart(webId, true);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Payment> getList(Long webId, boolean all) {
		return dao.getList(webId, all);
	}
	
	@Override
	public Payment[] updatePriority(Long[] ids, Integer[] priority) {
		Payment[] beans = new Payment[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Payment> getByCode(String code, Long webId) {
		return dao.getByCode(code, webId);
	}

	@Override
	@Transactional(readOnly = true)
	public Payment findById(Long id) {
		Payment entity = dao.findById(id);
		return entity;
	}

	@Override
	public Payment save(Payment bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Payment update(Payment bean) {
		Updater<Payment> updater = new Updater<Payment>(bean);
		Payment entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Payment deleteById(Long id) {
		Payment bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public Payment[] deleteByIds(Long[] ids) {
		Payment[] beans = new Payment[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	
	
	
	
	  @Override
	public void addShipping(Payment Payment,long shippingIds[]){
	    	if (shippingIds != null) {
				for (long shippingId : shippingIds) {
					Payment.addToShippings(shippingMng.findById(shippingId));
				}
			}
	        
	    }
	
	  @Override
	public void updateShipping(Payment Payment,long shippingIds[]){
		  Payment.getShippings().clear();
	    	if (shippingIds != null) {
				for (long shippingId : shippingIds) {
					Payment.addToShippings(shippingMng.findById(shippingId));
				}
			}
	        
	    }
	
	
	
	

	private PaymentDao dao;

	@Autowired
	public void setDao(PaymentDao dao) {
		this.dao = dao;
	}
	
	@Autowired
	private ShippingMng shippingMng;
}