package com.jspgou.cms.app;

import static com.jspgou.common.web.Constants.SPT;
import static com.jspgou.common.web.Constants.UPLOAD_PATH;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.ApiUtilMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.file.FileNameUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;

@Controller
public class ImageUploadAct implements ServletContextAware {

	/**
	 * 文件上传接口
	 * 
	 * 相关参数协议： 00 上传失败 01 上传成功
	 */
	@RequestMapping("/api/upload/o_upload.jspx")
	public void execute(
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		ShopMember user = apiUtilMng.findbysessionKey(request);
		JSONObject o = new JSONObject();
		try {
			if (user != null) {
				Website web = SiteUtils.getWeb(request);
				String real = servletContext.getRealPath(web.getUploadRel());
				String dateDir = FileNameUtils.genPathName();
				// 创建目录
				File root = new File(real, dateDir);
				if (!root.exists()) {
					root.mkdirs();
				}
				// 取文件名
				String ext = FilenameUtils.getExtension(file
						.getOriginalFilename());
				String fileName = FileNameUtils.genFileName(ext);
				// 保存为临时文件
				File tempFile = new File(root, fileName);
				// 相对路径
				String relPath = SPT + UPLOAD_PATH + SPT + dateDir + SPT
						+ fileName;
				try {
					file.transferTo(tempFile);
					o.put("relPath", relPath);
					o.put("message", "01");
				} catch (IllegalStateException e) {
					o.put("message", "00");
				} catch (IOException e) {
					o.put("message", "00");
				}
			}
		} catch (JSONException e) {

		}
		ResponseUtils.renderJson(response,
				apiUtilMng.getJsonStrng(o.toString(), "", request));
	}

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Autowired
	private ApiUtilMng apiUtilMng;

}