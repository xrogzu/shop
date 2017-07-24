package com.jspgou.cms.manager;

import java.util.List;

import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.common.page.Pagination;

public interface ShopPlugMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<ShopPlug> getList(String author,Boolean used);

	public ShopPlug findById(Long id);
	
	public ShopPlug findByPath(String plugPath);

	public ShopPlug save(ShopPlug bean);

	public ShopPlug update(ShopPlug bean);

	public ShopPlug deleteById(Long id);
	
	public ShopPlug[] deleteByIds(Long[] ids);
}
