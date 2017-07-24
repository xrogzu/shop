package com.jspgou.cms.manager.impl;

import static com.jspgou.common.web.Constants.POINT;
import static com.jspgou.common.web.Constants.SPT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jspgou.cms.dao.ProductDao;
import com.jspgou.cms.dao.ProductFashionDao;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.Collect;
import com.jspgou.cms.entity.Consult;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductExt;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.entity.ProductStandard;
import com.jspgou.cms.entity.ProductTag;
import com.jspgou.cms.entity.ProductText;
import com.jspgou.cms.entity.ProductType;
import com.jspgou.cms.entity.ShopDictionaryType;
import com.jspgou.cms.entity.base.BaseProduct;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.CartItemMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.CollectMng;
import com.jspgou.cms.manager.ConsultMng;
import com.jspgou.cms.manager.ProductExtMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ProductStandardMng;
import com.jspgou.cms.manager.ProductTagMng;
import com.jspgou.cms.manager.ProductTextMng;
import com.jspgou.cms.manager.RelatedgoodsMng;
import com.jspgou.cms.manager.ShopConfigMng;
import com.jspgou.common.file.FileNameUtils;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.image.ImageScale;
import com.jspgou.common.image.ImageUtils;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.springmvc.RealPathResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.WebsiteMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ProductMngImpl implements ProductMng {
	
	@Override
	public List<Product> getList(Long typeId,Long brandId,String productName){
		return dao.getList(typeId, brandId, productName, true);
	}
	
	@Override
	public void resetSaleTop(){
		List<Product> list = getList(null,null,null);
		for(Product product:list){
			product.setSaleCount(0);
			update(product);
		}
	}
	
	@Override
	public void resetProfitTop(){
		List<Product> list = getList(null,null,null);
		for(Product product:list){
			product.setLiRun(0.0);
			update(product);
		}
	}
	
	@Override
	public void updateViewCount(Product product){
		product.setViewCount(product.getViewCount()+1);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long webId, Long ctgId, String productName,String brandName,
			Boolean isOnSale,Boolean isRecommend,Boolean isSpecial,Boolean isHotsale,
			Boolean isNewProduct,Long typeId,Double startSalePrice,Double endSalePrice,
			Integer startStock,Integer endStock,int pageNo, int pageSize) {
		Pagination page;
		if (ctgId != null) {
			page = dao.getPageByCategory(ctgId, productName,brandName,isOnSale,
					isRecommend, isSpecial, isHotsale,isNewProduct,typeId,
					startSalePrice,endSalePrice,startStock,endStock,pageNo,pageSize, false);
		} else {
			page = dao.getPageByWebsite(webId,productName,brandName,isOnSale,
					isRecommend, isSpecial, isHotsale,isNewProduct,typeId,
					startSalePrice,endSalePrice,startStock,endStock, pageNo,
					pageSize, false);
		}
		return page;
	}
	
	
	@Override
	public Pagination getPage(int orderBy,int pageNo, int pageSize){
		return dao.getPage(orderBy, pageNo, pageSize, true);
	}
	
	@Override
	public Pagination getPage1(Long typeId,int orderBy,int pageNo, int pageSize){
		return dao.getPage1(typeId,orderBy, pageNo, pageSize, true);
	}
	
	@Override
	public List<Product> findAll(){
		return dao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPageForTag(Long webId, Long ctgId, Long tagId,
			Boolean isRecommend, Boolean isSpecial, int pageNo, int pageSize) {
		Pagination page;
		if (tagId != null) {
			page = dao.getPageByTag(tagId, ctgId, isRecommend, isSpecial,
					pageNo, pageSize, true);
		} else {
			if (ctgId != null) {
				page = dao.getPageByCategory(ctgId, null,null,true,isRecommend, isSpecial,
						null,null,null,null,null,null,null,pageNo, pageSize, true);
			} else {
				page = dao.getPageByWebsite(webId, null,null,true,isRecommend, isSpecial,
						null,null,null,null,null,null,null,pageNo, pageSize, true);
			}
		}
		return page;
	}
	
	@Override
	public Pagination getPageByStockWarning(Long webId,Integer count,Boolean status,int pageNo, int pageSize){
		return dao.getPageByStockWarning(webId,count, status,pageNo, pageSize);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPageForTagChannel(String brandId,Long webId, Long ctgId, Long tagId,//栏目页商品分页manager(wangzewu)
			Boolean isRecommend, String[] names,String[] values, Boolean isSpecial, int orderBy,Double startPrice,Double endPrice,int pageNo, int pageSize) {
		Pagination page;
		if (tagId != null) {
			page = dao.getPageByTag(tagId, ctgId, isRecommend, isSpecial,pageNo, pageSize, true);
		} else {
			if (ctgId != null) {
				page = dao.getPageByCategoryChannel(brandId, ctgId, isRecommend,names,values,isSpecial,orderBy, startPrice, endPrice,pageNo, pageSize, true);
			} else {
				page = dao.getPageByWebsite(webId, null,null,true,isRecommend, isSpecial,null,null,null,null,null,null,null,pageNo, pageSize, true);
			}
		}
		return page;
	}

	@Override
	public List<Product> getListForTag(Long webId, Long ctgId, Long tagId,Boolean isRecommend, Boolean isSpecial,Boolean isHostSale,Boolean isNewProduct, int firstResult,int maxResults) {
		List<Product> list;
		if (tagId != null) {
			list = dao.getListByTag(tagId, ctgId, isRecommend, isSpecial,firstResult, maxResults, true);
		} else {
			if (ctgId != null) {
				list = dao.getListByCategory(ctgId, isRecommend, isSpecial,firstResult, maxResults, true);
			} else {
				list = dao.getListByWebsite1(webId, isRecommend, isSpecial,isHostSale,isNewProduct,firstResult, maxResults, true);
			}
		}
		return list;
	}
	
	@Override
	public List<Product> getIsRecommend(Boolean isRecommend,int count){
		return dao.getIsRecommend(isRecommend, count);
	}
	
	@Override
	public Integer[] getProductByTag(Long webId){
		return dao.getProductByTag(webId);
	}

	@Override
	public int deleteTagAssociation(ProductTag[] tags) {
		if (ArrayUtils.isEmpty(tags)) {
			return 0;
		}
		return dao.deleteTagAssociation(tags);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findById(Long id) {
		Product entity = dao.findById(id);
		return entity;
	}

	@Override
	public Product save(Product bean, ProductExt ext, Long webId, Long categoryId,
			Long brandId, Long[] tagIds, String[] keywords,String[] names,
			String[] values,String fashionSwitchPic[],String fashionBigPic[],String fashionAmpPic[],MultipartFile file) {
		ProductText text = bean.getProductText();
		if (text != null) {
			text.setProduct(bean);
		}
		Category category = categoryMng.findById(categoryId);
		if (brandId != null) {
			bean.setBrand(brandMng.findById(brandId));
		}
		Website web = websiteMng.findById(webId);
		bean.setWebsite(web);
		bean.setConfig(shopConfigMng.findById(webId));
		bean.setCategory(category);
		bean.setType(category.getType());
		if (!ArrayUtils.isEmpty(tagIds)) {
			for (Long tagId : tagIds) {
				bean.addToTags(productTagMng.findById(tagId));
			}
		}
		if (!ArrayUtils.isEmpty(keywords)) {
			bean.setKeywords(Arrays.asList(keywords));
		}
		bean.init();
		dao.save(bean);
		// 保存商品规格储存
		if (names != null && names.length > 0) {
			for (int i = 0, len = names.length; i < len; i++) {
				if (!StringUtils.isBlank(names[i])) {
					bean.addToExendeds(names[i], values[i]);
				}
			}
		}
		// 保存图片/大图片/放大图片集
		if (fashionSwitchPic != null && fashionSwitchPic.length > 0) {
			for (int i = 0, len = fashionSwitchPic.length; i < len; i++) {
				if (!StringUtils.isBlank(fashionSwitchPic[i])) {
					bean.addToPictures(fashionSwitchPic[i],fashionBigPic[i],fashionAmpPic[i]);
				}
			}
		}
		String uploadPath = realPathResolver.get(web.getUploadRel());
		saveImage(bean, ext, category.getType(), file, uploadPath);
		productExtMng.save(ext, bean);
		return bean;
	}
	
	@Override
	public Product updateByUpdater(Product bean) {
		Updater<Product> updater = new Updater<Product>(bean);
		Product entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Product update(Product bean, ProductExt ext, Long ctgId,
			Long brandId, Long[] tagIds, String[] keywords,String[] names,
			String[] values,Map<String, String> attr,
			String fashionSwitchPic[],String fashionBigPic[],String fashionAmpPic[],
			MultipartFile file) {
		Product entity = findById(bean.getId());
		ProductText text = bean.getProductText();
		// 先更新ProductText
		if (text != null) {
			ProductText ptext = entity.getProductText();
			if (ptext != null) {
				ptext.setText(text.getText());
				ptext.setText1(text.getText1());
				ptext.setText2(text.getText2());
			} else {
				text.setId(bean.getId());
				text.setProduct(entity);
				entity.setProductText(text);
				productTextMng.save(text);
			}
		}
		// 删除原tag
		entity.removeAllTags();
		// 设置类别、类型，不能为空
		Category category = categoryMng.findById(ctgId);
		entity.setCategory(category);
		entity.setType(category.getType());
		if(entity.getOnSale()==null){
			entity.setOnSale(false);
		}
		// 设置品牌，可以为空
		if (brandId != null) {
			entity.setBrand(brandMng.findById(brandId));
		} else {
			entity.setBrand(null);
		}
		// 设置tag，可以为空
		if (!ArrayUtils.isEmpty(tagIds)) {
			for (Long tagId : tagIds) {
				entity.addToTags(productTagMng.findById(tagId));
			}
		}
		// 设置关键字，可以为空
		if (keywords != null) {
			List<String> kw = entity.getKeywords();
			kw.clear();
			kw.addAll(Arrays.asList(keywords));
		} else {
			entity.getKeywords().clear();
		}
		
		// 更新其他属性
		Updater<Product> updater = new Updater<Product>(bean);
		updater.exclude(BaseProduct.PROP_WEBSITE);
		updater.exclude(BaseProduct.PROP_PRODUCT_TEXT);
		entity = dao.updateByUpdater(updater);
		// 更新属性表
		if (attr != null) {
			Map<String, String> attrOrig = entity.getAttr();
			attrOrig.clear();
			attrOrig.putAll(attr);
		}
		// 更新规则值
		entity.getExendeds().clear();
		if (names != null && names.length > 0) {
			for (int i = 0, len = names.length; i < len; i++) {
				if (!StringUtils.isBlank(names[i])) {
					entity.addToExendeds(names[i], values[i]);
				}
			}
		}
		// 更新图片集
		entity.getPictures().clear();
		if (fashionSwitchPic != null && fashionSwitchPic.length > 0) {
			for (int i = 0, len = fashionSwitchPic.length; i < len; i++) {
				if (!StringUtils.isBlank(fashionSwitchPic[i])) {
					entity.addToPictures(fashionSwitchPic[i],fashionBigPic[i],fashionAmpPic[i]);
				}
			}
		}
		String uploadPath = realPathResolver.get(entity.getWebsite().getUploadRel());
		saveImage(entity, ext, category.getType(), file, uploadPath);
		productExtMng.saveOrUpdate(ext, entity);
		return entity;
	}
	
	
	@Override
	public Product update(Product bean) {
		Updater<Product> updater = new Updater<Product>(bean);
		Product entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Product[] deleteByIds(Long[] ids) {
		Product[] beans = new Product[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			cartItemMng.deleteByProductId(ids[i]);
			List<Collect> clist=collectMng.findByProductId(ids[i]);
			for(Collect collect:clist){
				collectMng.deleteById(collect.getId());
			}
			List<Consult> consultList= consultMng.findByProductId(ids[i]);
			for(Consult consult:consultList){
				consultMng.deleteById(consult.getId());
			}
			List<ProductStandard> psList = productStandardMng.findByProductId(ids[i]);
			for(ProductStandard ps: psList){
				productStandardMng.deleteById(ps.getId());
			}
            //删除相关商品
			if(relatedgoodsMng.findByIdProductId(ids[i])!=null){			
				relatedgoodsMng.deleteProduct(ids[i]);
			}
			Product product = findById(ids[i]);
			product.getTags().clear();
			product.getFashions().clear();
			product.getKeywords().clear();
			product.getPopularityGroups().clear();
			beans[i] = dao.deleteById(ids[i]);
		}
		for (Product p : beans) {
			p.removeAllTags();
		}
		return beans;
	}

	private boolean saveImage(Product product, ProductExt bean,
			ProductType type, MultipartFile file, String uploadPath) {
		// 如果没有上传文件，则不处理。
		if (file == null || file.isEmpty()) {
			return false;
		}
		// 先删除图片，如果有的话。
		deleteImage(product, uploadPath);
		// 获得后缀
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		// 检查后缀是否允许上传
		if (!ImageUtils.isImage(ext)) {
			return false;
		}
		// 日期目录
		String dateDir = FileNameUtils.genPathName();
		// 创建目录
		File root = new File(uploadPath, dateDir);
		// 相对路径
		String relPath = SPT + dateDir + SPT;
		if (!root.exists()) {
			root.mkdirs();
		}
		// 取文件名
		String name = FileNameUtils.genFileName();
		// 保存为临时文件
		File tempFile = new File(root, name);
		try {
			file.transferTo(tempFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			// 保存详细图
			String detailName = name + Product.DETAIL_SUFFIX + POINT + ext;
			File detailFile = new File(root, detailName);
			imageScale.resizeFix(tempFile, detailFile,
					type.getDetailImgWidth(), type.getDetailImgHeight());
			bean.setDetailImg(relPath + detailName);
			// 保存列表图
			String listName = name + Product.LIST_SUFFIX + POINT + ext;
			File listFile = new File(root, listName);
			imageScale.resizeFix(tempFile, listFile, type.getListImgWidth(),
					type.getListImgHeight());
			bean.setListImg(relPath + listName);
			// 保存缩略图
			String minName = name + Product.MIN_SUFFIX + POINT + ext;
			File minFile = new File(root, minName);
			imageScale.resizeFix(tempFile, minFile, type.getMinImgWidth(), type
					.getMinImgHeight());
			bean.setMinImg(relPath + minName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 删除临时文件
		tempFile.delete();
		return true;
	}

	public void deleteImage(Product entity, String uploadPath) {
		String detail = entity.getDetailImg();
		if (!StringUtils.isBlank(detail)) {
			new File(uploadPath + detail).delete();
		}
		String list = entity.getListImg();
		if (!StringUtils.isBlank(list)) {
			new File(uploadPath + list).delete();
		}
		String min = entity.getMinImg();
		if (!StringUtils.isBlank(min)) {
			new File(uploadPath + min).delete();
		}
	}
	
	@Override
	public Integer getStoreByProductPattern(Long id,Long fashId){//获得商品款式库存wangzewu
		ProductFashion bean=fashDao.getPfashion(id, fashId);
		return bean.getStockCount();
	}
	
	@Override
	public List<Product> getHistoryProduct(HashSet<Long> set,Integer count){
		return dao.getHistoryProduct(set,count);
	}
	
	@Override
	public Integer getTotalStore(Long productId){
		Product bean=dao.findById(productId);
		Set<ProductFashion> set=bean.getFashions();
		Integer store=0;
		if(set!=null){
			for(ProductFashion f : set){
				store=store+f.getStockCount();
			}
		}
		
		return store;
	}
	
	@Override
	public String getTipFile(String key){
		String TipFile = "/WEB-INF/languages/jspgou_admin/messages_zh_CN.properties";
		String TipFileText = null;
		InputStream in;
		try {
			in = new FileInputStream(realPathResolver.get(TipFile));
			Properties p = new Properties();
			p.load(in);
			TipFileText = p.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TipFileText;
	}
	


	@Autowired
	private ProductFashionDao fashDao;
	@Autowired
	private ProductFashionMng productFashionMng;
	@Autowired
	private CollectMng collectMng;
	@Autowired
	private ConsultMng consultMng;
	@Autowired
	private CartItemMng cartItemMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private WebsiteMng websiteMng;
	@Autowired
	private CategoryMng categoryMng;
	@Autowired
	private ProductTagMng productTagMng;
	@Autowired
	private ProductTextMng productTextMng;
	@Autowired
	private ShopConfigMng shopConfigMng;
	@Autowired
	private BrandMng brandMng;
	@Autowired
	private ImageScale imageScale;
	@Autowired
	private ProductExtMng productExtMng;
	@Autowired
	private ProductStandardMng productStandardMng;
	@Autowired
    private RelatedgoodsMng relatedgoodsMng;
	@Autowired
	private ProductDao dao;
}