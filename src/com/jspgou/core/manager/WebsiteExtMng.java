package com.jspgou.core.manager;

import java.util.Map;

import com.jspgou.core.entity.WebsiteExt.ConfigLogin;

public interface WebsiteExtMng {
	public Map<String,String> getMap();
	
	public ConfigLogin getConfigLogin();
}
