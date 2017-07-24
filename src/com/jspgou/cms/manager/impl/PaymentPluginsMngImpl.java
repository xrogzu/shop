package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.PaymentPluginsDao;
import com.jspgou.cms.entity.PaymentPlugins;
import com.jspgou.cms.manager.PaymentPluginsMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class PaymentPluginsMngImpl implements PaymentPluginsMng {

	@Override
	@Transactional(readOnly = true)
	public List<PaymentPlugins> getList() {
		return dao.getList();
	}
	@Override
	public List<PaymentPlugins> getList1(String platform){
		
		return dao.getList1(platform);
	}
	
	@Override
	public PaymentPlugins[] updatePriority(Long[] ids, Integer[] priority) {
		PaymentPlugins[] beans = new PaymentPlugins[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	@Override
	public PaymentPlugins findByCode(String code){
		return dao.findByCode(code);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PaymentPlugins findById(Long id) {
		PaymentPlugins entity = dao.findById(id);
		return entity;
	}

	@Override
	public PaymentPlugins save(PaymentPlugins bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public PaymentPlugins update(PaymentPlugins bean) {
		Updater<PaymentPlugins> updater = new Updater<PaymentPlugins>(bean);
		PaymentPlugins entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public PaymentPlugins deleteById(Long id) {
		PaymentPlugins bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public PaymentPlugins[] deleteByIds(Long[] ids) {
		PaymentPlugins[] beans = new PaymentPlugins[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	private PaymentPluginsDao dao;

	@Autowired
	public void setDao(PaymentPluginsDao dao) {
		this.dao = dao;
	}
	
}