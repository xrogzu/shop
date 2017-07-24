package com.jspgou.cms.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.GiftExchangeDao;
import com.jspgou.cms.entity.Gift;
import com.jspgou.cms.entity.GiftExchange;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberAddress;
import com.jspgou.cms.entity.ShopScore;
import com.jspgou.cms.entity.ShopScore.ScoreTypes;
import com.jspgou.cms.manager.GiftExchangeMng;
import com.jspgou.cms.manager.ShopScoreMng;

@Service
@Transactional
public class GiftExchangeMngImpl implements GiftExchangeMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<GiftExchange> getlist(Long memberId){
		return dao.getlist(memberId);
	}

	@Override
	@Transactional(readOnly = true)
	public GiftExchange findById(Long id) {
		GiftExchange entity = dao.findById(id);
		return entity;
	}

	@Override
	public GiftExchange save(GiftExchange bean) {
		dao.save(bean);
		return bean;
	}
	
	@Override
	public GiftExchange save(Gift gift,ShopMemberAddress shopMemberAddress,ShopMember member,Integer count){
		GiftExchange giftExchange = new GiftExchange();
		Date now = new Timestamp(System.currentTimeMillis());
		giftExchange.setAmount(count);
		giftExchange.setCreateTime(now);
		if(StringUtils.isBlank(shopMemberAddress.getTel())&&StringUtils.isNotBlank(shopMemberAddress.getPhone())){	
			giftExchange.setDetailaddress(shopMemberAddress.getUsername()+",固话："+shopMemberAddress.getPhone()+","+shopMemberAddress.getDetailaddress()+","+shopMemberAddress.getPostCode());
		}else if(StringUtils.isBlank(shopMemberAddress.getPhone())&&StringUtils.isNotBlank(shopMemberAddress.getTel())){
			giftExchange.setDetailaddress(shopMemberAddress.getUsername()+",移动电话："+shopMemberAddress.getTel()+","+shopMemberAddress.getDetailaddress()+","+shopMemberAddress.getPostCode());
		}else if(StringUtils.isNotBlank(shopMemberAddress.getTel())&&StringUtils.isNotBlank(shopMemberAddress.getPhone())){
			giftExchange.setDetailaddress(shopMemberAddress.getUsername()+",移动电话："+shopMemberAddress.getTel()+","+shopMemberAddress.getDetailaddress()+","+shopMemberAddress.getPostCode());
		}
		giftExchange.setGift(gift);
		giftExchange.setMember(member);
		giftExchange.setScore(gift.getGiftScore());
		giftExchange.setTotalScore(gift.getGiftScore()*count);
		giftExchange.setStatus(1);
		
		ShopScore shopScore=new ShopScore();
		shopScore.setMember(member);
		shopScore.setName(gift.getGiftName());
		shopScore.setScoreTime(new Date());
		shopScore.setStatus(true);
		shopScore.setUseStatus(true);
		shopScore.setScoreType(ScoreTypes.ORDER_SCORE.getValue());
		shopScore.setScore(gift.getGiftScore()*count);
		shopScoreMng.save(shopScore);
		member.setScore(member.getScore()-gift.getGiftScore()*count);
		return save(giftExchange);
	}

	@Override
	public GiftExchange update(GiftExchange bean) {
		Updater<GiftExchange> updater = new Updater<GiftExchange>(bean);
		GiftExchange entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public GiftExchange deleteById(Long id) {
		GiftExchange bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public GiftExchange[] deleteByIds(Long[] ids) {
		GiftExchange[] beans = new GiftExchange[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private GiftExchangeDao dao;

	@Autowired
	public void setDao(GiftExchangeDao dao) {
		this.dao = dao;
	}
	
	@Autowired
	private ShopScoreMng shopScoreMng;
}