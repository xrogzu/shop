package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.OrderReturnDao;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.OrderReturn;
import com.jspgou.cms.entity.OrderReturn.OrderReturnStatus;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.OrderReturnMng;
import com.jspgou.cms.manager.ShopDictionaryMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class OrderReturnMngImpl implements OrderReturnMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Integer status,int pageNo, int pageSize) {
		Pagination page = dao.getPage(status,pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public OrderReturn findById(Long id) {
		OrderReturn entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public OrderReturn findByOrderId(Long orderId){
		List<OrderReturn> list=dao.findByOrderId(orderId);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public OrderReturn save(OrderReturn bean) {
		dao.save(bean);
		return bean;
	}
	
	@Override
	public OrderReturn save(OrderReturn bean,Order order, Long reasonId, Boolean delivery, String[] picPaths, String[] picDescs){
		bean.setOrder(order);
		bean.setShopDictionary(shopDictionaryMng.findById(reasonId));
		if(delivery){
			bean.setReturnType(OrderReturnStatus.SELLER_NODELIVERY_RETURN.getValue());
		}else{
			bean.setReturnType(OrderReturnStatus.SELLER_DELIVERY_RETURN.getValue());
		} 
		Long date=new Date().getTime()+order.getId();
		bean.setCode(String.valueOf(date));
		bean.setStatus(1);
		bean.setCreateTime(new Date());
		// 保存图片集
		if (picPaths != null && picPaths.length > 0) {
			for (int i = 0, len = picPaths.length; i < len; i++) {
				if (!StringUtils.isBlank(picPaths[i])) {
					bean.addToPictures(picPaths[i], picDescs[i]);
				}
			}
		}
		bean=dao.save(bean);
		return bean;
	}

	@Override
	public OrderReturn update(OrderReturn bean) {
		Updater<OrderReturn> updater = new Updater<OrderReturn>(bean);
		OrderReturn entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public OrderReturn deleteById(Long id) {
		//订单的退货id去掉，否则前台用户报错
		Order order=dao.findById(id).getOrder();
		order.setReturnOrder(null);
		orderMng.updateByUpdater(order);
		OrderReturn bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public OrderReturn[] deleteByIds(Long[] ids) {
		OrderReturn[] beans = new OrderReturn[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Autowired
	private OrderMng orderMng;
	@Autowired
	private OrderReturnDao dao;
	@Autowired
	private ShopDictionaryMng shopDictionaryMng;

	@Autowired
	public void setDao(OrderReturnDao dao) {
		this.dao = dao;
	}
}