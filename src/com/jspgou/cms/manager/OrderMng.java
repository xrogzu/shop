package com.jspgou.cms.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
public interface OrderMng {

	public void  updateSaleCount(Long id);
	
	public void  updateliRun(Long id);
	
	public List<Order> getlist();
	
	public void abolishOrder();
	
	public Pagination getPageForOrderReturn(Long memberId, int pageNo, int pageSize);
	//生成订单
	public Order createOrder(Cart cart,Long[] cartItemId, Long shippingMethodId,Long deliveryInfo,Long paymentMethodId,
			String comments, String ip,ShopMember member, Long webId,String memberCouponId);

	public Pagination getPageForMember(Long memberId, int pageNo, int pageSize);
	
	public Pagination getPageForMember2(Long memberId, int pageNo, int pageSize);
	
	public Pagination getPageForMember1(Long memberId, int pageNo, int pageSize);

	public Pagination getPageForMember3(Long memberId, int pageNo, int pageSize) ;
	
	public Pagination getPage(Long webId,Long memberId,String productName,String userName,Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Integer paymentStatus,Integer shippingStatus,Long code,int pageNo, int pageSize);
	
	public Pagination getPage(Long webId,Long memberId,String productName,String userName,Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Long code,int pageNo, int pageSize);


	public Order findById(Long id);
	
	public Order findByCode(Long code);

	public Order save(Order bean);
	
	public Order updateByUpdater(Order updater);

	public Order deleteById(Long id);

	public Order[] deleteByIds(Long[] ids);
	
	public  List<Object> getTotlaOrder();
	
	public BigDecimal getMemberMoneyByYear(Long memberId);
	
	public Pagination getOrderByReturn(Long memberId,Integer pageNo,Integer pageSize);
	
	public List<Order> getCountByStatus(Date startTime,Date endTime,Integer status);
	
	public List<Order> getCountByStatus1(Date startTime,Date endTime,Integer status);
	
	public List<Order> getStatisticByYear(Integer year,Integer status);
	
	public List<Order> getStatisticByYear1(Integer year,Integer status);
	
	public List<Order> getOrderList(Long webId,Long memberId,String productName,String userName,Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Long code,int firstResult, int maxResults);
	

	
}