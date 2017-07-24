package com.jspgou.common.upload;

import java.io.*;

import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
/**
* This class should preserve.
* @preserve
*/
public class FileRepository implements Repository, ServletContextAware{
	private Logger log = LoggerFactory.getLogger(FileRepository.class);
	private ServletContext ctx;

	public String storeByExt(String path, String ext, MultipartFile file)
	throws IOException {
		/*String filename = UploadUtils.generateFilename(path, ext);
		File dest = new File(ctx.getRealPath(filename));
		dest = UploadUtils.getUniqueFile(dest);
		stores(file, dest);
		return filename;*/
		String fileName=UploadUtils.generateRamdonFilename(ext);
		String fileUrl =path+fileName;
		File dest = new File(getRealPath(path),fileName);
		dest = UploadUtils.getUniqueFile(dest);
		stores(file, dest);
		return fileUrl;
		
	}
	
	private void stores(MultipartFile file, File dest) throws IOException {
		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			file.transferTo(dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}
	
	public String storeByFilePath(String path,String filename, MultipartFile file)
			throws IOException {
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			return "";
		}
		File dest = new File(getRealPath(path+filename));
		store1(file, dest);
		return path+filename;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.ctx = servletContext;
	}

    @Override
	public boolean store(String s, InputStream inputstream)
        throws FileNotFoundException, IOException{
        IOUtils.copy(inputstream, new FileOutputStream(ctx.getRealPath(s)));
        return true;
    }
    
    private void store1(MultipartFile file, File dest) throws IOException {
		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			file.transferTo(dest);
		} catch (IOException e) {
			log.error("Transfer file error when upload file", e);
			throw e;
		}
	}

    @Override
	public boolean retrieve(String s, OutputStream outputstream){
        return false;
    }
    
    private String getRealPath(String name){
		String realpath=ctx.getRealPath(name);
		if(realpath==null){
			realpath=ctx.getRealPath("/")+name;
		}
		return realpath;
	}
}
