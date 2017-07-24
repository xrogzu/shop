package com.jspgou.common.image;

import java.util.Locale;

/**
 * 图片辅助类
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
*/
public abstract class ImageUtils{
	/**
	 * 图片的后缀
	 */
	public static final String[] IMAGE_EXT = new String[] { "jpg", "jpeg",
			"gif", "png", "bmp" };
	
	/**
	 * 是否是图片
	 * 
	 * @param ext
	 * @return "jpg", "jpeg", "gif", "png", "bmp" 为文件后缀名者为图片
	 */
	public static boolean isValidImageExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : IMAGE_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the underlying input stream contains an image.
	 * 
	 * @param in
	 *            input stream of an image
	 * @return <code>true</code> if the underlying input stream contains an
	 *         image, else <code>false</code>
	 */
    public static boolean isImage(String s){
        s = s.toLowerCase();
        String as[] = IMAGE_EXT;
        int i = as.length;
        for(int j = 0; j < i; j++){
            String s1 = as[j];
            if(s1.equals(s))
                return true;
        }
        return false;
    }
}
