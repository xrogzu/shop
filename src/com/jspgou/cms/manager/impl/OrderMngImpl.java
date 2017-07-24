package com.jspgou.cms.manager.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.OrderDao;
import com.jspgou.cms.entity.Cart;
import com.jspgou.cms.entity.CartItem;
import com.jspgou.cms.entity.MemberCoupon;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.OrderItem;
import com.jspgou.cms.entity.Payment;
import com.jspgou.cms.entity.PopularityItem;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.entity.Shipping;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberAddress;
import com.jspgou.cms.entity.ShopScore;
import com.jspgou.cms.entity.ShopScore.ScoreTypes;
import com.jspgou.cms.manager.CartItemMng;
import com.jspgou.cms.manager.CartMng;
import com.jspgou.cms.manager.GatheringMng;
import com.jspgou.cms.manager.MemberCouponMng;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.OrderReturnMng;
import com.jspgou.cms.manager.PaymentMng;
import com.jspgou.cms.manager.PopularityItemMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ShipmentsMng;
import com.jspgou.cms.manager.ShippingMng;
import com.jspgou.cms.manager.ShopMemberAddressMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.manager.ShopScoreMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.WebsiteMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class OrderMngImpl implements OrderMng {
	
	@Override
	public Pagination getPageForOrderReturn(Long memberId, int pageNo, int pageSize){
		return dao.getPageForOrderReturn(memberId, pageNo, pageSize);
	}
	
	//生成订单
	@Override
	public Order createOrder(Cart cart,Long[] cartItemId,Long shippingMethodId,Long deliveryInfo,Long paymentMethodId,
			String comments, String ip,ShopMember member, Long webId,String memberCouponId) {
		Website web = websiteMng.findById(webId);
		Long mcId = null;
		if (!StringUtils.isBlank(memberCouponId)){
			mcId = Long.parseLong(memberCouponId);
		}
		Payment pay=paymentMng.findById(paymentMethodId);
		
		Shipping shipping=shippingMng.findById(shippingMethodId);
		ShopMemberAddress address=shopMemberAddressMng.findById(deliveryInfo);
		
		Order order = new Order();
		order.setShipping(shipping);
		order.setWebsite(web);
		order.setMember(member);
        order.setPayment(pay);
		order.setIp(ip);
		order.setComments(comments);
		order.setStatus(1);//未确认
		order.setShippingStatus(1);//未发货
		order.setPaymentStatus(1);//未支付
		order.setReceiveName(address.getUsername());
		order.setReceiveAddress(getAddress(address));
		order.setReceiveMobile(address.getTel());
		order.setReceivePhone(address.getMobile());
		order.setReceiveZip(address.getPostCode());
		int score = 0;
		int weight=0;
		double price=0.00;
		Double  couponPrice=0.00;
		Double  popularityPrice =0.0;
		if(cart!=null){
			List<PopularityItem> popularityItems=popularityItemMng.getlist(cart.getId(),null);
			for(PopularityItem popularityItem:popularityItems){
				popularityPrice+=popularityItem.getPopularityGroup().getPrivilege()*popularityItem.getCount();
			}
		}
		if(mcId!=null){
			MemberCoupon memberCoupon = memberCouponMng.findById(mcId);
			if(memberCoupon!=null){
				if(memberCoupon.getCoupon().getIsusing()&&(!memberCoupon.getIsuse())){
					couponPrice = memberCoupon.getCoupon().getCouponPrice().doubleValue();
					memberCoupon.setIsuse(true);
					memberCouponMng.update(memberCoupon);
				}
			}
		}
	
		List<CartItem> itemList=new ArrayList<CartItem>();
		for(Long cId:cartItemId){
			itemList.add(cartItemMng.findById(cId));
		}
		for (CartItem item : itemList) {
			score = score + item.getProduct().getScore() * item.getCount();
			weight=weight+item.getProduct().getWeight()*item.getCount();
			if(item.getProductFash()!=null){
				 price+=item.getProductFash().getSalePrice()*item.getCount();
			}else{
			  price+=item.getProduct().getSalePrice()*item.getCount();
			}
		}
		if(member.getFreezeScore()!=null){
			member.setFreezeScore(score+member.getFreezeScore());//会员冻结的积分
		}else{
			member.setFreezeScore(score+0);//会员冻结的积分
		}
		order.setScore(score);
		order.setWeight((double)weight);
		order.setProductPrice(price);
		double freight=shipping.calPrice((double)weight);
		order.setFreight(freight);
		order.setTotal(freight+price-couponPrice-popularityPrice);
		Long date=new Date().getTime()+member.getId();
		order.setCode(date);

		CartItem cartItem=null;
		OrderItem orderItem=null;
		String productName="";
		for(int j=0;j<itemList.size();j++){
			orderItem=new OrderItem();
			cartItem=itemList.get(j);
			orderItem.setOrdeR(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setWebsite(web);
			orderItem.setCount(cartItem.getCount());
			if(cartItem.getProductFash()!=null){
				orderItem.setProductFash(cartItem.getProductFash());
				orderItem.setSalePrice(cartItem.getProductFash().getSalePrice());
			}else{
				orderItem.setSalePrice(cartItem.getProduct().getSalePrice());
			}
			productName+=orderItem.getProduct().getName();
			order.addToItems(orderItem);
		}
		order.setProductName(productName);
		List<PopularityItem> popularityItemList = popularityItemMng.getlist(cart.getId(),null);
		for(PopularityItem popularityItem:popularityItemList){
			popularityItemMng.deleteById(popularityItem.getId());
		}
		cart.getItems().removeAll(itemList);
		cartMng.update(cart);
		order=save(order);
		ShopScore shopScore=null;
		Product product=null;
		ProductFashion fashion=null;
		for(OrderItem item:order.getItems()){
			//处理库存
			product=item.getProduct();
			if(item.getProductFash()!=null){
				fashion=item.getProductFash();
				fashion.setStockCount(fashion.getStockCount()-item.getCount());
				product.setStockCount(product.getStockCount()-item.getCount());
				productFashionMng.update(fashion);
			}else{
				product.setStockCount(product.getStockCount()-item.getCount());
			}
			productMng.updateByUpdater(product);
			shopScore=new ShopScore();
			shopScore.setMember(member);
			shopScore.setName(cartItem.getProduct().getName());
			shopScore.setScoreTime(new Date());
			shopScore.setStatus(false);
			shopScore.setUseStatus(false);
			shopScore.setScoreType(ScoreTypes.ORDER_SCORE.getValue());
			shopScore.setScore(item.getProduct().getScore());
			shopScore.setCode(Long.toString(order.getCode()));
			shopScoreMng.save(shopScore);
		}
		return order;
	}
	
	
	public String getAddress(ShopMemberAddress address){
		String str = "";
		if(address.getProvince()!=null){
			str = str + address.getProvince()+"-";
		}
		if(address.getCity()!=null){
			str = str + address.getCity()+"-";
		}
		if(address.getCountry()!=null){
			str = str + address.getCountry()+"-";
		}
		str = str+address.getDetailaddress();
		return str;
	}
	
	@Override
	public Order updateByUpdater(Order bean) {
		Updater<Order> updater = new Updater<Order>(bean);
		return dao.updateByUpdater(updater);
	}
	
	@Override
	public Pagination getOrderByReturn(Long memberId,Integer pageNo,Integer pageSize){
		return dao.getOrderByReturn(memberId, pageNo, pageSize);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long webId, Long memberId,String productName,String userName, Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Integer paymentStatus,Integer shippingStatus,
			Long code,int pageNo, int pageSize) {
		Pagination page = dao.getPage(webId, memberId,productName,userName,paymentId,shippingId, 
				startTime,endTime,startOrderTotal,endOrderTotal,status,paymentStatus,shippingStatus,code, pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<Order> getlist(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newDate = new Date();	
		Date endDate = null;
		Calendar date = Calendar.getInstance();		
		date.setTime(newDate);		
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		try {
			endDate = sdf.parse(sdf.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Order> list = dao.getlist(endDate);
		return list;
	}
	
	@Override
	public void abolishOrder(){
		List<Order> orderList = getlist();
		for(Order order:orderList){
			order.setStatus(3);
			//处理库存
			for(OrderItem item:order.getItems()){	
				Product product=item.getProduct();
				if(item.getProductFash()!=null){
					ProductFashion fashion=item.getProductFash();
					fashion.setStockCount(fashion.getStockCount()+item.getCount());
					product.setStockCount(product.getStockCount()+item.getCount());
					productFashionMng.update(fashion);
				}else{
					product.setStockCount(product.getStockCount()+item.getCount());
				}
				productMng.updateByUpdater(product);
			}
			//会员冻结的积分
			ShopMember member = order.getMember();
			member.setFreezeScore(member.getFreezeScore()-order.getScore());
			shopMemberMng.update(member);
			List<ShopScore> list = shopScoreMng.getlist(Long.toString(order.getCode()));
			for(ShopScore s:list){
				shopScoreMng.deleteById(s.getId());
			}
			updateByUpdater(order);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long webId, Long memberId,String productName,String userName, Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,
			Long code,int pageNo, int pageSize) {
		Pagination page = dao.getPage(webId, memberId,productName,userName,paymentId,shippingId, 
				startTime,endTime,startOrderTotal,endOrderTotal,status,code, pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPageForMember(Long memberId, int pageNo, int pageSize) {
		return dao.getPageForMember(memberId, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageForMember1(Long memberId, int pageNo, int pageSize){
		return dao.getPageForMember1(memberId, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageForMember2(Long memberId, int pageNo, int pageSize){
		return dao.getPageForMember2(memberId, pageNo, pageSize);
	}
	@Override
	public Pagination getPageForMember3(Long memberId, int pageNo, int pageSize) {
		return dao.getPageForMember3(memberId, pageNo, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public Order findById(Long id) {
		Order entity = dao.findById(id);
		return entity;
	}
	
	@Override
	public Order findByCode(Long code){
		Order entity=dao.findByCode(code);
		return entity;
	}
	
	//销量
	@Override
	public void  updateSaleCount(Long id) {
		Order entity = dao.findById(id);
		for(OrderItem item:entity.getItems()){
			Product product = item.getProduct();
			product.setSaleCount(product.getSaleCount()+item.getCount());
			productMng.update(product);
		}
	}
	
	//利润
	@Override
	public void  updateliRun(Long id) {
		Order entity = dao.findById(id);
		for(OrderItem item:entity.getItems()){
			Product product = item.getProduct();
			ProductFashion productFashion = item.getProductFash();
			if(productFashion!=null){
				product.setLiRun(product.getLiRun()+item.getCount()*(productFashion.getSalePrice()-productFashion.getCostPrice()));
			}else{
				product.setLiRun(product.getLiRun()+item.getCount()*(product.getSalePrice()-product.getCostPrice()));
			}
			
			productMng.update(product);
		}
	}

	@Override
	public Order save(Order bean) {
		bean.init();
		dao.save(bean);
		return bean;
	}

	@Override
	public List<Object> getTotlaOrder() {
		return dao.getTotlaOrder();
	}

	@Override
	public BigDecimal getMemberMoneyByYear(Long memberId) {
		return dao.getMemberMoneyByYear(memberId);
	}

	@Override
	public Order deleteById(Long id) {
		Order bean=dao.findById(id);
		gatheringMng.deleteByorderId(id);
		shipmentMng.deleteByorderId(id);
		if(bean.getReturnOrder()!=null){
			orderReturnMng.deleteById(bean.getReturnOrder().getId());
		}
		if((bean.getShippingStatus()==1&&bean.getStatus()==1)||(bean.getShippingStatus()==1&&bean.getStatus()==2)){
			Set<OrderItem> set = bean.getItems();
			//处理库存
			for(OrderItem item:set){	
				Product product=item.getProduct();
				if(item.getProductFash()!=null){
					ProductFashion fashion=item.getProductFash();
					fashion.setStockCount(fashion.getStockCount()+item.getCount());
					product.setStockCount(product.getStockCount()+item.getCount());
					productFashionMng.update(fashion);
				}else{
					product.setStockCount(product.getStockCount()+item.getCount());
				}
				productMng.updateByUpdater(product);
			}
			//会员冻结的积分
			ShopMember member = bean.getMember();
			member.setFreezeScore(member.getFreezeScore()-bean.getScore());
			shopMemberMng.update(member);
			List<ShopScore> list = shopScoreMng.getlist(Long.toString(bean.getCode()));
			for(ShopScore s:list){
				shopScoreMng.deleteById(s.getId());
			}
		}	
		
		
		bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public Order[] deleteByIds(Long[] ids) {
		Order[] beans = new Order[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	//添加方法，根据订单状态获取所有订单
	public List<Order> getCountByStatus(Date startTime,Date endTime,Integer status){
		return dao.getCountByStatus(startTime,endTime,status);
	}
	
	public List<Order> getCountByStatus1(Date startTime,Date endTime,Integer status){
		return dao.getCountByStatus1(startTime,endTime,status);
	}
	
	
	public List<Order> getStatisticByYear(Integer year,Integer status){
		return dao.getStatisticByYear(year,status);
	}
	
	public List<Order> getStatisticByYear1(Integer year,Integer status){
		return dao.getStatisticByYear1(year,status);
	}
	
	
	public List<Order> getOrderList(Long webId,Long memberId,String productName,String userName,Long paymentId,Long shippingId, 
			Date startTime,Date endTime,Double startOrderTotal,Double endOrderTotal,Integer status,Long code,int firstResult, int maxResults){
		return dao.getOrderList(webId, memberId, productName, userName, paymentId, shippingId, startTime, endTime, startOrderTotal, endOrderTotal, status, code, firstResult, maxResults);
	}
	@Autowired
	private ProductMng productMng;
	@Autowired
	private ProductFashionMng productFashionMng;
	@Autowired
	private ShopScoreMng shopScoreMng;
	@Autowired
	private WebsiteMng websiteMng;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private CartMng cartMng;
	@Autowired
	private OrderDao dao;
	@Autowired
	private CartItemMng cartItemMng;
	@Autowired
	private GatheringMng gatheringMng;
	@Autowired
	private ShipmentsMng shipmentMng;
	@Autowired
	private OrderReturnMng orderReturnMng;
	@Autowired
	private MemberCouponMng memberCouponMng;
	@Autowired
	private PaymentMng paymentMng;
	@Autowired
	private ShopMemberAddressMng shopMemberAddressMng;
	@Autowired
	private ShippingMng shippingMng;
	@Autowired
	private PopularityItemMng popularityItemMng;
}