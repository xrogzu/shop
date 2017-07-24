package com.jspgou.cms.manager.impl;

import static com.jspgou.common.web.Constants.SPT;
import static com.jspgou.common.web.Constants.UTF8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.cms.manager.ResourceMng;
import com.jspgou.cms.manager.ShopPlugMng;
import com.jspgou.common.file.FileWrap;
import com.jspgou.common.file.FileWrap.FileComparator;
import com.jspgou.common.util.Zipper.FileEntry;
import com.jspgou.common.web.springmvc.RealPathResolver;
import com.jspgou.core.entity.Website;

/**
* This class should preserve.
* @preserve
*/
@Service
public class ResourceMngImpl implements ResourceMng {
	
	//插件权限配置key
	private static final String PLUG_PERMS="plug.perms";
	private static final Logger log = LoggerFactory
			.getLogger(ResourceMngImpl.class);

	@Override
	public List<FileWrap> listFile(String path, boolean dirAndEditable) {
		File parent = new File(realPathResolver.get(path));
		if (parent.exists()) {
			File[] files;
			if (dirAndEditable) {
				files = parent.listFiles(filter);
			} else {
				files = parent.listFiles();
			}
			Arrays.sort(files, new FileComparator());
			List<FileWrap> list = new ArrayList<FileWrap>(files.length);
			for (File f : files) {
				list.add(new FileWrap(f, realPathResolver.get("")));
			}
			return list;
		} else {
			return new ArrayList<FileWrap>(0);
		}
	}

	@Override
	public boolean createDir(String path, String dirName) {
		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File dir = new File(parent, dirName);
		return dir.mkdir();
	}

	@Override
	public void saveFile(String path, MultipartFile file)
			throws IllegalStateException, IOException {
		File dest = new File(realPathResolver.get(path), file
				.getOriginalFilename());
		file.transferTo(dest);
	}

	@Override
	public void createFile(String path, String filename, String data)
			throws IOException {
		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File file = new File(parent, filename);
		FileUtils.writeStringToFile(file, data, UTF8);
	}

	@Override
	public String readFile(String name) throws IOException {
		File file = new File(realPathResolver.get(name));
		return FileUtils.readFileToString(file, UTF8);
	}

	@Override
	public void updateFile(String name, String data) throws IOException {
		File file = new File(realPathResolver.get(name));
		FileUtils.writeStringToFile(file, data, UTF8);
	}

	@Override
	public int delete(String[] names) {
		int count = 0;
		File f;
		for (String name : names) {
			f = new File(realPathResolver.get(name));
			if (FileUtils.deleteQuietly(f)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public void rename(String origName, String destName) {
		File orig = new File(realPathResolver.get(origName));
		File dest = new File(realPathResolver.get(destName));
		orig.renameTo(dest);
	}

	/**
	* This class should preserve.
	* @preserve
	*/
	@Override
	public String[] getSolutions(String path) {
		File tpl = new File(realPathResolver.get(path));
		return tpl.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory();
			}
		});
	}

	@Override
	public List<FileEntry> export(Website site, String solution) {
		List<FileEntry> fileEntrys = new ArrayList<FileEntry>();
		File tpl = new File(realPathResolver.get(site.getTplPath()), solution);
		fileEntrys.add(new FileEntry("", "", tpl));
		File res = new File(realPathResolver.get("/r/gou/www"));
		if (res.exists()) {
			for (File r : res.listFiles()) {
				fileEntrys.add(new FileEntry("${res}", r));
			}
		}
		return fileEntrys;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void imoport(File file, Website site) throws IOException {
		String resRoot = "/r/gou/www";
		String tplRoot="/WEB-INF/t/gou";
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File outFile;
		File pfile;
		byte[] buf = new byte[1024];
		int len;
		InputStream is = null;
		OutputStream os = null;
		String solution = null;

		Enumeration<ZipEntry> en = zip.getEntries();
		while (en.hasMoreElements()) {
			name = en.nextElement().getName();
			if (!name.startsWith("${res}")) {
				solution = name.substring(0, name.indexOf("/"));
				break;
			}
		}
		if (solution == null) {
			return;
		}
		en = zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			if (!entry.isDirectory()) {
				name = entry.getName();
				log.debug("unzip file：{}", name);
				// 模板还是资源
				if (name.startsWith("${res}")) {
					filename = resRoot + name.substring("${res}".length());
				} else {
					filename = tplRoot + SPT + name;
				}
				log.debug("解压地址：{}", filename);
				outFile = new File(realPathResolver.get(filename));
				pfile = outFile.getParentFile();
				if (!pfile.exists()) {
					pfile.mkdirs();
				}
				try {
					is = zip.getInputStream(entry);
					os = new FileOutputStream(outFile);
					while ((len = is.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					if (is != null) {
						is.close();
						is = null;
					}
					if (os != null) {
						os.close();
						os = null;
					}
				}
			}
		}
	}

	/**
	* This class should preserve.
	* @preserve
	*/
	private FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory()|| FileWrap.editableExt(FilenameUtils.getExtension(file.getName()));
		}
	};


