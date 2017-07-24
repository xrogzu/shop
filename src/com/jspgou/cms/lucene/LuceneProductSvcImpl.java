package com.jspgou.cms.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.store.LockObtainFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.Constants;
import com.jspgou.cms.dao.ProductDao;
import com.jspgou.cms.entity.Product;

/**
* This class should preserve.
* @preserve
*/
@Service
public class LuceneProductSvcImpl implements LuceneProductSvc {
	@Override
	public int index(String path, Long webId, Date start, Date end)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {
		Directory dir = new SimpleFSDirectory(new File(path));
		IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30),
				true, IndexWriter.MaxFieldLength.LIMITED);
		try {
			int count = productDao.luceneWriteIndex(writer, webId, start, end);
			writer.optimize();
			return count;
		} finally {
			writer.close();
		}
	}

	public void createIndex(Product product) throws IOException{
		String path=servletContext.getRealPath(Constants.LUCENE_JSPGOU);
		Directory dir =new SimpleFSDirectory(new File(path));
		createIndex(product,dir);
	}
	
	public void createIndex(Product product, Directory dir) throws IOException {
		boolean exist = IndexReader.indexExists(dir);
		IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(
				Version.LUCENE_30), !exist, IndexWriter.MaxFieldLength.LIMITED);
		try {
			writer.addDocument(LuceneProduct.createDocument(product));
		} finally {
			writer.close();
		}
	}
	
	public void updateIndex(Product product) throws IOException, ParseException{
		String path = servletContext.getRealPath(Constants.LUCENE_JSPGOU);
		Directory dir = new SimpleFSDirectory(new File(path));
		updateIndex(product,dir);
	}
	
	public void updateIndex(Product product, Directory dir) throws IOException,ParseException {
        boolean exist = IndexReader.indexExists(dir);
        IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(
		       Version.LUCENE_30), !exist, IndexWriter.MaxFieldLength.LIMITED);
        try {
	        if (exist) {
	        	LuceneProduct.delete(product.getId(), writer);
	        }
	        //当商品修改的时候Lucence会自动多加一条，暂时去掉。
	        writer.addDocument(LuceneProduct.createDocument(product));
        } finally {
	        writer.close();
        }
      }
	
	public void deleteIndex(Product product) throws IOException, ParseException{
		String path = servletContext.getRealPath(Constants.LUCENE_JSPGOU);
		Directory dir = new SimpleFSDirectory(new File(path));
		deleteIndex(product,dir);
	}
	
	public void deleteIndex(Product product,Directory dir) throws IOException, ParseException{
		boolean exist = IndexReader.indexExists(dir);
		if(exist){
			IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(
					Version.LUCENE_30), false,
					IndexWriter.MaxFieldLength.LIMITED);
			try {
				LuceneProduct.delete(product.getId(), writer);
			} finally {
				writer.close();
			}
		}
	}
	
	
	
	
	
	
	
	@Override
	public Pagination search(String path, String queryString, Long webId,
			Long ctgId,Long brandId, Date start, Date end,int orderBy, int pageNo, int pageSize)
			throws CorruptIndexException, IOException, ParseException {
		Directory dir = new SimpleFSDirectory(new File(path));
		Searcher searcher = new IndexSearcher(dir);
		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			Query query =LuceneProduct.createQuery(queryString, webId,ctgId,brandId, start, end, analyzer);
			Sort sort = getSort(orderBy);
			TopDocs docs;
		    if(sort!=null){
				 docs = searcher.search(query, null, pageNo * pageSize,sort);
			}else{
				 docs = searcher.search(query, pageNo * pageSize);
			}
			Pagination p = LuceneProduct.getResult(searcher, docs, pageNo,pageSize);
			return p;
		} finally {
			searcher.close();
		}
	}
	
	
	private Sort getSort(int orderBy){
		Sort sort = null;
		switch (orderBy) {
			case 1:
				sort= new Sort(new SortField("saleCount", SortField.INT,true));
				return sort;
			case 2:
				sort= new Sort(new SortField("createTime", SortField.STRING,false));
				return sort;
			case 3:
				sort= new Sort(new SortField("salePrice", SortField.DOUBLE,true));
				return sort;
			case 4:
				sort= new Sort(new SortField("salePrice", SortField.DOUBLE,false));
				return sort;
			case 5:
				sort= new Sort(new SortField("viewCount", SortField.LONG,true));
				return sort;
			default:
				sort = null;
			}
		return sort;	
	}
	
	@Override
	public List<LuceneProduct> getlist(String path, String queryString,
			Long webId, Long ctgId, Long brandId, Date start, Date end,
			int orderBy, int first, int max) throws IOException, ParseException {
		Directory dir = new SimpleFSDirectory(new File(path));
		Searcher searcher = new IndexSearcher(dir);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		Query query =LuceneProduct.createQuery(queryString, webId,ctgId,brandId, start, end, analyzer);
		Sort sort = getSort(orderBy);
		if (first < 0) {
			first = 0;
		}
		if (max < 0) {
			max = 0;
		}
		TopDocs docs;
		if(sort!=null){
			 docs = searcher.search(query, null, first + max,sort);
		}else{
			docs=searcher.search(query,first + max);
		}
     List<LuceneProduct> list=LuceneProduct.getResultList(searcher, docs, first, max);
		return list;
	
	}
	


	private ProductDao productDao;
	@Autowired
	private ServletContext servletContext;

	@Autowired
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
