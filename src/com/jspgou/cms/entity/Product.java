package com.jspgou.cms.entity;

import static com.jspgou.common.web.Constants.SPT;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jspgou.core.entity.Website;
import com.jspgou.cms.entity.base.BaseProduct;
import com.jspgou.cms.web.threadvariable.GroupThread;
import com.jspgou.core.entity.Website;

/**
 * 商品实体类
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
 */
public class Product extends BaseProduct implements ProductInfo {
	private static final long serialVersionUID = 1L;
	/**
	 * 详细图后缀
	 */
	public static String DETAIL_SUFFIX = "_d";
	/**
	 * 列表图后缀
	 */
	public static String LIST_SUFFIX = "_l";
	/**
	 * 缩略图后缀
	 */
	public static String MIN_SUFFIX = "_m";

	/**
	 * 获得会员价
	 * 
	 * @return
	 */
	public Double getMemberPrice() {
		ShopMemberGroup group = GroupThread.get();
		if (group == null) {
			return getSalePrice();
		}
		return getMemberPrice(group);
	}

	/**
	 * 获得会员价
	 * 
	 * @param group
	 *            会员组
	 * @return
	 */
	public Double getMemberPrice(ShopMemberGroup group) {
//		return getSalePrice().multiply(new BigDecimal(group.getDiscount()))
//				.divide(new BigDecimal(100));
		return getSalePrice()*group.getDiscount()/100;
	}

	/**
	 * 获得URL访问地址
	 * 
	 * @return
	 */
	@Override
	public String getUrl() {
		return getWebsite().getUrlBuff(false).
				append(SPT).
				append(getCategory().getPath()).
				append(SPT).
				append(getId()).
				append(getWebsite().getSuffix()).toString();
	}
	
	

	/**
	 * 获得详细图的url地址
	 * 
	 * @return
	 */
	@Override
	public String getDetailImgUrl() {
		return getImageUrl(getDetailImg());
	}

	/**
	 * 获得列表图的url地址
	 * 
	 * @return
	 */
	@Override
	public String getListImgUrl() {
		return getImageUrl(getListImg());
	}

