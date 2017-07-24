package com.jspgou.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.core.dao.WebsiteExtDao;
import com.jspgou.core.entity.WebsiteExt;
import com.jspgou.core.entity.WebsiteExt.ConfigLogin;
import com.jspgou.core.manager.WebsiteExtMng;


@Service
@Transactional
public class WebsiteExtMngImpl implements WebsiteExtMng{

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getMap() {
		List<WebsiteExt> list=dao.getList();
		Map<String ,String> map=new HashMap<String ,String>(list.size());
		for(WebsiteExt websiteExt:list){
			map.put(websiteExt.getId(), websiteExt.getValue());
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public ConfigLogin getConfigLogin() {
		return ConfigLogin.create(getMap());
	}
	
	private WebsiteExtDao dao;
	
	@Autowired
	public void setDao(WebsiteExtDao dao){
		this.dao=dao;
	}
}
