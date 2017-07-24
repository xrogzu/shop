package com.jspgou.core.manager;

import javax.servlet.http.HttpServletRequest;


import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Log;
import com.jspgou.core.entity.User;
/**
* This class should preserve.
* @preserve
*/
public interface LogMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public Log operating(HttpServletRequest request, String title,
			String content);

	public Log findById(Long id);

	public Log save(Log bean);
	
	public void save(String versions,String updatelog);

	public Log update(Log bean);

	public Log deleteById(Long id);
	
	public Log[] deleteByIds(Long[] ids);
	
	public Log loginFailure(HttpServletRequest request,String content);
	
	public Log loginSuccess(HttpServletRequest request,User user);
}