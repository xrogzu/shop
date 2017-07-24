package com.jspgou.cms.manager;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ApiUserLogin;

public interface ApiUserLoginMng {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiUserLogin findById(Long id);

	public ApiUserLogin save(ApiUserLogin bean);

	public ApiUserLogin update(ApiUserLogin bean);

	public ApiUserLogin deleteById(Long id);
	
	public ApiUserLogin[] deleteByIds(Long[] ids);
	
	public ApiUserLogin findBySessionKey(String sessionKey);
	
	public ApiUserLogin findByUsername(String username);
	
	public void updateLoginSuccess(String username,String sessionKey);
	
	public void saveLoginSuccess(String username,String sessionKey);
}