package com.jspgou.cms.dao;

import java.util.List;

import com.jspgou.cms.entity.Webservice;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

public interface WebserviceDao {
	public Pagination getPage(int pageNo,int pageSize);
	
	public List<Webservice> getList(String type);
	
	public Webservice findById(Integer id);
	
	public Webservice save(Webservice bean);
	
	public Webservice updateByUpdater(Updater<Webservice> update);
	
	public Webservice deleteById(Integer id);
}
