package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.OrderItemDao;
import com.jspgou.cms.entity.OrderItem;
import com.jspgou.cms.manager.OrderItemMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class OrderItemMngImpl implements OrderItemMng{

	@Override
	public List<Object[]> profitTop(Long ctgid,Long typeid,Integer pageNo,Integer pageSize) {
		return dao.profitTop(ctgid,typeid,pageNo,pageSize);
	}

	@Override
	public Integer totalCount(Long ctgid,Long typeid) {
		return dao.totalCount(ctgid,typeid);
	}
	
	@Override
	public OrderItem findById(Long id){
		return dao.findById(id);
	}
	
	@Override
	public OrderItem updateByUpdater(OrderItem bean) {
		Updater<OrderItem> updater = new Updater<OrderItem>(bean);
		return dao.updateByUpdater(updater);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Pagination getPageByMember(Integer status,Long memberId,int pageNo,int pageSize){
		return dao.getPageForMember(memberId, status, pageNo, pageSize);
	}
	
	@Override
	public List<Object[]> getOrderItem(Date endTime,Date beginTime){
		List<Object[]> orderItemList=dao.getOrderItem(endTime,beginTime);
		return orderItemList;
	}
	

	@Override
	public OrderItem findByMember(Long memberId,Long productId,Long orderId){
		return dao.findByMember(memberId, productId,orderId);
	}
	
	@Override
	public Pagination getOrderItem(Integer pageNo,Integer pageSize,Long productId){
		return dao.getPageForProuct(productId, pageNo, pageSize);
	}
	
	@Autowired
	private OrderItemDao dao;
	
}

