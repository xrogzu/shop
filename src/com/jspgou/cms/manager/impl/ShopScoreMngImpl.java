package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopScoreDao;
import com.jspgou.cms.entity.ShopScore;
import com.jspgou.cms.manager.ShopScoreMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopScoreMngImpl implements ShopScoreMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long memberId,Boolean status,Boolean useStatus,
			Date startTime,Date endTime,Integer pageSize,Integer pageNo){
		Pagination page = dao.getPage(memberId,status,useStatus,
				startTime,endTime,pageNo, pageSize);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ShopScore> getlist(String code){
		return dao.getlist(code);
	}

	@Override
	@Transactional(readOnly = true)
	public ShopScore findById(Long id) {
		ShopScore entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopScore save(ShopScore bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopScore update(ShopScore bean) {
		Updater<ShopScore> updater = new Updater<ShopScore>(bean);
		ShopScore entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopScore deleteById(Long id) {
		ShopScore bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public ShopScore[] deleteByIds(Long[] ids) {
		ShopScore[] beans = new ShopScore[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopScoreDao dao;

	@Autowired
	public void setDao(ShopScoreDao dao) {
		this.dao = dao;
	}
}