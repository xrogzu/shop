package com.jspgou.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.core.dao.WebsiteExtDao;
import com.jspgou.core.entity.WebsiteExt;

@Service
@Transactional
public class WebsiteExtDaoImpl extends HibernateBaseDao<WebsiteExt,String> implements WebsiteExtDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<WebsiteExt> getList() {
		String hql="from WebsiteExt";
		return find(hql);
	}

	@Override
	protected Class<WebsiteExt> getEntityClass() {
		return WebsiteExt.class;
	}

}
