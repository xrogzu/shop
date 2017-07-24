package com.jspgou.cms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.OrderItemDao;
import com.jspgou.cms.entity.OrderItem;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class OrderItemDaoImpl extends HibernateBaseDao<OrderItem, Long> implements OrderItemDao{

	@Override
	protected Class<OrderItem> getEntityClass() {
		// TODO Auto-generated method stub
		return OrderItem.class;
	}

	@Override
	public List<Object[]> profitTop(Long ctgid,Long typeid,Integer pageNo,Integer pageSize) {
		Query query = getSession().createQuery(getHQL(ctgid, typeid));
		if(ctgid!=null){
			query.setLong("ctgid", ctgid);
		}
		if(typeid!=null){
			query.setLong("typeid", typeid);
		}
		Iterator iter = query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).iterate();
		List<Object[]> list = new ArrayList<Object[]>();
		while(iter.hasNext()){
			list.add((Object[]) iter.next());
		}
		return list;
	}
	@Override
	public Integer totalCount(Long ctgid,Long typeid) {
		Integer allPage = 0;
		Query query = getSession().createQuery(getCountHQL(ctgid, typeid));
		if(ctgid!=null){
			query.setLong("ctgid", ctgid);
		}
		if(typeid!=null){
			query.setLong("typeid", typeid);
		}
		Iterator iterator = query.iterate();
		if(iterator.hasNext()){
			allPage = Integer.parseInt(iterator.next()+"");
		}
		return allPage;
	}

	
	@Override
	public Pagination getPageForMember(Long memberId, Integer status,int pageNo, int pageSize) {//会员中心购买记录
		Finder f = Finder.create("select bean from OrderItem bean");
		f.append(" join bean.ordeR indent");
		f.append(" where indent.member.id=:memberId and indent.status=:status");
		f.setParam("memberId", memberId).setParam("status", status);
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
	
	//商品购买记录
	@Override
	public Pagination getPageForProuct(Long productId,int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from OrderItem bean");
		if(productId!=null){
			f.append(" where bean.product.id=:productId");
			f.setParam("productId", productId);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
	
	public static String getHQL(Long ctgid,Long typeid){
		StringBuffer sb = new StringBuffer();
		sb.append("select bean.product.id, sum(bean.count), sum((bean.finalPrice-bean.costPrice)*bean.count) ");
		sb.append(" from OrderItem bean where 1=1 ");
		if(ctgid!=null){
			sb.append(" and bean.product.category.id=:ctgid ");
		}
		if(typeid!=null){
			sb.append(" and bean.product.type.id=:typeid ");
		}
		sb.append(" group by bean.product.id order by sum((bean.finalPrice-bean.costPrice)*bean.count) desc");
		return sb.toString();
	}
	public static String getCountHQL(Long ctgid,Long typeid){
		StringBuffer sb = new StringBuffer();
		sb.append("select count(DISTINCT bean.product.id) ");
		sb.append(" from OrderItem bean where 1=1 ");
		if(ctgid!=null){
			sb.append(" and bean.product.category.id=:ctgid ");
		}
		if(typeid!=null){
			sb.append(" and bean.product.type.id=:typeid ");
		}
		return sb.toString();
	}

	@Override
	public List<Object[]> getOrderItem(Date endTime,Date beginTime){
		String hql="select bean,sum(bean.count) from OrderItem bean" +
				" where bean.ordeR.createTime<=:endTime and bean.ordeR.createTime>=:beginTime"+
				 " group by bean.id,bean.productFash.attitude,bean.count,bean.salePrice,bean.memberPrice,bean.costPrice,bean.finalPrice,bean.seckillprice,bean.website.id,bean.product.id,bean.ordeR.id,bean.productFash.id " +
				 		"order by sum(bean.count) desc";
		List<Object[]> list=this.getSession().createQuery(hql).setParameter("endTime", endTime).setParameter("beginTime", beginTime).list();
		return list;
	}
	
	@Override
	public OrderItem findById(Long id) {
		OrderItem entity = get(id);
		return entity;
	}
	
	@Override
	public OrderItem findByMember(Long memberId,Long productId,Long orderId){
		String hql="from OrderItem bean where bean.product.id=:productId and bean.ordeR.member.id=:memberId ";
		if(orderId!=null){
			String hql1=hql+" and bean.ordeR.id=:orderId ";
		    Iterator<OrderItem> it=this.getSession().createQuery(hql1).setParameter("memberId", memberId).setParameter("productId", productId).setParameter("orderId", orderId)
		    	    .iterate();
		    if(it.hasNext()){
		    	return it.next();
		    }else{
		    	return null;
		    }  
		}else{
			 Iterator<OrderItem> it=this.getSession().createQuery(hql).setParameter("memberId", memberId).setParameter("productId", productId)
			    	    .iterate();
			    if(it.hasNext()){
			    	return it.next();
			    }else{
			    	return null;
			    }
		}

	    
	}
	

	
}

