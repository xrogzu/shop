package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.image.ImageScale;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.springmvc.RealPathResolver;
import com.jspgou.core.manager.WebsiteMng;
import com.jspgou.cms.dao.ProductFashionDao;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.StandardMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductFashionMngImpl implements ProductFashionMng{
	
	@Override
	public ProductFashion deleteById(Long id) {
		return dao.deleteById(id);
	}

	@Override
	public ProductFashion[] deleteByIds(Long[] ids) {
		ProductFashion[] beans = new ProductFashion[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public ProductFashion findById(Long id) {
		return dao.findById(id);
	}
	
	@Override
	public List<ProductFashion> findByProductId(Long productId){
		return dao.findByProductId(productId);
	}

	@Override
	public Pagination getPage(Long productId, int pageNo, int pageSize) {
		return dao.getPage(productId,pageNo, pageSize);
	}
	
	@Override
	public ProductFashion save(ProductFashion bean,String[] arr){
		String attitude = "";
		for(String a:arr){
			attitude =attitude +" "+standardMng.findById(Long.parseLong(a)).getName();
		}
		bean.setAttitude(attitude);
		bean.init();
		return dao.save(bean);
	}

	@Override
	public void addStandard(ProductFashion bean,String[] arr){
		for(String a:arr){
			bean.addTostandards(standardMng.findById(Long.parseLong(a)));
		}
	}
	
	
	@Override
	public void updateStandard(ProductFashion bean,String[] arr){
		bean.getStandards().clear();
		for(String a:arr){
			 bean.addTostandards(standardMng.findById(Long.parseLong(a)));
		}
	}
	
	public void deleteImage(ProductFashion entity, String uploadPath) {
		/*String detail = entity.getDetailImg();
		if (!StringUtils.isBlank(detail)) {
			new File(uploadPath + detail).delete();
		}
		String list = entity.getListImg();
		if (!StringUtils.isBlank(list)) {
			new File(uploadPath + list).delete();
		}
		String min = entity.getMinImg();
		if (!StringUtils.isBlank(min)) {
			new File(uploadPath + min).delete();
		}*/
	}
	
	@Override
	public ProductFashion update(ProductFashion bean,String[] arr) {
		String attitude = "";
		for(String a:arr){
			attitude =attitude +" "+standardMng.findById(Long.parseLong(a)).getName();
		}
		bean.setAttitude(attitude);
		Updater<ProductFashion> updater = new Updater<ProductFashion>(bean);
		ProductFashion entity = dao.updateByUpdater(updater);
		return entity;
	}
	
	@Override
	public ProductFashion update(ProductFashion bean) {
		Updater<ProductFashion> updater = new Updater<ProductFashion>(bean);
		ProductFashion entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Pagination productLack(Integer status,Integer count, int pageNo, int pageSize) {
		return dao.productLack(status,count, pageNo, pageSize);
	}

	@Override
	public Integer productLackCount(Integer status,Integer count) {
		return dao.productLackCount(status,count);
	}

	@Override
	public Pagination getSaleTopPage(int pageNo, int pageSize) {
		return dao.getSaleTopPage(pageNo, pageSize);
	}
	
	@Override
	public Boolean getOneFash(Long productId){
		return dao.getOneFashion(productId);
	}
	
	@Autowired
	private ProductFashionDao dao;
	
	@Autowired
	private WebsiteMng websiteMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CategoryMng categoryMng;
	@Autowired
	private ImageScale imageScale;
	@Autowired
	private StandardMng standardMng;

}

