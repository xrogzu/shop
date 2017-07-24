package com.jspgou.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

 
public class Apputil {
   
	public static Boolean getRequestBoolean(String requeststring){
		Boolean requestBoolean = null;
		if (StringUtils.isNotBlank(requeststring)){
			requestBoolean = Boolean.parseBoolean(requeststring);
		}
		return requestBoolean;
	}
	
	public static Long getRequestLong(String requeststring){
		Long requestLong = null;
		if (StringUtils.isNotBlank(requeststring)){
			requestLong = Long.parseLong(requeststring);
		}
		return requestLong;
	}
	
	public static Double getRequestDouble(String requeststring){
		Double requestInteger = null;
		if (StringUtils.isNotBlank(requeststring)){
			requestInteger = Double.parseDouble(requeststring);
		}
		return requestInteger;
	}
	
	public static Integer getRequestInteger(String requeststring){
		Integer requestInteger = null;
		if (StringUtils.isNotBlank(requeststring)){
			requestInteger = Integer.parseInt(requeststring);
		}
		return requestInteger;
	}
	
	 public static Date getRequestDate(String requeststring) {  
		 Date date = null;  
		 if (StringUtils.isNotBlank(requeststring)){
	        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	        try {  
	            date = format.parse(requeststring);   
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        }  
	        date = java.sql.Date.valueOf(requeststring);   
		 }
	     return date;  
	  }  
	 
	 public static Integer getfirstResult(String requeststring){
			Integer requestInteger = 0;
			if (StringUtils.isNotBlank(requeststring)){
				requestInteger = Integer.parseInt(requeststring);
			}
			return requestInteger;
	}

	 public static Integer getmaxResults(String requeststring){
			Integer requestInteger = 10;
			if (StringUtils.isNotBlank(requeststring)){
				requestInteger = Integer.parseInt(requeststring);
			}
			return requestInteger;
	}

}