	public String getCoverImgUrl() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getCoverImg();
		}
		return null;
	}

	public Double getPrice() {
		ProductFashion fashion = getProductFashion();
		if(fashion!=null){
			return fashion.getSalePrice();
		}
		return getSalePrice();
	}

	/**
	 * 获得缩略图的url地址
	 * 
	 * @return
	 */
	@Override
	public String getMinImgUrl() {
		return getImageUrl(getMinImg());
	}
	
	public  ProductFashion getProductFashion(){
		Set<ProductFashion> set =getFashions();
		for(ProductFashion p:set){
			if(p.getIsDefault()){
				return p;	
			}
		}
		return null;
		
	}	
	
	private String getImageUrl(String img) {
		if (StringUtils.isBlank(img)) {
			return null;
		}
		return getWebsite().getUploadUrl(img);
	}

	public List<Category> getCategoryList() {
		return getCategory().getCategoryList();
	}

	/**
	 * 添加至tags
	 * 
	 * @param tag
	 */
	public void addToTags(ProductTag tag) {
		Set<ProductTag> set = getTags();
		if (set == null) {
			set = new HashSet<ProductTag>();
			setTags(set);
		}
		tag.increaseCount();
		set.add(tag);
	}

	/**
	 * 从tags中删除
	 * 
	 * @param tag
	 */
	public void removeFromTags(ProductTag tag) {
		Set<ProductTag> set = getTags();
		if (set != null) {
			tag.reduceCount();
			set.remove(tag);
		}
	}

	/**
	 * 删除所有标签
	 */
	public void removeAllTags() {
		Set<ProductTag> set = getTags();
		for (ProductTag tag : set) {
			tag.reduceCount();
		}
		set.clear();
	}

	/**
	 * 获得tags的ID数组
	 * 
	 * @return
	 */
	public Long[] getTagIds() {
		Set<ProductTag> set = getTags();
		if (set == null) {
			return null;
		}
		Long[] tagIds = new Long[set.size()];
		int i = 0;
		for (ProductTag tag : set) {
			tagIds[i++] = tag.getId();
		}
		return tagIds;
	}
	
	
	public void addToExendeds(String name, String value) {
		List<ProductExended> list = getExendeds();
		if (list == null) {
			list = new ArrayList<ProductExended>();
			setExendeds(list);
		}
		ProductExended cp = new ProductExended();
		cp.setName(name);
		cp.setValue(value);
		list.add(cp);
	}
	
	public void addToPictures(String fashionSwitchPic,String fashionBigPic,String fashionAmpPic) {
		List<ProductPicture> list = getPictures();
		if (list == null) {
			list = new ArrayList<ProductPicture>();
			setPictures(list);
		}
		ProductPicture pp = new ProductPicture();
		pp.setPicturePath(fashionSwitchPic);
		pp.setBigPicturePath(fashionBigPic);
		pp.setAppPicturePath(fashionAmpPic);
		list.add(pp);
	}

	/**
	 * 获得商品详细信息
	 * 
	 * @return
	 */
	public String getText() {
		ProductText productText = getProductText();
		if (productText != null) {
			return productText.getText();
		} else {
			return null;
		}
	}

	/**
	 * 设置商品详细信息
	 */
	public void setText(String s) {
		ProductText productText = getProductText();
		if (productText != null) {
			productText.setText(s);
		} else {
			productText = new ProductText();
			productText.setText(s);
			setProductText(productText);
		}
	}
	
	/**
	 * 获得商品规格(wang ze wu)
	 * 
	 * @return
	 */
	public String getText1() {
		ProductText productText = getProductText();
		if (productText != null) {
			return productText.getText1();
		} else {
			return null;
		}
	}

	/**
	 * 设置商品规格(wang ze wu)
	 */
	public void setText1(String s) {
		ProductText productText = getProductText();
		if (productText != null) {
			productText.setText1(s);
		} else {
			productText = new ProductText();
			productText.setText1(s);
			setProductText(productText);
		}
	}
	
	/**
	 * 获得商品注意事项(wang ze wu)
	 * 
	 * @return
	 */
	public String getText2() {
		ProductText productText = getProductText();
		if (productText != null) {
			return productText.getText2();
		} else {
			return null;
		}
	}

	/**
	 * 设置商品注意事项(wang ze wu)
	 */
	public void setText2(String s) {
		ProductText productText = getProductText();
		if (productText != null) {
			productText.setText2(s);
		} else {
			productText = new ProductText();
			productText.setText2(s);
			setProductText(productText);
		}
	}
     
	
	
	public String getWeixinProductUrl(){
		String sourceUrl=getUrl();
		StringBuilder url = new StringBuilder();
		if(!sourceUrl.startsWith(getWebsite().getContextPath())){
			Website site=getWebsite();
			url.append(site.getContextPath()).append(site.getDomain());
			if (site.getPort() != null) {
				url.append(":").append(site.getPort());
			}
			url.append(sourceUrl);
			sourceUrl=url.toString();
		}
		return sourceUrl;
	}
	
	
	
	
	@Override
	public Collection<String> getKeywordArray() {
		return getKeywords();
	}

	@Override
	public Collection<String> getTagArray() {
		Set<ProductTag> tags = getTags();
		if(tags!=null){
			  List<String> list = new ArrayList<String>(tags.size());
			  for (ProductTag tag : tags) {
				 list.add(tag.getName());
			  }
			  return list;
			}else{
				return null;
			}
	}

	@Override
	public String getBrandName() {
		Brand brand = getBrand();
		if (brand != null) {
			return brand.getName();
		} else {
			return null;
		}
	}
	
	public String getBrandId() {
		Brand brand = getBrand();
		if (brand != null) {
			return brand.getId().toString();
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getCategoryNameArray() {
		List<Category> list = getCategoryList();
		List<String> nameList = new ArrayList<String>(list.size());
		for (Category c : list) {
			nameList.add(c.getName());
		}
		return nameList;
	}

	@Override
	public Collection<Long> getCategoryIdArray() {
		List<Category> list = getCategoryList();
		List<Long> nameList = new ArrayList<Long>(list.size());
		for (Category c : list) {
			nameList.add(c.getId());
		}
		return nameList;
	}

	/**
	 * 初始化非空字段
	 */
	public void init() {
		if(getLackRemind() == null){
			setLackRemind(true);
		}
		if (getOnSale() == null) {
			setOnSale(true);
		}
		if (getViewCount() == null) {
			setViewCount(0L);
		}
		if (getSaleCount() == null) {
			setSaleCount(0);
		}
		if (getStockCount() == null) {
			setStockCount(10);
		}
		if (getMarketPrice() == null) {
			setMarketPrice(0.0);
		}
		if (getSalePrice() == null) {
			setSalePrice(0.0);
		}
		if (getCostPrice() == null) {
			setCostPrice(0.0);
		}
		if (getRecommend() == null) {
			setRecommend(false);
		}
		if (getSpecial() == null) {
			setSpecial(false);
		}
		if(getScore() == null){
			setScore(0);
		}
		if (getLiRun() == null) {
			setLiRun(0.0);
		}
		setCreateTime(new Timestamp(System.currentTimeMillis()));
	}

	public String getMtitle() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getMtitle();
		} else {
			return null;
		}
	}

	public String getMkeywords() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getMkeywords();
		} else {
			return null;
		}
	}

	public String getMdescription() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getMdescription();
		} else {
			return null;
		}
	}

	public String getDetailImg() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getDetailImg();
		} else {
			return null;
		}
	}

	public String getListImg() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getListImg();
		} else {
			return null;
		}
	}
	
	public String getCoverImg() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getCoverImg();
		} else {
			return null;
		}
	}

	public String getMinImg() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getMinImg();
		} else {
			return null;
		}
	}

	public java.lang.Integer getWeight() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getWeight();
		} else {
			return null;
		}
	}

	public java.lang.String getUnit() {
		ProductExt ext = getProductExt();
		if (ext != null) {
			return ext.getUnit();
		} else {
			return null;
		}
	}
	
	
	public Integer getProductTotalStockCount(){//商品库存（加了商品款式wangzewu）
		Set<ProductFashion> set=this.getFashions();
		int store=0;
		if(set!=null){
			for(ProductFashion s : set){
				store=store+s.getStockCount();
			}
		}
//		set.clear();
		return store;
	}
	
	public Integer getProductTotalSaleCount(){
		Set<ProductFashion> set=this.getFashions();
		int sale=0;
		if(set!=null){
			for(ProductFashion s : set){
				sale=sale+s.getSaleCount();
			}
		}
		return sale;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Product () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Product (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Product (
		java.lang.Long id,
		com.jspgou.cms.entity.ShopConfig config,
		com.jspgou.cms.entity.Category category,
		com.jspgou.cms.entity.ProductType type,
		com.jspgou.core.entity.Website website,
		java.lang.String name,
		java.lang.Double marketPrice,
		java.lang.Double salePrice,
		java.lang.Double costPrice,
		java.lang.Long viewCount,
		java.lang.Integer saleCount,
		java.lang.Integer stockCount,
		java.util.Date createTime,
		java.lang.Boolean special,
		java.lang.Boolean recommend,
		java.lang.Boolean hotsale,
		java.lang.Boolean newProduct,
        java.lang.Boolean relatedGoods,
		java.lang.Boolean onSale) {

		super (
			id,
			config,
			category,
			type,
			website,
			name,
			marketPrice,
			salePrice,
			costPrice,
			viewCount,
			saleCount,
			stockCount,
			createTime,
			special,
			recommend,
			hotsale,
			newProduct,
            relatedGoods,
			onSale);
	}

	/* [CONSTRUCTOR MARKER END] */

}