package com.jspgou.common.util;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

public class ConvertMapToJson {

	private static final String QUOTE = "\"";
	
	public static String buildJsonBody(Map<String, Object> body, int tabCount,boolean addComma) {
		StringBuilder sbJsonBody = new StringBuilder();
		sbJsonBody.append("{");
		Set<String> keySet = body.keySet();
		int count = 0;
		int size = keySet.size();
		for (String key : keySet) {
			count++;
			sbJsonBody.append(buildJsonField(key, body.get(key), tabCount + 1,count != size));
		}
		sbJsonBody.append("}");
		return sbJsonBody.toString();
	}

	private static String buildJsonField(String key, Object value,int tabCount, boolean addComma) {
		StringBuilder sbJsonField = new StringBuilder();
		sbJsonField.append(QUOTE).append(key).append(QUOTE).append(": ");
		sbJsonField.append(buildJsonValue(value, tabCount, addComma));
		return sbJsonField.toString();
	}

	/**
	 * 生成json值对象 e.g. "string" 或 { "key": "value" }
	 * 
	 * @param value
	 * @param tabCount
	 * @param addComma
	 * @return
	 */
	private static String buildJsonValue(Object value, int tabCount,boolean addComma) {
		StringBuilder sbJsonValue = new StringBuilder();
		if (value instanceof String) {
			sbJsonValue.append(QUOTE).append(value).append(QUOTE);
		} else if (value instanceof Integer || value instanceof Long || value instanceof Double) {
			sbJsonValue.append(value);
		} else if (value instanceof java.util.Date) {
			sbJsonValue.append(QUOTE).append(formatDate((java.util.Date) value)).append(QUOTE);
		} else if (value.getClass().isArray()|| value instanceof java.util.Collection) {
			sbJsonValue.append(buildJsonArray(value, tabCount, addComma));
		} else if (value instanceof java.util.Map) {
			sbJsonValue.append(buildJsonBody((java.util.Map) value, tabCount,addComma));
		}
		sbJsonValue.append(buildJsonTail(addComma));
		return sbJsonValue.toString();
	}

	/**
	 * 生成json数组对象 [ "value" ] 或 [ { "key": "value" } ]
	 * 
	 * @param value
	 * @param tabCount
	 * @param addComma
	 * @return
	 */
	private static String buildJsonArray(Object value, int tabCount,
			boolean addComma) {
		StringBuilder sbJsonArray = new StringBuilder();
		sbJsonArray.append("[\n");
		Object[] objArray = null;
		if (value.getClass().isArray()) {
			objArray = (Object[]) value;
		} else if (value instanceof java.util.Collection)// 将集合类改为对象数组
		{
			objArray = ((java.util.Collection) value).toArray();
		}
		int size = objArray.length;
		int count = 0;
		// 循环数组里的每一个对象
		for (Object obj : objArray) {
			// 加上缩进，比上一行要多一个缩进
			// 加上对象值，比如一个字符串"String"，或者一个对象
			sbJsonArray.append(buildJsonValue(obj, tabCount + 1,++count != size));
		}
		sbJsonArray.append("]");
		return sbJsonArray.toString();
	}

	/**
	 * 加上逗号 TODO.
	 * 
	 * @param addComma
	 * @return
	 */
	private static String buildJsonTail(boolean addComma) {
		return addComma ? "," : "";
	}

	/**
	 * 格式化日期 TODO.
	 * 
	 * @param date
	 * @return
	 */
	private static String formatDate(java.util.Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
