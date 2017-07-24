package com.jspgou.core.dao.impl;

import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.core.dao.GlobalDao;
import com.jspgou.core.entity.Global;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
/**
* This class should preserve.
* @preserve
*/
@Repository
public class GlobalDaoImpl extends HibernateBaseDao<Global, Long>
    implements GlobalDao{
    @Override
	public Global findById(Long id){
        Global global =get(id);
        return global;
    }

    @Override
	public Global update(Global bean){
        getSession().update(bean);
        return bean;
    }

	@Override
	protected Class<Global> getEntityClass() {
		return Global.class;
	}
	
	public Global findIdkey(){
		String hql="from Global bean where 1=1";
	       Query query=getSession().createQuery(hql);
			query.setMaxResults(1);
	     return (Global)query.uniqueResult();
	   }
}
