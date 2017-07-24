package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.GiftDao;
import com.jspgou.cms.dao.ShopMemberDao;
import com.jspgou.cms.entity.Gift;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.GiftMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class GiftMngImpl implements GiftMng {

	@Override
	public Gift deleteById(Long id) {
		return dao.deleteById(id);
	}

	@Override
	public Gift[] deleteByIds(Long[] ids) {
		Gift[] beans = new Gift[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
		
	}

	@Override
	public Gift findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Pagination getPageGift(int pageNo, int pageSize) {
		return dao.getPageGift(pageNo, pageSize);
	}

	@Override
	public Gift save(Gift bean) {
		return dao.save(bean);
	}
	
	@Override
	public Gift updateByGiftnumb(Long giftId,Integer giftNumb,Long shopMemberId){
		Gift gift=dao.findById(giftId);
		ShopMember smber=memberDao.findById(shopMemberId);
		Integer stock=gift.getGiftStock();
		Integer totalScore=gift.getGiftScore()*giftNumb;
		if(stock<giftNumb){
			return null;
		}else if(totalScore>smber.getScore()){
			return null;
		}else{
			gift.setGiftStock(stock-giftNumb);
			smber.setScore(smber.getScore()-totalScore);
		}
		return gift;
	}

	@Override
	public Gift updateByUpdater(Gift bean) {
		  Updater<Gift> updater = new Updater<Gift>(bean);
			return dao.updateByUpdater(updater);
	}
	
      @Autowired
      private GiftDao dao;
      @Autowired
      private ShopMemberDao memberDao;
}