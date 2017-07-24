package com.jspgou.cms.dao;

import java.util.List;

import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

/**
* This class should preserve.
* @preserve
*/
public interface RelatedgoodsDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<Relatedgoods> findByIdProductId(Long productId);

	public Relatedgoods findById(Long id);

	public Relatedgoods save(Relatedgoods bean);

	public Relatedgoods updateByUpdater(Updater<Relatedgoods> updater);

	public Relatedgoods deleteById(Long id);
}