package com.jspgou.common.image;

import java.io.File;
import magick.Magick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

/**
* This class should preserve.
* @preserve
*/
public class ImageScaleImpl implements ImageScale{
	
	private static final Logger log = LoggerFactory
	         .getLogger(ImageScaleImpl.class);
	private boolean isMagick = false;

	@Override
	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws Exception {
		try{
			if (isMagick) {
				MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
			} else {
				AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
			}			
		}catch(Exception e) {
			log.error("裁剪图片出错，请重新裁剪", e);
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws Exception {
		try{			
			if (isMagick) {
				MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight,
						cutTop, cutLeft, cutWidth, catHeight);
			} else {			
				AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight,
						cutTop, cutLeft, cutWidth, catHeight);
			}
		}catch(Exception e) {
			log.error("裁剪图片出错，请重新裁剪", e);
			System.out.println(e.getMessage());
		}
	}

    public void init() {
        try{
            System.setProperty("jmagick.systemclassloader", "no");
            new Magick();
            log.info("use jmagick");
            isMagick = true;
        }catch(Throwable throwable) {
            log.warn("load magick fail, use java image scale. message:{}", throwable.getMessage());
            isMagick = false;
        }
    }
}
