package com.jspgou.core.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import com.jspgou.core.entity.base.BaseWebsiteExt;

public class WebsiteExt extends BaseWebsiteExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WebsiteExt(){
		super();
	}
	
	public WebsiteExt(java.lang.String id){
		super(id);
	}
	
	public static class ConfigLogin {
		public static String LOGIN_ERROR_INTERVAL = "login_error_interval";
		public static String LOGIN_ERROR_TIMES = "login_error_times";

		private Map<String, String> attr;

		public static ConfigLogin create(Map<String, String> map) {
			ConfigLogin configLogin = new ConfigLogin();
			configLogin.setAttr(map);
			return configLogin;
		}

		public Map<String, String> getAttr() {
			if (attr == null) {
				attr = new HashMap<String, String>();
			}
			return attr;
		}

		public void setAttr(Map<String, String> attr) {
			this.attr = attr;
		}

		public Integer getErrorInterval() {
			String interval = getAttr().get(LOGIN_ERROR_INTERVAL);
			if (NumberUtils.isDigits(interval)) {
				return Integer.parseInt(interval);
			} else {
				// 默认间隔30分钟
				return 30;
			}
		}

		public void setErrorInterval(Integer errorInterval) {
			if (errorInterval != null) {
				getAttr().put(LOGIN_ERROR_INTERVAL, errorInterval.toString());
			} else {
				getAttr().put(LOGIN_ERROR_INTERVAL, null);
			}
		}

		public Integer getErrorTimes() {
			String times = getAttr().get(LOGIN_ERROR_TIMES);
			if (NumberUtils.isDigits(times)) {
				return Integer.parseInt(times);
			} else {
				// 默认3次
				return 3;
			}
		}

		public void setErrorTimes(Integer errorTimes) {
			if (errorTimes != null) {
				getAttr().put(LOGIN_ERROR_TIMES, errorTimes.toString());
			} else {
				getAttr().put(LOGIN_ERROR_TIMES, null);
			}
		}
	}
	
}
