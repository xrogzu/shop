package com.jspgou.cms.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ShippingDao;
import com.jspgou.cms.entity.Shipping;
import com.jspgou.cms.manager.ShippingMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShippingMngImpl implements ShippingMng {
	@Override
	@Transactional(readOnly = true)
	public List<Shipping> getList(Long webId, boolean all) {
		return dao.getList(webId, all, false);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Shipping> getListForCart(Long webId, Long countryId,
			int weight, int count) {
		List<Shipping> list = dao.getList(webId, false, true);
		for (Shipping shipping : list) {
//			shipping.setPrice(shipping.calPrice(weight));
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Shipping findById(Long id) {
		Shipping entity = dao.findById(id);
		return entity;
	}

	@Override
	public Shipping save(Shipping bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Shipping update(Shipping bean) {
		Shipping entity = findById(bean.getId());
		Updater<Shipping> updater = new Updater<Shipping>(bean);
		entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Shipping deleteById(Long id) {
		Shipping bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public Shipping[] deleteByIds(Long[] ids) {
		Shipping[] beans = new Shipping[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public Shipping[] updatePriority(Long[] ids, Integer[] priority) {
		Shipping[] beans = new Shipping[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	private ShippingDao dao;


	@Autowired
	public void setDao(ShippingDao dao) {
		this.dao = dao;
	}
}