	private RealPathResolver realPathResolver;

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}
	
	private  void createFolder(File f){
		File parent=f.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
			createFolder(parent);
		}
	}
	
	@Override
	public void unZipFile(File file) throws IOException {
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
		//解压之前要坚持是否冲突
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File outFile;
		File pfile;
		byte[] buf = new byte[1024];
		int len;
		InputStream is = null;
		OutputStream os = null;
		Enumeration<ZipEntry> en = zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			name = entry.getName();
			if (!entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				outFile = new File(realPathResolver.get(filename));
				if(outFile.exists()){
					break;
				}
				pfile = outFile.getParentFile();
				if (!pfile.exists()) {
					//	pfile.mkdirs();
					createFolder(outFile);
				}
				try {
					is = zip.getInputStream(entry);
					os = new FileOutputStream(outFile);
					while ((len = is.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					if (is != null) {
						is.close();
						is = null;
					}
					if (os != null) {
						os.close();
						os = null;
					}
				}
			}
		}
		zip.close();
	}
	
	//文件夹判断是否有文件
		private  boolean directoryHasFile(File directory){
			File[] files = directory.listFiles();
			if(files!=null&&files.length>0){
				for(File f:files){
					if(directoryHasFile(f)){
						return true;
					}else{
						continue;
					}
				}
				return false;
			}else{
				return false;
			}
		}
	
	@Override
	public void deleteZipFile(File file) throws IOException {
		//根据压缩包删除解压后的文件
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File directory;
		//删除文件
		Enumeration<ZipEntry> en = zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			if (!entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				directory = new File(realPathResolver.get(filename));
				if(directory.exists()){
					directory.delete();
				}
			}
		}
		//删除空文件夹
		en= zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			if (entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				directory = new File(realPathResolver.get(filename));
				if(!directoryHasFile(directory)){
					directory.delete();
				}
			}
		}
		zip.close();
	}

	@Override
	public String readFileFromZip(File file, String readFileName)
			throws IOException {
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
		//解压之前要坚持是否冲突
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		InputStream is = null;
		InputStreamReader reader=null;
		BufferedReader in=null;
		Enumeration<ZipEntry> en = zip.getEntries();
		StringBuffer buff=new StringBuffer();
		String line;
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			name = entry.getName();
			if (!entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				if(filename.endsWith(readFileName)){
					try {
						is = zip.getInputStream(entry);
						reader=new InputStreamReader(is);
						in=new BufferedReader(reader);
						line=in.readLine();
						while(StringUtils.isNotBlank(line)) {
							buff.append(line);
							line=in.readLine();
						}
					} finally {
						if (is != null) {
							is.close();
							is = null;
						}
						if(reader!=null){
							reader.close();
							reader=null;
						}
						if(in!=null){
							in.close();
							in=null;
						}
					}
					break;
				}
			}
		}
		zip.close();
		return buff.toString();
	}

	@Override
	public void installPlug(File zipFile, ShopPlug plug) throws IOException {
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
		//解压之前要坚持是否冲突
		ZipFile zip = new ZipFile(zipFile, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File outFile;
		File pfile;
		byte[] buf = new byte[1024];
		int len;
		InputStream is = null;
		OutputStream os = null;
		Enumeration<ZipEntry> en = zip.getEntries();
		StringBuffer buff=new StringBuffer();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			name = entry.getName();
			if (!entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				outFile = new File(realPathResolver.get(filename));
				if(outFile.exists()){
					break;
				}
				pfile = outFile.getParentFile();
				if (!pfile.exists()) {
					//	pfile.mkdirs();
					createFolder(outFile);
				}
				try {
					is = zip.getInputStream(entry);
					os = new FileOutputStream(outFile);
					while ((len = is.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					if (is != null) {
						is.close();
						is = null;
					}
					if (os != null) {
						os.close();
						os = null;
					}
				}
				//读取配置文件
				if(filename.toLowerCase().endsWith(".properties")&&!filename.toLowerCase().contains("messages")){
					Properties p=new Properties();
					p.load(new FileInputStream(outFile));
					Set<Object>pKeys=p.keySet();
					for(Object pk:pKeys){
						if(pk.toString().startsWith(PLUG_PERMS)){
							buff.append(p.getProperty((String) pk)+";");
						}
					}
				}
			}
		}
		zip.close();
		plug.setPlugPerms(buff.toString());
		plugMng.update(plug);
	}
	
	@Autowired
	private ShopPlugMng plugMng;

}