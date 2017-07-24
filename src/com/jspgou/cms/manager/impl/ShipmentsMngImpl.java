package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShipmentsDao;
import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.entity.Shipments;
import com.jspgou.cms.manager.ShipmentsMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShipmentsMngImpl implements ShipmentsMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Boolean isPrint,int pageNo, int pageSize) {
		Pagination page = dao.getPage(isPrint,pageNo, pageSize);
		return page;
	}
	@Override
	@Transactional(readOnly = true)
	public List<Logistics> getAllList() {
		List<Logistics> list = dao.getAllList();
		return list;
	}
	
	@Override
	public List<Shipments> getlist(Long orderId) {
		return dao.getlist(orderId);
	}
	
	@Override
	public void deleteByorderId(Long orderId) {
		List<Shipments> list = getlist(orderId);
		for(Shipments shipments:list){
			deleteById(shipments.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Shipments findById(Long id) {
		Shipments entity = dao.findById(id);
		return entity;
	}

	@Override
	public Shipments save(Shipments bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Shipments update(Shipments bean) {
		Updater<Shipments> updater = new Updater<Shipments>(bean);
		Shipments entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Shipments deleteById(Long id) {
		Shipments bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Shipments[] deleteByIds(Long[] ids) {
		Shipments[] beans = new Shipments[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShipmentsDao dao;

	@Autowired
	public void setDao(ShipmentsDao dao) {
		this.dao = dao;
	}
}