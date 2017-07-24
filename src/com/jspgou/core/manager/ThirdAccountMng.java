package com.jspgou.core.manager;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.ThirdAccount;



public interface ThirdAccountMng {
	public Pagination getPage(String username,String source,int pageNo, int pageSize);

	public ThirdAccount findById(Long id);
	
	public ThirdAccount findByKey(String key);

	public ThirdAccount save(ThirdAccount bean);

	public ThirdAccount update(ThirdAccount bean);

	public ThirdAccount deleteById(Long id);
	
	public ThirdAccount[] deleteByIds(Long[] ids);
}