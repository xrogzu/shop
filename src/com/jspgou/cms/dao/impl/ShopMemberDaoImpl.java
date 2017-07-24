package com.jspgou.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Member;
import com.jspgou.core.manager.MemberMng;
import com.jspgou.cms.dao.ShopMemberDao;
import com.jspgou.cms.entity.ShopMember;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShopMemberDaoImpl extends HibernateBaseDao<ShopMember, Long>
		implements ShopMemberDao {
	@Override
	public Pagination getPage(Long webId, int pageNo, int pageSize) {
		Finder f = Finder
				.create("from ShopMember bean where bean.website.id=:webId order by bean.id desc");
		f.setParam("webId", webId);
		return find(f, pageNo, pageSize);
	}

	@Override
	public ShopMember getByUserId(Long webId, Long userId) {
		Member coreMember = memberMng.getByUserId(webId, userId);
		if (coreMember != null) {
			return findById(coreMember.getId());
		} else {
			return null;
		}
	}
	
	@Override
	public ShopMember findById(Long id) {
		ShopMember entity = get(id);
		return entity;
	}

	@Override
	public ShopMember save(ShopMember bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ShopMember deleteById(Long id) {
		ShopMember entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ShopMember> getEntityClass() {
		return ShopMember.class;
	}
	
	public ShopMember findUsername(String username) {
		String hql = "from ShopMember bean where bean.member.user.username=:realName";
		Query query = getSession().createQuery(hql);
		query.setParameter("realName", username);
		query.setMaxResults(1);
		return (ShopMember) query.uniqueResult();
	}
	
	/**
	 * 输入用户名查找是否是唯一的
	 */
	public ShopMember findByUsername(String realName){
		return findUniqueByProperty("realName",realName);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ShopMember> getList(String realName, Long groupId) {
		Finder f=Finder.create("select bean from ShopMember bean where 1=1");
		if(!StringUtils.isBlank(realName)){
			f.append(" and bean.realName like :realName");
			f.setParam("realName", "%"+realName+"%");
		}
		if(groupId!=null){
			f.append(" and bean.group.id=:groupId");
			f.setParam("groupId", groupId);
		}
		f.append(" order by bean.id desc");
		return find(f);
	}
	
	
	public int countByUsername(String realName) {
		String hql="select count(*) from ShopMember bean where bean.member.user.username=:realName";
		Query query =getSession().createQuery(hql);
		query.setParameter("realName", realName);
		return ((Number) query.iterate().next()).intValue();
	}
	
	@Autowired
	private MemberMng memberMng;
}