package com.jspgou.cms.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.jspgou.cms.entity.ShopPlug;
import com.jspgou.common.file.FileWrap;
import com.jspgou.common.util.Zipper.FileEntry;
import com.jspgou.core.entity.Website;

/**
* This class should preserve.
* @preserve
*/
public interface ResourceMng {
	/**
	 * 获得子文件列表
	 * 
	 * @param path
	 *            父文件夹路径
	 * @param dirAndEditable
	 *            是否只获取文件夹和可编辑文件
	 * @return
	 */
	public List<FileWrap> listFile(String path, boolean dirAndEditable);

	/**
	 * 保存文件
	 * 
	 * @param path
	 *            保存路径
	 * @param file
	 *            保存文件
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void saveFile(String path, MultipartFile file)
			throws IllegalStateException, IOException;

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 *            当前目录
	 * @param dirName
	 *            文件夹名
	 * @return
	 */
	public boolean createDir(String path, String dirName);

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            当前目录
	 * @param filename
	 *            文件名
	 * @param data
	 *            文件内容
	 * @throws IOException
	 */
	public void createFile(String path, String filename, String data)
			throws IOException;

	/**
	 * 读取文件
	 * 
	 * @param name
	 *            文件名称
	 * @return 文件内容
	 * @throws IOException
	 */
	public String readFile(String name) throws IOException;

	/**
	 * 更新文件
	 * 
	 * @param name
	 *            文件名称
	 * @param data
	 *            文件内容
	 * @throws IOException
	 */
	public void updateFile(String name, String data) throws IOException;

	/**
	 * 文件重命名
	 * 
	 * @param origName
	 *            原文件名
	 * @param destName
	 *            目标文件名
	 */
	public void rename(String origName, String destName);

	/**
	 * 删除文件
	 * 
	 * @param names
	 * @return
	 */
	public int delete(String[] names);
	
	/**
	 * 解压文件（插件）
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public void unZipFile(File file) throws IOException;
	/**
	 * 删除解压文件以及zip包本身
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public void deleteZipFile(File file) throws IOException;
	
	/**
	 * 从zip文件中读取文件内容
	 * @param file
	 * @param readFileName
	 * @return
	 * @throws IOException
	 */
	public String readFileFromZip(File file,String readFileName) throws IOException;
	
	/**
	 * 插件安装
	 * @param zipFile
	 * @param plug
	 * @throws IOException
	 */
	public void installPlug(File zipFile,ShopPlug plug) throws IOException;

	/**
	 * 列出所有模板方案
	 * 
	 * @param path
	 *            模板方案路径
	 * @return
	 */
	public String[] getSolutions(String path);

	/**
	 * 导出模板
	 * 
	 * @param site
	 * @param solution
	 * @return
	 */
	public List<FileEntry> export(Website site, String solution);

	/**
	 * 导入模板
	 * 
	 * @param file
	 * @param site
	 * @throws IOException
	 */
	public void imoport(File file, Website site) throws IOException;
}
