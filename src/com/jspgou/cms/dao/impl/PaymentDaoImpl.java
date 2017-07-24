package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.PaymentDao;
import com.jspgou.cms.entity.Payment;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class PaymentDaoImpl extends HibernateBaseDao<Payment, Long> implements
		PaymentDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<Payment> getListForCart(Long webId, boolean cacheable) {
		String hql = "from Payment bean where bean.website.id=:webId and bean.disabled=false order by bean.priority";
		return getSession().createQuery(hql).setParameter("webId", webId)
				.setCacheable(cacheable).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Payment> getList(Long webId, boolean all) {
		Finder f = Finder
				.create("from Payment bean where bean.website.id=:webId");
		f.setParam("webId", webId);
		if (!all) {
			f.append(" bean.disabled=false order by bean.priority");
		} else {
			f.append(" order by bean.disabled,bean.priority");
		}
		return find(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Payment> getByCode(String code, Long webId) {
		String hql = "from Payment bean where bean.website.id=? and bean.code=?";
		return find(hql, webId, code);
	}

	@Override
	public Payment findById(Long id) {
		Payment entity = get(id);
		return entity;
	}

	@Override
	public Payment save(Payment bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Payment deleteById(Long id) {
		Payment entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Payment> getEntityClass() {
		return Payment.class;
	}
}