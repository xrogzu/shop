package com.jspgou.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jspgou.cms.dao.ShopPlugDao;
import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.common.hibernate3.Finder;
import com.jspgou.common.hibernate3.HibernateBaseDao;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
@Repository
public class ShopPlugDaoImpl extends HibernateBaseDao<ShopPlug, Long> implements ShopPlugDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public List<ShopPlug> getList(String author,Boolean used){
		Finder f=Finder.create("from ShopPlug plug where 1=1 ");
		if(StringUtils.isNotBlank(author)){
			f.append("and plug.author=:author").setParam("author", author);
		}
		if(used!=null){
			if(used){
				f.append("and plug.used=true");
			}else{
				f.append("and plug.used=false");
			}
		}
		return find(f);
	}

	public ShopPlug findById(Long id) {
		ShopPlug entity = get(id);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public ShopPlug findByPath(String plugPath){
		Finder f=Finder.create("from ShopPlug plug where plug.path=:path").setParam("path", plugPath);
		List<ShopPlug>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public ShopPlug save(ShopPlug bean) {
		getSession().save(bean);
		return bean;
	}
	
	@Override
	public ShopPlug deleteById(Long id) {
		ShopPlug entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
		
		
	}
	
	@Override
	protected Class<ShopPlug> getEntityClass() {
		return ShopPlug.class;
	}
}
