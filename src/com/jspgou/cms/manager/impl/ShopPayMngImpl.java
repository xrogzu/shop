package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ShopPayDao;
import com.jspgou.cms.entity.ShopPay;
import com.jspgou.cms.manager.ShopPayMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopPayMngImpl implements ShopPayMng {

	@Override
	public ShopPay deleteById(Integer id) {
		return dao.deleteById(id);
	}

	@Override
	public ShopPay[] deleteByIds(Integer[] ids) {
		ShopPay[] beans = new ShopPay[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
		
	}

	@Override
	public ShopPay findById(Integer id) {
		return dao.findById(id);
	}


	@Override
	public ShopPay save(ShopPay bean) {
		return dao.save(bean);
	}
	
	

	@Override
	public ShopPay updateByUpdater(ShopPay bean) {
		Updater<ShopPay> updater = new Updater<ShopPay>(bean);
			
			return dao.updateByUpdater(updater);
		
	}
	
      @Autowired
      private ShopPayDao dao;
}