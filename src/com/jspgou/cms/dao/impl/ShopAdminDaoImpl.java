package com.jspgou.cms.dao.impl;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopAdminDao;
import com.jspgou.cms.entity.ShopAdmin;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShopAdminDaoImpl extends HibernateBaseDao<ShopAdmin, Long>
		implements ShopAdminDao {
	@Override
	public Pagination getPage(Long webId, int pageNo, int pageSize) {
		Finder f = Finder
				.create("from ShopAdmin bean where bean.website.id=:webId order by bean.id desc");
		f.setParam("webId", webId);
		return find(f, pageNo, pageSize);
	}

	@Override
	public ShopAdmin findById(Long id) {
		ShopAdmin entity = get(id);
		return entity;
	}

	@Override
	public ShopAdmin save(ShopAdmin bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ShopAdmin deleteById(Long id) {
		ShopAdmin entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ShopAdmin> getEntityClass() {
		return ShopAdmin.class;
	}

	@Override
	public ShopAdmin getByUsername(String username) {
		String hql="from ShopAdmin bean where bean.admin.user.username=:username";
		return (ShopAdmin)getSession().createQuery(hql).setParameter("username", username).uniqueResult();
	}
	

	//*添加方法,findByName
	public ShopAdmin findByName(String name){
		String sql="from ShopAdmin bean where bean.admin.user.username=:username";
		return (ShopAdmin) getSession().createQuery(sql).setParameter("username", name).uniqueResult();
	}
}