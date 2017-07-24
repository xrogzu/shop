package com.jspgou.cms.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopMoneyDao;
import com.jspgou.cms.entity.ShopMoney;
import com.jspgou.cms.manager.ShopMoneyMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopMoneyMngImpl implements ShopMoneyMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public Pagination getPage(Long memberId,Boolean status,
			Date startTime,Date endTime,Integer pageSize,Integer pageNo){
		Pagination page = dao.getPage(memberId,status,
				startTime,endTime,pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopMoney findById(Long id) {
		ShopMoney entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopMoney save(ShopMoney bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopMoney update(ShopMoney bean) {
		Updater<ShopMoney> updater = new Updater<ShopMoney>(bean);
		ShopMoney entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopMoney deleteById(Long id) {
		ShopMoney bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public ShopMoney[] deleteByIds(Long[] ids) {
		ShopMoney[] beans = new ShopMoney[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopMoneyDao dao;

	@Autowired
	public void setDao(ShopMoneyDao dao) {
		this.dao = dao;
	}
}