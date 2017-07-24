package com.jspgou.core.dao.impl;

import java.util.List;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.dao.MemberDao;
import com.jspgou.core.entity.Member;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;
/**
* This class should preserve.
* @preserve
*/
@Repository
public class MemberDaoImpl extends HibernateBaseDao<Member, Long>
    implements MemberDao{
	
	@Override
	public Member getByUsername(String username){
		String f="from Member bean where bean.user.username=:username";
		return (Member)getSession().createQuery(f).setParameter("username", username).uniqueResult();
	}
	
    @Override
	public Member getByUserId(Long webId, Long userId){
      /*  String s = "from Member bean where bean.website.id=? and bean.user.id=?";
        return (Member)findUnique(s, new Object[] {
        		webId, userId
<<<<<<< .mine
        });*/
    	
    	String hql="from Member bean where bean.user.id=:userId";
        
    	Finder finder=Finder.create(hql).setParam("userId", userId);
    	if(webId!=null){
    		finder.append(" and bean.website.id=:webId").setParam("webId", webId);
    	}
    	List<Member>li=find(finder);
    	if(li.size()>0){
    		return li.get(0);
    	}else{
    		return null;
    	}
    }
    
    @Override
	public Member getByUserIdAndActive(String activationCode, Long userId){
        String s = "from Member bean where bean.activationCode=? and bean.user.id=?";
        return (Member)findUnique(s, new Object[] {
        		activationCode, userId
        });
    }

    @Override
	public Pagination getPage(int pageNo, int pageSize){
        org.hibernate.Criteria criteria = createCriteria(new Criterion[0]);
        Pagination pagination = findByCriteria(criteria, pageNo, pageSize);
        return pagination;
    }

    @Override
	public Member findById(Long id){
        Member entity =get(id);
        return entity;
    }

    @Override
	public Member save(Member bean){
        getSession().save(bean);
        return bean;
    }

    @Override
	public Member deleteById(Long id){
        Member entity =super.get(id);
        if(entity != null)
            getSession().delete(entity);
        return entity;
    }
    
	@Override
	protected Class<Member> getEntityClass() {
		return Member.class;
	}
}
