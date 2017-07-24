package com.jspgou.cms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jspgou.cms.entity.Order;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
public interface OrderDao {
	
	public Pagination getPageForMember(Long memberId, int pageNo, int pageSize);

	public Pagination getPageForMember1(Long memberId, int pageNo, int pageSize);
	
	public Pagination getPageForOrderReturn(Long memberId, int pageNo, int pageSize);
	
	public Pagination getPageForMember2(Long memberId, int pageNo, int pageSize);
	
	public Pagination getPageForMember3(Long memberId, int pageNo, int pageSize) ;
	
	public Pagination getPage(Long webId,Long memberId,String productName, String userName,Long paymentId, Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Integer paymentStatus,Integer shippingStatus,Long code,int pageNo, int pageSize);
   
	public Pagination getPage(Long webId,Long memberId,String productName, String userName,Long paymentId, Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Long code,int pageNo, int pageSize);
	
	public Order findById(Long id);
	
	public Order findByCode(Long code);

	public Order save(Order bean);

	public Order updateByUpdater(Updater<Order> updater);

	public Order deleteById(Long id);
	
	public List<Object> getTotlaOrder();
	
	public List<Order> getlist(Date endDate);
	
	public BigDecimal getMemberMoneyByYear(Long memberId);
	
	public Integer[] getOrderByMember(Long memberId);
	
	public Pagination getOrderByReturn(Long memberId,Integer pageNo,Integer pageSize);
	
	//添加方法，根据订单状态获取所有订单
	public List<Order> getCountByStatus(Date startTime,Date endTime,Integer status);
	
	public List<Order> getCountByStatus1(Date startTime,Date endTime,Integer status);
	
	public List<Order> getStatisticByYear(Integer year,Integer status);
	
	public List<Order> getStatisticByYear1(Integer year,Integer status);
	
	public List<Order> getOrderList(Long webId,Long memberId,String productName,String userName,Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Long code,int firstResult, int maxResults);
	
}