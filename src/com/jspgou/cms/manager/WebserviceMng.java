package com.jspgou.cms.manager;

import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.Webservice;
import java.util.List;
import java.util.Map;

import com.jspgou.common.page.Pagination;

public interface WebserviceMng {
	
	public Pagination getPage(int pageNo,int pageSize);
	
	public List<Webservice> getList(String type);
	
	public boolean hashWebservice(String type);
	
	public String callWebService(Webservice service,Map<String,String>params);
	
	public void callWebService(String operate,Map<String,String>params);
	
	public void callWebService(String admin,String username,String password,String email,ShopMember shopmember,String operate);
	
	public Webservice findById(Integer id);
	
	public Webservice save(Webservice bean,String[] paramName, String[] defaultValue);
	
	public Webservice update(Webservice bean,String[] paramName,String[] defaultValue);
	
	public Webservice deleteById(Integer id);
	
	public Webservice[] deleteByIds(Integer[] ids);
}
