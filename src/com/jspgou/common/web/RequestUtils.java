package com.jspgou.common.web;

import static com.jspgou.common.web.Constants.POST;
import static com.jspgou.common.web.Constants.UTF8;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

/**
* This class should preserve.
* @preserve
*/
public class RequestUtils{
	private static final Logger log = LoggerFactory
	               .getLogger(RequestUtils.class);
	
	
	
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
    public static String getQueryParam(HttpServletRequest request, String name){
        String s = request.getQueryString();
        if(StringUtils.isBlank(s)){
            return null;
        }
        try{
            s = URLDecoder.decode(s, "UTF-8");
        }catch(UnsupportedEncodingException e){
			log.error("encoding " + UTF8 + " not support?", e);
        }
        if(StringUtils.isBlank(s)){
            return null;
        }
        String[] as = StringUtils.split(s, "&");
        String[] as1 = as;
        int i = as1.length;
        for(int j = 0; j < i; j++){
            String s2 = as1[j];
            String[] as2= StringUtils.split(s2, "=");
            int k = as2.length;
            if(k >= 1 && as2[0].equals(name)){
                if(k == 2){
                    return as2[1];
                }else{
                    return "";
                }
            }
        }

        return null;
    }
    
    
    
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQueryParams(HttpServletRequest request) {
		Map<String, String[]> map;
		if (request.getMethod().equalsIgnoreCase(POST)) {
			map = request.getParameterMap();
		} else {
			String s = request.getQueryString();
			if (StringUtils.isBlank(s)) {
				return new HashMap<String, Object>();
			}
			try {
				s = URLDecoder.decode(s, UTF8);
			} catch (UnsupportedEncodingException e) {
				log.error("encoding " + UTF8 + " not support?", e);
			}
			map = parseQueryString(s);
		}

		Map<String, Object> params = new HashMap<String, Object>(map.size());
		int len;
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			len = entry.getValue().length;
			if (len == 1) {
				params.put(entry.getKey(), entry.getValue()[0]);
			} else if (len > 1) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		return params;
	}
	
	/**
	 * 
	 * Parses a query string passed from the client to the server and builds a
	 * <code>HashTable</code> object with key-value pairs. The query string
	 * should be in the form of a string packaged by the GET or POST method,
	 * that is, it should have key-value pairs in the form <i>key=value</i>,
	 * with each pair separated from the next by a &amp; character.
	 * 
	 * <p>
	 * A key can appear more than once in the query string with different
	 * values. However, the key appears only once in the hashtable, with its
	 * value being an array of strings containing the multiple values sent by
	 * the query string.
	 * 
	 * <p>
	 * The keys and values in the hashtable are stored in their decoded form, so
	 * any + characters are converted to spaces, and characters sent in
	 * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
	 * 
	 * @param s
	 *            a string containing the query to be parsed
	 * 
	 * @return a <code>HashTable</code> object built from the parsed key-value
	 *         pairs
	 * 
	 * @exception IllegalArgumentException
	 *                if the query string is invalid
	 * 
	 */
	public static Map<String, String[]> parseQueryString(String s) {
		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (ht.containsKey(key)) {
				String oldVals[] = ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++) {
					valArray[i] = oldVals[i];
				}
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}
		return ht;
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}
	
	public static Map<String, String> getRequestMap(HttpServletRequest request,
			String prefix) {
		return getRequestMap(request, prefix, false);
	}
	
	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	
	@SuppressWarnings("unchecked")
	private static Map<String, String> getRequestMap(
			HttpServletRequest request, String prefix, boolean nameWithPrefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name, key, value;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				key = nameWithPrefix ? name : name.substring(prefix.length());
				value = StringUtils.join(request.getParameterValues(name), ',');
				map.put(key, value);
			}
		}
		return map;
	}

    public static void main(String args[]){
        System.out.println(StringUtils.split("", "=").length);
    }

}
