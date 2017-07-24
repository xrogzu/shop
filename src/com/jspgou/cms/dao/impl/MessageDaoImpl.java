package com.jspgou.cms.dao.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.MessageDao;
import com.jspgou.cms.entity.Message;

@Repository
public class MessageDaoImpl extends HibernateBaseDao<Message, Long> implements MessageDao {

	public Pagination getPage(Long sendMemberId,int pageNo, int pageSize) {
		/*Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;*/
		
		String hql = " select msg from Message msg where 1=1 ";
		Finder finder = Finder.create(hql);
		if (sendMemberId != null) {
			finder.append(" and msg.msgSendUser.id=:sendMemberId").setParam(
					"sendMemberId", sendMemberId);
		}
		finder.append(" order by msg.id desc");
		return find(finder, pageNo, pageSize);
	}

	public Message findById(Long id) {
		Message entity = get(id);
		return entity;
	}

	public Message save(Message bean) {
		getSession().save(bean);
		return bean;
	}

	public Message deleteById(Long id) {
		Message entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<Message> getEntityClass() {
		return Message.class;
	}
}