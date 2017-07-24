package com.jspgou.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测是否为移动端设备访问
 * @author Administrator
 *
 */
public class CheckMobile {
	
	// \b是单词边界（连着的两个（字母字符与非字母字符）之间的逻辑上的间隔），
	// 字符串在编译时会被转码以一次，所以是“\\b”
	// \b是单词内部逻辑间隔（连着的两个字母字符之间的逻辑上的间隔）
	
	//手机
	static String phoneReg="\\b(ip(hone|od)|android|opera m(ob|in)i"  
	        +"|windows (phone|ce)|blackberry"  
	        +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"  
	        +"|laystation portable)|nokia|fennec|htc[-_]"  
	        +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";  
	//平板
	static String tableReg="\\b(ipad|tablet|(Nexus 7)|up.browser"  
	        +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	
	//移动设备正则表达式匹配：手机端、平板
	static Pattern phonePat =Pattern.compile(phoneReg,Pattern.CASE_INSENSITIVE);
	static Pattern tablePat =Pattern.compile(tableReg,Pattern.CASE_INSENSITIVE);
	
	public static boolean check(String userAgent){
		if(null==userAgent){
			userAgent="";
		}
		//开始匹配
		Matcher matcherPhone=phonePat.matcher(userAgent);
		Matcher matcherTable=tablePat.matcher(userAgent);
		if(matcherPhone.find()||matcherTable.find()){
			//移动设备入口
			return true;
		}else{
			//pc端入口
			return false;
		}
	}
}
