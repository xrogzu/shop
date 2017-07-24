package com.jspgou.cms.manager;

import com.jspgou.cms.entity.WebserviceAuth;
import com.jspgou.common.page.Pagination;

public interface WebserviceAuthMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public boolean isPasswordValid(String username, String password);
	
	public WebserviceAuth findByUsername(String username);

	public WebserviceAuth findById(Integer id);

	public WebserviceAuth save(WebserviceAuth bean);

	public WebserviceAuth update(WebserviceAuth bean);
	
	public WebserviceAuth update(Integer id,String username,String password,String system,Boolean enable);

	public WebserviceAuth deleteById(Integer id);
	
	public WebserviceAuth[] deleteByIds(Integer[] ids);

}