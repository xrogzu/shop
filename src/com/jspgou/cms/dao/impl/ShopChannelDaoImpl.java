package com.jspgou.cms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.ShopChannelDao;
import com.jspgou.cms.entity.ShopChannel;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;

/**
* This class should preserve.
* @preserve
*/
@Repository
public class ShopChannelDaoImpl extends HibernateBaseDao<ShopChannel, Long>
		implements ShopChannelDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<ShopChannel> getList(Long webId) {
		String hql = "from ShopChannel bean where bean.website.id=?";
		return find(hql, webId);
	}

	@Override
	@SuppressWarnings("unchecked")//查单页(wang ze wu)
	public List<ShopChannel> getList(Long webId, Integer type) {
		String hql = "from ShopChannel bean where bean.website.id=? and bean.type=? order by bean.priority";
		return find(hql, webId, type);
	}
	
	@Override
	@SuppressWarnings("unchecked")//查询页脚栏目（wang ze wu）
	public List<ShopChannel> getList(Long webId,Integer type,Long idBegin,Long idEnd){
		List<ShopChannel> channList=this.getSession().createQuery("from ShopChannel bean where bean.website.id=:webId and bean.type" +
				"=:type and bean.id >=:idBegin and bean.id <=:idEnd")
				.setParameter("webId", webId).setParameter("type", type)
				.setParameter("idBegin", idBegin).setParameter("idEnd", idEnd).list();
		return channList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ShopChannel> getListForParent(Long webId, Long currId) {
		Finder f = Finder
				.create("select node from ShopChannel node,ShopChannel exclude");
		f.append(" where node.lft<exclude.lft and node.rgt>exclude.rgt");
		f.append(" and exclude.id=:currId and node.website.id=:webId");
		f.setParam("webId", webId);
		f.setParam("currId", currId);
		return find(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ShopChannel> getListForChild(Long webId, Long parentId) {
		Finder f = Finder
				.create("select node from ShopChannel node, ShopChannel parent");
		f.append(" where node.lft>=parent.lft and node.lft<=parent.rgt");
		f.append(" and parent.id=:parentId and node.website.id=:webId");
		f.setParam("webId", webId);
		f.setParam("parentId", parentId);
		return find(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ShopChannel> getTopList(Long webId, boolean cacheable,Integer count) {//count 是新加的
		String hql = "from ShopChannel bean where bean.website.id=?"
				+ " and bean.parent.id is null order by bean.priority";
		if(count!=null){
			return getSession().createQuery(hql).setParameter(0, webId).setCacheable(cacheable)
			.setFirstResult(0).setMaxResults(count).list();
		}
		return getSession().createQuery(hql).setParameter(0, webId)
				.setCacheable(cacheable).list();
	}
	
	/**
	 * 获得商品类别列表
	 * 
	 * @param webId
	 *            站点ID
	 * @param parentId
	 *            类别ID
	 * @param cacheable
	 *            是否开启查询缓存
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ShopChannel> getChildList(Long webId, Long parentId){
		Finder f = Finder.create("from ShopChannel bean");
		f.append(" where bean.parent.id=:parentId");
		f.setParam("parentId", parentId);
		f.append(" order by bean.priority asc,bean.id asc");
		return find(f);
	}

	@Override
	public ShopChannel getByPath(Long webId, String path) {
		String hql = "from ShopChannel bean where bean.website.id=? and bean.path=?";
		return (ShopChannel) findUnique(hql, webId, path);
	}

	@Override
	public ShopChannel findById(Long id) {
		ShopChannel entity = get(id);
		return entity;
	}

	@Override
	public ShopChannel save(ShopChannel bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public ShopChannel deleteById(Long id) {
		ShopChannel entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ShopChannel> getEntityClass() {
		return ShopChannel.class;
	}
}