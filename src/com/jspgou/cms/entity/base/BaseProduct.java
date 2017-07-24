package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_shop_product table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_shop_product"
 * This class should preserve.
 * @preserve
 */

public abstract class BaseProduct  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String REF = "Product";
	public static String PROP_PRODUCT_EXT = "productExt";
	public static String PROP_BRAND = "brand";
	public static String PROP_CONFIG = "config";
	public static String PROP_SALE_COUNT = "saleCount";
	public static String PROP_SPECIAL = "special";
	public static String PROP_TYPE = "type";
	public static String PROP_SHARE_CONTENT = "shareContent";
	public static String PROP_RECOMMEND = "recommend";
	public static String PROP_NEW_PRODUCT = "newProduct";
	public static String PROP_VIEW_COUNT = "viewCount";
	public static String PROP_HOTSALE = "hotsale";
	public static String PROP_SCORE = "score";
	public static String PROP_MARKET_PRICE = "marketPrice";
	public static String PROP_WEBSITE = "website";
	public static String PROP_STOCK_COUNT = "stockCount";
	public static String PROP_PRODUCT_TEXT = "productText";
	public static String PROP_ON_SALE = "onSale";
	public static String PROP_NAME = "name";
	public static String PROP_CATEGORY = "category";
	public static String PROP_SALE_PRICE = "salePrice";
	public static String PROP_CREATE_TIME = "createTime";
	public static String PROP_ID = "id";
	public static String PROP_COST_PRICE = "costPrice";
    public static String PROP_RELATED_GOODS = "relatedGoods";


	// constructors
	public BaseProduct () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseProduct (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseProduct (
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

		this.setId(id);
		this.setConfig(config);
		this.setCategory(category);
		this.setType(type);
		this.setWebsite(website);
		this.setName(name);
		this.setMarketPrice(marketPrice);
		this.setSalePrice(salePrice);
		this.setCostPrice(costPrice);
		this.setViewCount(viewCount);
		this.setSaleCount(saleCount);
		this.setStockCount(stockCount);
		this.setCreateTime(createTime);
		this.setSpecial(special);
		this.setRecommend(recommend);
		this.setHotsale(hotsale);
		this.setNewProduct(newProduct);
        this.setRelatedGoods(relatedGoods);
		this.setOnSale(onSale);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.Double marketPrice;
	private java.lang.Double salePrice;
	private java.lang.Double costPrice;
	private java.lang.Long viewCount;
	private java.lang.Integer saleCount;
	private java.lang.Integer stockCount;
	private java.util.Date createTime;
	private java.util.Date expireTime;
	private java.lang.Boolean special;
	private java.lang.Boolean recommend;
	private java.lang.Boolean hotsale;
	private java.lang.Boolean newProduct;
	private java.lang.Boolean onSale;
	public java.lang.Boolean getRelatedGoods() {
		return relatedGoods;
	}

	public void setRelatedGoods(java.lang.Boolean relatedGoods) {
		this.relatedGoods = relatedGoods;
	}
	private java.lang.Boolean lackRemind;
	private java.lang.Integer score;
	private java.lang.String shareContent;
	private java.lang.Integer alertInventory;
    private java.lang.Boolean relatedGoods;
	private java.lang.Double liRun;

	// one to one
	private com.jspgou.cms.entity.ProductText productText;
	private com.jspgou.cms.entity.ProductExt productExt;

	// many to one
	private com.jspgou.cms.entity.Brand brand;
	private com.jspgou.cms.entity.ShopConfig config;
	private com.jspgou.cms.entity.Category category;
	private com.jspgou.cms.entity.ProductType type;
	private com.jspgou.core.entity.Website website;

	// collections
	private java.util.Set<com.jspgou.cms.entity.ProductTag> tags;
	private java.util.Set<com.jspgou.cms.entity.ProductFashion> fashions;
	
	private java.util.List<java.lang.String> keywords;
	private java.util.List<com.jspgou.cms.entity.ProductPicture> pictures;
	private java.util.Map<java.lang.String, java.lang.String> attr;
	private java.util.List<com.jspgou.cms.entity.ProductExended> exendeds;
	private java.util.Set<com.jspgou.cms.entity.PopularityGroup> popularityGroups;

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="product_id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}


	/**
	 * Return the value associated with the column: market_price
	 */
	public java.lang.Double getMarketPrice () {
		return marketPrice;
	}

	/**
	 * Set the value related to the column: market_price
	 * @param marketPrice the market_price value
	 */
	public void setMarketPrice (java.lang.Double marketPrice) {
		this.marketPrice = marketPrice;
	}


	/**
	 * Return the value associated with the column: sale_price
	 */
	public java.lang.Double getSalePrice () {
		return salePrice;
	}

	/**
	 * Set the value related to the column: sale_price
	 * @param salePrice the sale_price value
	 */
	public void setSalePrice (java.lang.Double salePrice) {
		this.salePrice = salePrice;
	}


	/**
	 * Return the value associated with the column: cost_price
	 */
	public java.lang.Double getCostPrice () {
		return costPrice;
	}

	/**
	 * Set the value related to the column: cost_price
	 * @param costPrice the cost_price value
	 */
	public void setCostPrice (java.lang.Double costPrice) {
		this.costPrice = costPrice;
	}


	/**
	 * Return the value associated with the column: view_count
	 */
	public java.lang.Long getViewCount () {
		return viewCount;
	}

	/**
	 * Set the value related to the column: view_count
	 * @param viewCount the view_count value
	 */
	public void setViewCount (java.lang.Long viewCount) {
		this.viewCount = viewCount;
	}


	/**
	 * Return the value associated with the column: sale_count
	 */
	public java.lang.Integer getSaleCount () {
		return saleCount;
	}

	/**
	 * Set the value related to the column: sale_count
	 * @param saleCount the sale_count value
	 */
	public void setSaleCount (java.lang.Integer saleCount) {
		this.saleCount = saleCount;
	}


	/**
	 * Return the value associated with the column: stock_count
	 */
	public java.lang.Integer getStockCount () {
		return stockCount;
	}

	/**
	 * Set the value related to the column: stock_count
	 * @param stockCount the stock_count value
	 */
	public void setStockCount (java.lang.Integer stockCount) {
		this.stockCount = stockCount;
	}


	/**
	 * Return the value associated with the column: create_time
	 */
	public java.util.Date getCreateTime () {
		return createTime;
	}

	/**
	 * Set the value related to the column: create_time
	 * @param createTime the create_time value
	 */
	public void setCreateTime (java.util.Date createTime) {
		this.createTime = createTime;
	}


	/**
	 * Return the value associated with the column: is_special
	 */
	public java.lang.Boolean getSpecial () {
		return special;
	}

	/**
	 * Set the value related to the column: is_special
	 * @param special the is_special value
	 */
	public void setSpecial (java.lang.Boolean special) {
		this.special = special;
	}


	/**
	 * Return the value associated with the column: is_recommend
	 */
	public java.lang.Boolean getRecommend () {
		return recommend;
	}

	/**
	 * Set the value related to the column: is_recommend
	 * @param recommend the is_recommend value
	 */
	public void setRecommend (java.lang.Boolean recommend) {
		this.recommend = recommend;
	}


	/**
	 * Return the value associated with the column: is_hotsale
	 */
	public java.lang.Boolean getHotsale () {
		return hotsale;
	}

	/**
	 * Set the value related to the column: is_hotsale
	 * @param hotsale the is_hotsale value
	 */
	public void setHotsale (java.lang.Boolean hotsale) {
		this.hotsale = hotsale;
	}


	/**
	 * Return the value associated with the column: is_newProduct
	 */
	public java.lang.Boolean getNewProduct () {
		return newProduct;
	}

	/**
	 * Set the value related to the column: is_newProduct
	 * @param newProduct the is_newProduct value
	 */
	public void setNewProduct (java.lang.Boolean newProduct) {
		this.newProduct = newProduct;
	}


	/**
	 * Return the value associated with the column: on_sale
	 */
	public java.lang.Boolean getOnSale () {
		return onSale;
	}

	/**
	 * Set the value related to the column: on_sale
	 * @param onSale the on_sale value
	 */
	public void setOnSale (java.lang.Boolean onSale) {
		this.onSale = onSale;
	}

	public java.lang.Boolean getLackRemind() {
		return lackRemind;
	}

	public void setLackRemind(java.lang.Boolean lackRemind) {
		this.lackRemind = lackRemind;
	}
	
	/**
	 * Return the value associated with the column: score
	 */
	public java.lang.Integer getScore () {
		return score;
	}

	/**
	 * Set the value related to the column: score
	 * @param score the score value
	 */
	public void setScore (java.lang.Integer score) {
		this.score = score;
	}


	/**
	 * Return the value associated with the column: shareContent
	 */
	public java.lang.String getShareContent () {
		return shareContent;
	}

	/**
	 * Set the value related to the column: shareContent
	 * @param shareContent the shareContent value
	 */
	public void setShareContent (java.lang.String shareContent) {
		this.shareContent = shareContent;
	}


	/**
	 * Return the value associated with the column: productText
	 */
	public com.jspgou.cms.entity.ProductText getProductText () {
		return productText;
	}

	/**
	 * Set the value related to the column: productText
	 * @param productText the productText value
	 */
	public void setProductText (com.jspgou.cms.entity.ProductText productText) {
		this.productText = productText;
	}


	/**
	 * Return the value associated with the column: productExt
	 */
	public com.jspgou.cms.entity.ProductExt getProductExt () {
		return productExt;
	}

	/**
	 * Set the value related to the column: productExt
	 * @param productExt the productExt value
	 */
	public void setProductExt (com.jspgou.cms.entity.ProductExt productExt) {
		this.productExt = productExt;
	}


	/**
	 * Return the value associated with the column: brand_id
	 */
	public com.jspgou.cms.entity.Brand getBrand () {
		return brand;
	}

	/**
	 * Set the value related to the column: brand_id
	 * @param brand the brand_id value
	 */
	public void setBrand (com.jspgou.cms.entity.Brand brand) {
		this.brand = brand;
	}


	/**
	 * Return the value associated with the column: config_id
	 */
	public com.jspgou.cms.entity.ShopConfig getConfig () {
		return config;
	}

	/**
	 * Set the value related to the column: config_id
	 * @param config the config_id value
	 */
	public void setConfig (com.jspgou.cms.entity.ShopConfig config) {
		this.config = config;
	}


	/**
	 * Return the value associated with the column: category_id
	 */
	public com.jspgou.cms.entity.Category getCategory () {
		return category;
	}

	/**
	 * Set the value related to the column: category_id
	 * @param category the category_id value
	 */
	public void setCategory (com.jspgou.cms.entity.Category category) {
		this.category = category;
	}


	/**
	 * Return the value associated with the column: ptype_id
	 */
	public com.jspgou.cms.entity.ProductType getType () {
		return type;
	}

	/**
	 * Set the value related to the column: ptype_id
	 * @param type the ptype_id value
	 */
	public void setType (com.jspgou.cms.entity.ProductType type) {
		this.type = type;
	}


	/**
	 * Return the value associated with the column: website_id
	 */
	public com.jspgou.core.entity.Website getWebsite () {
		return website;
	}

	/**
	 * Set the value related to the column: website_id
	 * @param website the website_id value
	 */
	public void setWebsite (com.jspgou.core.entity.Website website) {
		this.website = website;
	}


	/**
	 * Return the value associated with the column: tags
	 */
	public java.util.Set<com.jspgou.cms.entity.ProductTag> getTags () {
		return tags;
	}

	/**
	 * Set the value related to the column: tags
	 * @param tags the tags value
	 */
	public void setTags (java.util.Set<com.jspgou.cms.entity.ProductTag> tags) {
		this.tags = tags;
	}


	

	/**
	 * Return the value associated with the column: fashions
	 */
	public java.util.Set<com.jspgou.cms.entity.ProductFashion> getFashions () {
		return fashions;
	}

	/**
	 * Set the value related to the column: fashions
	 * @param fashions the fashions value
	 */
	public void setFashions (java.util.Set<com.jspgou.cms.entity.ProductFashion> fashions) {
		this.fashions = fashions;
	}


	/**
	 * Return the value associated with the column: keywords
	 */
	public java.util.List<java.lang.String> getKeywords () {
		return keywords;
	}

	/**
	 * Set the value related to the column: keywords
	 * @param keywords the keywords value
	 */
	public void setKeywords (java.util.List<java.lang.String> keywords) {
		this.keywords = keywords;
	}

	/**
	 * Return the value associated with the column: pictures
	 */
	public java.util.List<com.jspgou.cms.entity.ProductPicture> getPictures () {
		return pictures;
	}

	/**
	 * Set the value related to the column: pictures
	 * @param pictures the pictures value
	 */
	public void setPictures (java.util.List<com.jspgou.cms.entity.ProductPicture> pictures) {
		this.pictures = pictures;
	}


	/**
	 * Return the value associated with the column: attr
	 */
	public java.util.Map<java.lang.String, java.lang.String> getAttr () {
		return attr;
	}

	/**
	 * Set the value related to the column: attr
	 * @param attr the attr value
	 */
	public void setAttr (java.util.Map<java.lang.String, java.lang.String> attr) {
		this.attr = attr;
	}



	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Product)) return false;
		else {
			com.jspgou.cms.entity.Product product = (com.jspgou.cms.entity.Product) obj;
			if (null == this.getId() || null == product.getId()) return false;
			else return (this.getId().equals(product.getId()));
		}
	}

	@Override
	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	@Override
	public String toString () {
		return super.toString();
	}

	public void setAlertInventory(java.lang.Integer alertInventory) {
		this.alertInventory = alertInventory;
	}

	public java.lang.Integer getAlertInventory() {
		return alertInventory;
	}

	public void setExendeds(java.util.List<com.jspgou.cms.entity.ProductExended> exendeds) {
		this.exendeds = exendeds;
	}

	public java.util.List<com.jspgou.cms.entity.ProductExended> getExendeds() {
		return exendeds;
	}

	public void setExpireTime(java.util.Date expireTime) {
		this.expireTime = expireTime;
	}

	public java.util.Date getExpireTime() {
		return expireTime;
	}

	public void setLiRun(java.lang.Double liRun) {
		this.liRun = liRun;
	}

	public java.lang.Double getLiRun() {
		return liRun;
	}

	public void setPopularityGroups(java.util.Set<com.jspgou.cms.entity.PopularityGroup> popularityGroups) {
		this.popularityGroups = popularityGroups;
	}

	public java.util.Set<com.jspgou.cms.entity.PopularityGroup> getPopularityGroups() {
		return popularityGroups;
	}


}