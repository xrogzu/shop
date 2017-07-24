package com.jspgou.cms.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.OrderDao;
import com.jspgou.cms.entity.Order;

/**
 * This class should preserve.
 * 
 * @preserve
 */
@Repository
public class OrderDaoImpl extends HibernateBaseDao<Order, Long> implements
		OrderDao {
	@Override
	public Pagination getPageForMember(Long memberId, int pageNo, int pageSize) {// 会员中心(我的王成订单)
		Finder f = Finder
				.create("from Order bean where bean.member.id=:memberId");
		f.setParam("memberId", memberId);
		f.append(" and bean.status=40");
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPageForOrderReturn(Long memberId, int pageNo,
			int pageSize) {// 会员中心(退货订单)
		Finder f = Finder
				.create("from Order bean where bean.returnOrder.id is not null");
		if (memberId != null) {
			f.append(" and bean.member.id=:memberId");
			f.setParam("memberId", memberId);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPageForMember1(Long memberId, int pageNo, int pageSize) {// 会员中心(我的待付款订单)
		Finder f = Finder
				.create("from Order bean where bean.member.id=:memberId");
		f.setParam("memberId", memberId);
		f.append(" and bean.status>=10 and bean.status<=19");
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPageForMember2(Long memberId, int pageNo, int pageSize) {// 会员中心(我的处理中订单)
		Finder f = Finder
				.create("from Order bean where bean.member.id=:memberId");
		f.setParam("memberId", memberId);
		f.append(" and bean.status>=20 and bean.status<=29");
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public List<Order> getlist(Date endDate) {
		Finder f = Finder.create("from Order bean where bean.payment.type=1");
		f.append(" and bean.paymentStatus=:paymentStatus");
		f.append(" and bean.createTime<:endTime");
		f.append(" and (bean.status=:checking or bean.status=:checked)");
		f.setParam("checking", CHECKING);
		f.setParam("checked", CHECKED);
		f.setParam("endTime", endDate);
		f.setParam("paymentStatus", CHECKING);
		return find(f);
	}

	@Override
	public Pagination getPageForMember3(Long memberId, int pageNo, int pageSize) {// 会员中心(全部订单)
		Finder f = Finder
				.create("from Order bean where bean.member.id=:memberId");
		f.setParam("memberId", memberId);
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPage(Long webId, Long memberId, String productName,
			String userName, Long paymentId, Long shippingId, Date startTime,
			Date endTime, Double startOrderTotal, Double endOrderTotal,
			Integer status, Integer paymentStatus, Integer shippingStatus,
			Long code, int pageNo, int pageSize) {
		Finder f = Finder
				.create("from Order bean where bean.returnOrder.id is null ");
		if (webId != null) {
			f.append(" and bean.website.id=:webId");
			f.setParam("webId", webId);
		}
		if (memberId != null) {
			f.append(" and bean.member.id=:memberId");
			f.setParam("memberId", memberId);
		}
		if (!StringUtils.isBlank(userName)) {
			f.append(" and bean.receiveName like:userName");
			f.setParam("userName", "%" + userName + "%");
		}
		if (!StringUtils.isBlank(productName)) {
			f.append(" and bean.productName like:productName");
			f.setParam("productName", "%" + productName + "%");
		}
		if (paymentId != null) {
			f.append(" and bean.payment.id=:paymentId");
			f.setParam("paymentId", paymentId);
		}
		if (shippingId != null) {
			f.append(" and bean.shipping.id=:shippingId");
			f.setParam("shippingId", shippingId);
		}
		if (startTime != null) {
			f.append(" and bean.createTime>:startTime");
			f.setParam("startTime", startTime);
		}
		if (endTime != null) {
			f.append(" and bean.createTime<:endTime");
			f.setParam("endTime", endTime);
		}
		if (startOrderTotal != null) {
			f.append(" and bean.total>=:startOrderTotal");
			f.setParam("startOrderTotal", startOrderTotal);
		}
		if (endOrderTotal != null) {
			f.append(" and bean.total<=:endOrderTotal");
			f.setParam("endOrderTotal", endOrderTotal);
		}
		if (status != null) {
			if (status == 5) {
				f.append(" and (bean.status=:checking or bean.status=:checked)");
				f.append(" and bean.paymentStatus=:payment");
				f.setParam("checking", CHECKING);
				f.setParam("checked", CHECKED);
				f.setParam("payment", CHECKING);
			} else if (status == 6) {
				f.append(" and ((bean.payment.type=1 and bean.paymentStatus=:payment)or(bean.payment.type=2))");
				f.append(" and bean.status=:checked");
				f.append(" and bean.shippingStatus=:shipping");
				f.setParam("checked", CHECKED);
				f.setParam("shipping", CHECKING);
				f.setParam("payment", CHECKING);
			} else {
				f.append(" and bean.status=:status");
				f.setParam("status", status);
			}
		}
		if (paymentStatus != null) {
			f.append(" and bean.paymentStatus=:paymentStatus");
			f.setParam("paymentStatus", paymentStatus);
		}
		if (shippingStatus != null) {
			f.append(" and bean.shippingStatus=:shippingStatus");
			f.setParam("shippingStatus", shippingStatus);
		}
		if (code != null) {
			f.append(" and bean.code=:code");
			f.setParam("code", code);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPage(Long webId, Long memberId, String productName,
			String userName, Long paymentId, Long shippingId, Date startTime,
			Date endTime, Double startOrderTotal, Double endOrderTotal,
			Integer status, Long code, int pageNo, int pageSize) {
		Finder f = Finder.create("from Order bean where 1=1 ");
		if (webId != null) {
			f.append(" and bean.website.id=:webId");
			f.setParam("webId", webId);
		}
		if (memberId != null) {
			f.append(" and bean.member.id=:memberId");
			f.setParam("memberId", memberId);
		}
		if (!StringUtils.isBlank(userName)) {
			f.append(" and bean.receiveName like:userName");
			f.setParam("userName", "%" + userName + "%");
		}
		if (!StringUtils.isBlank(productName)) {
			f.append(" and bean.productName like:productName");
			f.setParam("productName", "%" + productName + "%");
		}
		if (paymentId != null) {
			f.append(" and bean.payment.id=:paymentId");
			f.setParam("paymentId", paymentId);
		}
		if (shippingId != null) {
			f.append(" and bean.shipping.id=:shippingId");
			f.setParam("shippingId", shippingId);
		}
		if (startTime != null) {
			f.append(" and bean.createTime>:startTime");
			f.setParam("startTime", startTime);
		}
		if (endTime != null) {
			f.append(" and bean.createTime<:endTime");
			f.setParam("endTime", endTime);
		}
		if (startOrderTotal != null) {
			f.append(" and bean.total>=:startOrderTotal");
			f.setParam("startOrderTotal", startOrderTotal);
		}
		if (endOrderTotal != null) {
			f.append(" and bean.total<=:endOrderTotal");
			f.setParam("endOrderTotal", endOrderTotal);
		}
		if (status != null) {
			if (status == 5) {
				f.append(" and (bean.status=:checking or bean.status=:checked)");
				f.append(" and bean.paymentStatus=:payment");
				f.setParam("checking", CHECKING);
				f.setParam("checked", CHECKED);
				f.setParam("payment", CHECKING);
			} else if (status == 6) {
				f.append(" and (bean.status=:checking or bean.status=:checked)");
				f.append(" and bean.shippingStatus=:shipping");
				f.setParam("checking", CHECKING);
				f.setParam("checked", CHECKED);
				f.setParam("shipping", CHECKED);
			} else {
				f.append(" and bean.status=:status");
				f.setParam("status", status);
			}

		}
		if (code != null) {
			f.append(" and bean.code=:code");
			f.setParam("code", code);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public List<Object> getTotlaOrder() {// 后台首页统计函数（wang ze wu）
		List<Object> o = new ArrayList<Object>();
		Long totalOrder = (Long) this.getSession()
				.createQuery("select count(*) from Order bean").uniqueResult();
		Long noCompleteOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.status between 1 and 2")
				.uniqueResult();
		Calendar c = Calendar.getInstance();
		String month = (c.get(Calendar.MONTH) + 1) + "";
		String year = c.get(Calendar.YEAR) + "";
		if (month.length() == 1) {
			month = "0" + month;
		} else {
			month = month;
		}
		String str = year + "-" + month;
		Long thisMontyOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.createTime like :time")
				.setString("time", "%" + str + "%").uniqueResult();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String tady = sf.format(new Date());
		Long todayOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.createTime like :tody")
				.setString("tody", "%" + tady + "%").uniqueResult();
		Long totalProduct = (Long) this.getSession()
				.createQuery("select count(*) from Product bean")
				.uniqueResult();
		Long newProductMonth = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Product bean where bean.createTime like :time")
				.setString("time", "%" + str + "%").uniqueResult();
		Long dateProduct = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Product bean where bean.createTime like :time")
				.setString("time", "%" + tady + "%").uniqueResult();
		Long putawayProduct = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Product bean where bean.onSale=true")
				.uniqueResult();

		Long totalMember = (Long) this.getSession()
				.createQuery("select count(*) from ShopMember bean")
				.uniqueResult();
		Long totalMonthMember = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Member bean where bean.createTime like :time")
				.setString("time", "%" + str + "%").uniqueResult();
		Long totalDateMember = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Member bean where bean.createTime like :time")
				.setString("time", "%" + tady + "%").uniqueResult();
		c.add(Calendar.DATE, -7);
		Date oldDate = c.getTime();
		Long date7Member = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Member bean where bean.createTime >:time")
				.setParameter("time", oldDate).uniqueResult();

		Long[] cc = new Long[12];
		cc[0] = totalOrder == null ? 0 : totalOrder;
		cc[1] = noCompleteOrder == null ? 0 : noCompleteOrder;
		cc[2] = thisMontyOrder == null ? 0 : thisMontyOrder;
		cc[3] = todayOrder == null ? 0 : todayOrder;

		cc[4] = totalProduct == null ? 0 : totalProduct;
		cc[5] = newProductMonth == null ? 0 : newProductMonth;
		cc[6] = dateProduct == null ? 0 : dateProduct;

		cc[7] = totalMember == null ? 0 : totalMember;
		cc[8] = totalMonthMember == null ? 0 : totalMonthMember;
		cc[9] = totalDateMember == null ? 0 : totalDateMember;
		cc[10] = date7Member == null ? 0 : date7Member;
		cc[11] = putawayProduct == null ? 0 : putawayProduct;

		o.add(cc);
		return o;
	}

	@Override
	public BigDecimal getMemberMoneyByYear(Long memberId) {// 会员今年的消费(wang ze
															// wu)
		Calendar c = Calendar.getInstance();
		String year = "" + c.get(Calendar.YEAR);
		Query q = this
				.getSession()
				.createQuery(
						"select sum((bean.salePrice)* bean.count) from OrderItem bean"
								+ " where bean.ordeR.member.id=:id and bean.ordeR.createTime like:time and bean.ordeR.status=4");
		q.setParameter("id", memberId).setString("time", "%" + year + "%");
		Double v1 = (Double) q.uniqueResult();
		if (v1 == null) {
			v1 = 0.0;
		}
		return new BigDecimal(v1);
	}

	@Override
	public Integer[] getOrderByMember(Long memberId) {// 会员我的订单（完成，未完成）
		Long succOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.member.id=:id")
				.setParameter("id", memberId).uniqueResult();
		Long failOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.member.id=:id")
				.setParameter("id", memberId).uniqueResult();

		Long totalOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.member.id=:id")// 全部订单
				.setParameter("id", memberId).uniqueResult();

		Long pendIngOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.member.id=:id")// 待付款订单
				.setParameter("id", memberId).uniqueResult();

		Long proceOrder = (Long) this
				.getSession()
				.createQuery(
						"select count(*) from Order bean where bean.member.id=:id")// 待处理订单
				.setParameter("id", memberId).uniqueResult();

		Integer[] orders = new Integer[5];
		orders[0] = succOrder.intValue();
		orders[1] = failOrder.intValue();// 退回订单
		orders[2] = totalOrder.intValue();
		orders[3] = pendIngOrder.intValue();
		orders[4] = proceOrder.intValue();
		return orders;
	}

	@Override
	public Pagination getOrderByReturn(Long memberId, Integer pageNo,
			Integer pageSize) {// 会员中心，退回订单
		Finder f = Finder
				.create("from Order bean where bean.member.id=:id and bean.status=41");
		f.setParam("id", memberId);
		return this.find(f, pageNo, pageSize);
	}

	@Override
	public Order findById(Long id) {
		Finder entity = Finder.create("from Order bean where bean.id=:id");
		entity.setParam("id", id);
		List<Order> list = find(entity);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Order findByCode(Long code) {
		Finder f = Finder.create("from Order bean where bean.code=:code")
				.setParam("code", code);
		List<Order> list = find(f);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Order save(Order bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Order deleteById(Long id) {
		Order entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Order> getEntityClass() {
		return Order.class;
	}

	// 添加方法，根据订单状态获取所有订单
	public List<Order> getCountByStatus(Date startTime, Date endTime,
			Integer status) {
		Finder finder = Finder
				.create("SELECT DATE_FORMAT(createTime, '%Y-%m-%d' ), COUNT(*) from Order bean where 1=1");

		if (startTime != null) {
			finder.append(" and bean.createTime>:startTime");
			finder.setParam("startTime", startTime);
		}
		if (endTime != null) {
			finder.append(" and bean.createTime<=:endTime");
			finder.setParam("endTime", endTime);

		}
		if (status != null) {
			finder.append(" and bean.status=:status");
			finder.setParam("status", status);

		}
		finder.append(" GROUP BY DATE_FORMAT( createTime, '%Y-%m-%d' )");

		return find(finder);
	}

	public List<Order> getCountByStatus1(Date startTime, Date endTime,
			Integer status) {
		Finder finder = Finder
				.create("SELECT DATE_FORMAT(createTime, '%Y-%m-%d' ), sum(bean.total) from Order bean where 1=1");

		if (startTime != null) {
			finder.append(" and bean.createTime>:startTime");
			finder.setParam("startTime", startTime);
		}
		if (endTime != null) {
			finder.append(" and bean.createTime<=:endTime");
			finder.setParam("endTime", endTime);

		}
		if (status != null) {
			finder.append(" and bean.status=:status");
			finder.setParam("status", status);

		}
		finder.append(" GROUP BY DATE_FORMAT( createTime, '%Y-%m-%d' )");

		return find(finder);
	}

	// 根据订单状态，获取年度统计
	// 订单统计
	public List<Order> getStatisticByYear(Integer year, Integer status) {
		Finder finder = Finder
				.create("SELECT bean.createTime,COUNT(*) from Order bean where 1=1");
		if (status != null) {
			finder.append(" and bean.status=:status");
			finder.setParam("status", status);
		}
		if (year != null) {
			String y = year.toString();
			finder.append(" and DATE_FORMAT(bean.createTime,'%Y') =:year");
			finder.setParam("year", y);
		}
		finder.append(" GROUP BY DATE_FORMAT(bean.createTime,'%Y%m')");
		return find(finder);
	}

	// 根据订单状态，获取年度统计
	// 销售额统计
	public List<Order> getStatisticByYear1(Integer year, Integer status) {
		Finder finder = Finder
				.create("SELECT bean.createTime,sum(bean.total) from Order bean where 1=1");
		if (status != null) {
			finder.append(" and bean.status=:status");
			finder.setParam("status", status);
		}
		if (year != null) {
			String y = year.toString();
			finder.append(" and DATE_FORMAT(bean.createTime,'%Y') =:year");
			finder.setParam("year", y);
		}
		finder.append(" GROUP BY DATE_FORMAT(bean.createTime,'%Y%m')");
		return find(finder);
	}

	public List<Order> getOrderList(Long webId, Long memberId,
			String productName, String userName, Long paymentId,
			Long shippingId, Date startTime, Date endTime,
			Double startOrderTotal, Double endOrderTotal, Integer status,
			Long code, int firstResult, int maxResults) {
		Finder f = Finder.create("from Order bean where 1=1");
		if (webId != null) {
			f.append(" and bean.website.id=:webId");
			f.setParam("webId", webId);
		}
		if (memberId != null) {
			f.append(" and bean.member.id=:memberId");
			f.setParam("memberId", memberId);
		}
		if (!StringUtils.isBlank(productName)) {
			f.append(" and bean.productName like:productName");
			f.setParam("productName", "%" + productName + "%");
		}
		if (!StringUtils.isBlank("userName")) {
			f.append(" and bean.receiveName like:userName");
			f.setParam("userName", "%" + userName + "%");
		}
		if (paymentId != null) {
			f.append("and bean.payment.id=:paymentId");
			f.setParam("paymentId", paymentId);
		}
		if (shippingId != null) {
			f.append(" and bean.shipping.id=:shippingId");
			f.setParam("shippingId", shippingId);
		}
		if (startTime != null) {
			f.append(" and bean.createTime>:startTime");
			f.setParam("startTime", startTime);
		}
		if (endTime != null) {
			f.append(" and bean.createTime<:endTime");
			f.setParam("endTime", endTime);
		}
		if (startOrderTotal != null) {
			f.append(" and bean.total>=:startOrderTotal");
			f.setParam("startOrderTotal", startOrderTotal);
		}
		if (endOrderTotal != null) {
			f.append(" and bean.total<=:endOrderTotal");
			f.setParam("endOrderTotal", endOrderTotal);
		}
		if (status != null) {
			f.append(" and bean.status=:status");
			f.setParam("status", status);
		}
		f.setFirstResult(firstResult);
		f.setMaxResults(maxResults);
		f.append(" order by bean.id desc");
		return find(f);
	}

	/**
	 * 未确认
	 */
	public static final Integer CHECKING = 1;
	/**
	 * 已确认
	 */
	public static final Integer CHECKED = 2;

}