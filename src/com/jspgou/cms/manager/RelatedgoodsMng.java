package com.jspgou.cms.manager;
import java.util.List;

import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
public interface RelatedgoodsMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<Relatedgoods> findByIdProductId(Long productId);

	public Relatedgoods findById(Long id);

	public Relatedgoods save(Relatedgoods bean);

	public Relatedgoods update(Relatedgoods bean);

	public Relatedgoods deleteById(Long id);
	
	public Relatedgoods[] deleteByIds(Long[] ids);
	
	public void addProduct(Long id,Long productIds[]);
	
	public void updateProduct(Long id,Long productIds[]);
	
	public void deleteProduct(Long productid);
}