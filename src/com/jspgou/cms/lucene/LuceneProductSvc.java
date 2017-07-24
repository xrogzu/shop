package com.jspgou.cms.lucene;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.jspgou.cms.entity.Product;
import com.jspgou.common.page.Pagination;

/**
 * This class should preserve.
 * 
 * @preserve
 */
public interface LuceneProductSvc {
	/**
	 * 索引产品
	 * 
	 * @param webId
	 *            站点ID
	 * @param path
	 *            索引绝对路径
	 * @param start
	 *            产品录入开始时间
	 * @param end
	 *            产品录入结束时间
	 */
	public int index(String path, Long webId, Date start, Date end)
			throws CorruptIndexException, LockObtainFailedException,
			IOException;

	// 商品添加时，生成一条索引文件
	public void createIndex(Product product) throws IOException;

	public void createIndex(Product product, Directory dir) throws IOException;

	// 商品修改时，修改对应的索引文件
	public void updateIndex(Product product) throws IOException, ParseException;

	public void updateIndex(Product product, Directory dir) throws IOException,
			ParseException;

	/**
	 * 删除商品时，删除商品索引
	 * 
	 * @param product
	 * @throws IOException
	 * @throws ParseException
	 */
	public void deleteIndex(Product product) throws IOException, ParseException;

	public void deleteIndex(Product product, Directory dir) throws IOException,
			ParseException;

	/**
	 * 
	 * 搜索产品
	 * 
	 * @param path
	 *            索引地址
	 * @param queryString
	 *            搜索关键字
	 * @param webId
	 *            站点ID。可以为null。
	 * @param ctgId
	 *            产品类别ID。可以为null。
	 * @param start
	 *            开始时间。可以为null。
	 * @param end
	 *            结束时间。可以为null。
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Pagination search(String path, String queryString, Long webId,
			Long ctgId, Long brandId, Date start, Date end, int orderBy,
			int pageNo, int pageSize) throws CorruptIndexException,
			IOException, ParseException;

	public List<LuceneProduct> getlist(String path, String queryString,
			Long webId, Long ctgId, Long brandId, Date start, Date end,
			int orderBy, int first, int max) throws IOException, ParseException;
}
