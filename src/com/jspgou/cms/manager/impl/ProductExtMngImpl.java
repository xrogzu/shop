package com.jspgou.cms.manager.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.ProductExtDao;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductExt;
import com.jspgou.cms.manager.ProductExtMng;
import com.jspgou.common.hibernate3.Updater;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductExtMngImpl implements ProductExtMng {
	@Override
	public ProductExt save(ProductExt ext, Product product) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		long code=Long.parseLong(sdf.format(new Date()))+product.getId();
		ext.setCode(code);
		ext.setProduct(product);
		ext.init();
		dao.save(ext);
		return ext;
	}

	@Override
	public ProductExt saveOrUpdate(ProductExt ext, Product product) {
		ProductExt entity = dao.findById(ext.getId());
		if (entity != null) {
			Updater<ProductExt> updater = new Updater<ProductExt>(ext);
			dao.updateByUpdater(updater);
			return entity;
		} else {
			return save(ext, product);
		}
	}

	private ProductExtDao dao;

	@Autowired
	public void setDao(ProductExtDao dao) {
		this.dao = dao;
	}
}