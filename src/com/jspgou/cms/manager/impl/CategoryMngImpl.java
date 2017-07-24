package com.jspgou.cms.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.jspgou.cms.dao.BrandDao;
import com.jspgou.cms.dao.CategoryDao;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.StandardType;
import com.jspgou.cms.entity.base.BaseCategory;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ProductTypeMng;
import com.jspgou.cms.manager.StandardTypeMng;
import com.jspgou.common.hibernate3.Updater;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class CategoryMngImpl implements CategoryMng {
	public Category getByPath(Long webId, String path) {
		return dao.getByPath(webId, path, false);
	}

	@Override
	public Category getByPathForTag(Long webId, String path) {
		return dao.getByPath(webId, path, true);
	}

	/**
	 * @see CategoryMng#getListForParent(Long, Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> getListForParent(Long webId, Long ctgId) {
		List<Category> allList = getList(webId);
		if (ctgId != null) {
			List<Category> list = dao.getListForChild(webId, ctgId);
			allList.removeAll(list);
		}
		return allList;
	}

	/**
	 * @see CategoryMng#getListForProduct(Long, Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> getListForProduct(Long webId, Long ctgId) {
		List<Category> list = new ArrayList<Category>();
		Category category = findById(ctgId);
		addAllChildToList(list, Arrays.asList(category), category.getType()
				.getId());
		return list;
	}

	/**
	 * @see CategoryMng#getTopList(Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> getTopList(Long webId) {
		return dao.getTopList(webId, false);
	}
	
	@Override
	public List<Category> getChildList(Long wegId,Long parentId){
		return dao.getChildList(wegId, parentId);
	}

	/**
	 * @see CategoryMng#getTopListForTag(Long)
	 */
	@Override
	public List<Category> getTopListForTag(Long webId) {
		return dao.getTopList(webId, true);
	}

	/**
	 * @see CategoryMng#getList(Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> getList(Long webId) {
		List<Category> list = dao.getTopList(webId, false);
		List<Category> allList = new ArrayList<Category>();
		addAllChildToList(allList, list, null);
		return allList;
	}

	/**
	 * @see CategoryMng#checkPath(Long, String)
	 */
	@Override
	public boolean checkPath(Long webId, String path) {
		return dao.countPath(webId, path) <= 0;
	}

	/**
	 * @see CategoryMng#findById(Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Category findById(Long id) {
		Category entity = dao.findById(id);
		return entity;
	}

	/**
	 * @see CategoryMng#save(Category, Long, Long)
	 */
	@Override
	public Category save(Category bean, Long parentId, Long typeId, Long[] brandIds,Long[] standardTypeIds) {
		Category parent = null;
		if (parentId != null) {
			parent = findById(parentId);
			bean.setParent(parent);
		}
		if (typeId != null) {
			bean.setType(productTypeMng.findById(typeId));
		}
		Category entity=dao.save(bean);
		if(brandIds!=null && brandIds.length>0){//-------wang ze wu s
			for(Long brandId : brandIds){
				entity.addToBrands(brandMng.findById(brandId));
			}
		}else{
			entity.setBrands(new HashSet<Brand>());
		}
		if (parent != null) {
			parent.addToChild(bean);
		}
		if(standardTypeIds!=null&&standardTypeIds.length>0){
			for(Long sid:standardTypeIds){
				entity.addToStandardTypes(standardTypeMng.findById(sid));
			}
		}
		return bean;
	}
	
	@Override
	public List<Brand> getBrandByCate(Long categoryId){//获得某类型的品牌
		return brandDao.getListByCate(categoryId);
	}

	/**
	 * @see CategoryMng#update(Category, Long, Long)wang ze wu 改动
	 */
	@Override
	public Category update(Category bean, Long parentId, Long typeId, Long[] brandIds,Map<String, String> attr,Long[] standardTypeIds) {
		Assert.notNull(bean);
		Category entity = findById(bean.getId());
		Category origParent = entity.getParent();
		Category parent = null;
		if (parentId != null) {
			parent = findById(parentId);
			bean.setParent(parent);
		} else {
			bean.setParent(null);
		}
		Updater<Category> updater = new Updater<Category>(bean);
		updater.include(BaseCategory.PROP_PARENT);
		entity = dao.updateByUpdater(updater);
		if (origParent != null) {
			origParent.removeFromChild(entity);
		}
		if (parent != null) {
			parent.addToChild(entity);
		}
		//清空关联品牌
		Set<Brand> brands=entity.getBrands();
		for(Brand brand : brands){
			brand.removeFromCategorys(entity);
		}
		brands.clear();
		if(brandIds!=null && brandIds.length>0){
			for(Long brandId : brandIds){
				entity.addToBrands(brandMng.findById(brandId));
			}
		}else{
			entity.setBrands(new HashSet<Brand>());
		}
		Set<StandardType> standardTypes = entity.getStandardType();
		for(StandardType t: standardTypes){
			t.removeFromCategorys(entity);
		} 
		standardTypes.clear();
		if(standardTypeIds!=null&&standardTypeIds.length>0){
			for(Long sid:standardTypeIds){
				entity.addToStandardTypes(standardTypeMng.findById(sid));
			}
		}
		// 更新属性表
		if (attr != null) {
			Map<String, String> attrOrig = entity.getAttr();
			attrOrig.clear();
			attrOrig.putAll(attr);
		}
		
		return entity;
	}

	/**
	 * @see CategoryMng#deleteById(Long)
	 */
	@Override
	public Category deleteById(Long id) {
		Category parent = findById(id).getParent();
		Category bean = dao.deleteById(id);
		if (parent != null) {
			parent.removeFromChild(bean);
		}
		return bean;
	}

	/**
	 * @see CategoryMng#deleteByIds(Long[])
	 */
	@Override
	public Category[] deleteByIds(Long[] ids) {
		Category[] beans = new Category[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = dao.deleteById(ids[i]);
		}
		Category parent;
		for (Category bean : beans) {
			parent = bean.getParent();
			if (parent != null) {
				parent.removeFromChild(bean);
			}
		}
		return beans;
	}

	@Override
	public Category[] updatePriority(Long[] ids, Integer[] priority) {
		Category[] beans = new Category[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	/**
	 * 将coll及其子类别加入至allList
	 * 
	 * @param allList
	 * @param coll
	 * @param typeId
	 *            类型ID，如果不为null，则只添加同一type的child。
	 */
	private void addAllChildToList(List<Category> allList,
			Collection<Category> coll, Long typeId) {
		Collection<Category> child;
		for (Category ctg : coll) {
			// 如果类型ID不为null，则只添加同一type的child。
			if (typeId != null) {
				if (typeId.equals(ctg.getType().getId())) {
					allList.add(ctg);
				}
			} else {
				allList.add(ctg);
			}
			child = ctg.getChild();
			if (child != null && child.size() > 0) {
				addAllChildToList(allList, child, typeId);
			}
		}
	}
	
	@Override
	public List<Category> getListBypType(Long webId,Long typeId,Integer count){
		return dao.getListByptype(webId, typeId,count);
	}
	
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private BrandMng brandMng;
	@Autowired
	private StandardTypeMng standardTypeMng;
	
	private ProductTypeMng productTypeMng;
	private CategoryDao dao;

	@Autowired
	public void setProductTypeMng(ProductTypeMng productMng) {
		this.productTypeMng = productMng;
	}

	@Autowired
	public void setDao(CategoryDao dao) {
		this.dao = dao;
	}
}