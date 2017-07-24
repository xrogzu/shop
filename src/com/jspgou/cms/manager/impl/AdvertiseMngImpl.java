package com.jspgou.cms.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.AdvertiseDao;
import com.jspgou.cms.entity.Advertise;
import com.jspgou.cms.manager.AdspaceMng;
import com.jspgou.cms.manager.AdvertiseMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class AdvertiseMngImpl implements AdvertiseMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage( Integer adspaceId,
			Boolean enabled, int pageNo, int pageSize) {
		Pagination page = dao.getPage( adspaceId, enabled, pageNo,
				pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Advertise> getList(Integer adspaceId, Boolean enabled) {
		return dao.getList(adspaceId, enabled);
	}

	@Override
	@Transactional(readOnly = true)
	public Advertise findById(Integer id) {
		Advertise entity = dao.findById(id);
		return entity;
	}

	@Override
	public Advertise save(Advertise bean, Integer adspaceId,
			Map<String, String> attr) {
		bean.setAdspace(adspaceMng.findById(adspaceId));
		bean.setAttr(attr);
		bean.init();
		dao.save(bean);
		return bean;
	}

	@Override
	public Advertise update(Advertise bean, Integer adspaceId,
			Map<String, String> attr) {
		Updater<Advertise> updater = new Updater<Advertise>(bean);
		bean = dao.updateByUpdater(updater);
		bean.setAdspace(adspaceMng.findById(adspaceId));
		bean.getAttr().clear();
		bean.getAttr().putAll(attr);
		return bean;
	}

	@Override
	public Advertise deleteById(Integer id) {
		Advertise bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public Advertise[] deleteByIds(Integer[] ids) {
		Advertise[] beans = new Advertise[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public void display(Integer id) {
		Advertise ad = findById(id);
		if (ad != null) {
			ad.setDisplayCount(ad.getDisplayCount() + 1);
		}
	}

	@Override
	public void click(Integer id) {
		Advertise ad = findById(id);
		if (ad != null) {
			ad.setClickCount(ad.getClickCount() + 1);
		}
	}

	
	

	private AdspaceMng adspaceMng;
	private AdvertiseDao dao;

	@Autowired
	public void setAdspaceMng(AdspaceMng adspaceMng) {
		this.adspaceMng = adspaceMng;
	}

	@Autowired
	public void setDao(AdvertiseDao dao) {
		this.dao = dao;
	}
}