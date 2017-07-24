package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.cms.dao.CategoryDao;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.Category;

/**
 * CategoryDao实现类
 * @author liufang
 * This class should preserve.
 * @preserve
 */
@Repository
public class CategoryDaoImpl extends HibernateBaseDao<Category, Long> implements
		CategoryDao {
	@Override
	public Category getByPath(Long webId, String path, boolean cacheable) {
		String hql = "from Category bean where bean.website.id=? and bean.path=?";
		return (Category) createQuery(hql, webId, path).setCacheable(cacheable)
				.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getListForParent(Long webId, Long ctgId) {
		Finder f = Finder.create("select node");
		f.append(" from Category node,Category exclude");
		f.append(" where ex.id=:ctgId and node.website.id=?");
		f.append(" and node.lft<exclude.lft and node.rgt>exclude.rgt");
		f.append(" order by node.priority");
		f.setParam("webId", webId);
		f.setParam("ctgId", ctgId);
		return find(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getListForChild(Long webId, Long ctgId) {
		Finder f = Finder.create("select node");
		f.append(" from Category node, Category parent");
		f.append(" where parent.id=:ctgId and node.website.id=:webId");
		f.append(" and node.lft>=parent.lft and node.rgt<=parent.rgt");
		f.setParam("webId", webId);
		f.setParam("ctgId", ctgId);
		return find(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getTopList(Long webId, boolean cacheable) {
		String hql = "from Category bean where bean.website.id=? and bean.parent.id is null order by bean.priority";
		return createQuery(hql, webId).setCacheable(cacheable).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getChildList(Long webId, Long parentId){
		Finder f = Finder.create("from Category bean");
		f.append(" where bean.parent.id=:parentId");
		f.setParam("parentId", parentId);
		f.append(" order by bean.priority asc,bean.id asc");
		return find(f);
	}

	@Override
	public int countPath(Long webId, String path) {
		String hql = "select count(*) from Category bean where bean.website.id=:webId and bean.path=:path";
		return ((Number) getSession().createQuery(hql).setParameter("webId",
				webId).setParameter("path", path).iterate().next()).intValue();

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getListByptype(Long webId,Long pTypeId,Integer count){
		String hql="from Category bean where bean.website.id=? and bean.type.id=?";
		if(count!=null&&count!=0){
			
			return this.getSession().createQuery(hql).setParameter(0, webId).setParameter(1, pTypeId).setFirstResult(0)
			.setMaxResults(count).list();
		}
		return this.getSession().createQuery(hql).setParameter(0, webId).setParameter(1, pTypeId).list();
	}

	@Override
	public Category findById(Long id) {
		Category entity = get(id);
		return entity;
	}

	@Override
	public Category save(Category bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Category deleteById(Long id) {
		Category entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Category> getEntityClass() {
		return Category.class;
	}
}