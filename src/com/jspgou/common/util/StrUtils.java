package com.jspgou.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspgou.common.web.springmvc.RealPathResolver;

/**
* This class should preserve.
* @preserve
*/
public class StrUtils{
	
	private static final String API_PLACEHOLDER_VIDEO_BEGIN = "_video_begin";
	private static final String API_PLACEHOLDER_VIDEO_END = "_video_end";
	private static final String API_PLACEHOLDER_IMAGE_BEGIN = "_image_begin";
	private static final String API_PLACEHOLDER_IMAGE_END = "_image_end";
	
	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if (url.equals("") || url.startsWith("http://")
				|| url.startsWith("https://")) {
			return url;
		} else {
			return "http://" + url.trim();
		}
	}
	
	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}
	
	
	
	
	
	
	/**
	 * 把html内容转为文本
	 * 
	 * @param html
	 *            需要处理的html文本
	 * @return 
	 */
	public static String trimHtml2Txt(String html) {
		html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");// 去掉head
		html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");// 去掉注释
		html = html.replaceAll("\\<![\\s\\S]*?>", "");
		html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");// 去掉样式
		html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");// 去掉js
		html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");// 去掉word标签
		html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");
		html = html.replaceAll("\\<table>[\\s\\S]*?</table>(?i)", "");
		html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)",
				"");
		html = html.replaceAll("\\\r\n|\n|\r", "");// 去掉换行
		html = html.replaceAll("\\<br[^>]*>(?i)", "\r\n");
		html = html.replaceAll("\\</p>(?i)", "\r\n");
		html = html.replaceAll("\\<p>(?i)", "\r\n");
		// 图片替换特殊标记，客户端自己下载图片并替换标记
		// <img[^>]*/>
		String regular = "<img(.*?)src=\"(.*?)/>";
		String img_pre = "<img(.*?)src=\"";
		String img_sub = "\"(.*?)/>";
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		String src = null;
		String newSrc = null;
		while (m.find()) {
			src = m.group();
			newSrc = src.replaceAll(img_pre, API_PLACEHOLDER_IMAGE_BEGIN)
					.replaceAll(img_sub, API_PLACEHOLDER_IMAGE_END)
					.trim();
			html = html.replace(src, newSrc);
		}

		String videoregular = "<video(.*?)src=\"(.*?)\"(.*?)</video>";
		String video_pre = "<video(.*?)src=\"";
		String video_sub = "\"(.*?)</video>";
		Pattern videop = Pattern
				.compile(videoregular, Pattern.CASE_INSENSITIVE);
		Matcher videom = videop.matcher(html);
		String videosrc = null;
		String videonewSrc = null;
		while (videom.find()) {
			videosrc = videom.group();
			videonewSrc = videosrc
					.replaceAll(video_pre,API_PLACEHOLDER_VIDEO_BEGIN)
					.replaceAll(video_sub, API_PLACEHOLDER_VIDEO_END)
					.trim();
			html = html.replace(videosrc, videonewSrc);
		}
		// 去除分页
		html = html.replace("[NextPage][/NextPage]", "");
		html = html.replaceAll("\\<[^>]+>", "");
		// html = html.replaceAll("\\ ", " ");
		return html.trim();
	}
}
