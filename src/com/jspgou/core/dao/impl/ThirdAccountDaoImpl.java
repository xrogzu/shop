package com.jspgou.core.dao.impl;

import java.util.List;

import com.jspgou.core.dao.ThirdAccountDao;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.ThirdAccount;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import org.apache.commons.lang.StringUtils;





@Repository
public class ThirdAccountDaoImpl extends HibernateBaseDao<ThirdAccount, Long> implements ThirdAccountDao {
	public Pagination getPage(String username,String source,int pageNo, int pageSize) {
		String hql="from ThirdAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.username like :username").setParam("username", "%"+username+"%");
		}
		if(StringUtils.isNotBlank(source)){
			f.append(" and bean.source=:source").setParam("source", source);
		}
		return find(f, pageNo, pageSize);
	}

	public ThirdAccount findById(Long id) {
		ThirdAccount entity = get(id);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public ThirdAccount findByKey(String key){
		String hql="from ThirdAccount bean where bean.accountKey=:accountKey";
		Finder f=Finder.create(hql).setParam("accountKey", key);
		List<ThirdAccount>li= find(f);
		if(li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}

	public ThirdAccount save(ThirdAccount bean) {
		getSession().save(bean);
		return bean;
	}

	public ThirdAccount deleteById(Long id) {
		ThirdAccount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<ThirdAccount> getEntityClass() {
		return ThirdAccount.class;
	}
}