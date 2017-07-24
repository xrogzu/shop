package com.jspgou.cms.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_shop_logistics table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_shop_logistics"
 * This class should preserve.
 * @preserve
 */

public abstract class BaseLogistics  implements Serializable {

	public static String REF = "Logistics";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";
	public static String PROP_WEB_URL = "webUrl";
	public static String PROP_PRIORITY = "priority";
	public static String PROP_LOGO_PATH = "logoPath";


	// constructors
	public BaseLogistics () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseLogistics (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseLogistics (
		java.lang.Long id,
		java.lang.String name,
		java.lang.Integer priority) {

		this.setId(id);
		this.setName(name);
		this.setPriority(priority);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.String webUrl;
	private java.lang.String logoPath;
	private java.lang.Integer priority;
	
	private java.lang.Boolean courier;
	private java.lang.String imgUrl;
	private java.lang.Integer evenSpacing;
	private java.lang.Double fnt;
	private java.lang.Double fnz;
	private java.lang.Double fat;
	private java.lang.Double faz;
	private java.lang.Double fpt;
	private java.lang.Double fpz;
	private java.lang.Double snt;
	private java.lang.Double snz;
	private java.lang.Double sat;
	private java.lang.Double saz;
	private java.lang.Double spt;
	private java.lang.Double spz;
	
	private java.lang.Double fnw;
	private java.lang.Double fnh;
	private java.lang.Double faw;
	private java.lang.Double fah;
	private java.lang.Double fpw;
	private java.lang.Double fph;
	private java.lang.Double snw;
	private java.lang.Double snh;
	private java.lang.Double saw;
	private java.lang.Double sah;
	private java.lang.Double spw;
	private java.lang.Double sph;
	
	private java.lang.String fnwh;
	private java.lang.String fawh;
	private java.lang.String fpwh;
	private java.lang.String snwh;
	private java.lang.String sawh;
	private java.lang.String spwh;

	// collections
	private java.util.Set<com.jspgou.cms.entity.LogisticsText> logisticsTextSet;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="logistics_id"
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
	 * Return the value associated with the column: web_url
	 */
	public java.lang.String getWebUrl () {
		return webUrl;
	}

	/**
	 * Set the value related to the column: web_url
	 * @param webUrl the web_url value
	 */
	public void setWebUrl (java.lang.String webUrl) {
		this.webUrl = webUrl;
	}


	/**
	 * Return the value associated with the column: logo_path
	 */
	public java.lang.String getLogoPath () {
		return logoPath;
	}

	/**
	 * Set the value related to the column: logo_path
	 * @param logoPath the logo_path value
	 */
	public void setLogoPath (java.lang.String logoPath) {
		this.logoPath = logoPath;
	}


	/**
	 * Return the value associated with the column: priority
	 */
	public java.lang.Integer getPriority () {
		return priority;
	}

	/**
	 * Set the value related to the column: priority
	 * @param priority the priority value
	 */
	public void setPriority (java.lang.Integer priority) {
		this.priority = priority;
	}


	public java.lang.Boolean getCourier(){
		return courier;
	}
	public void setCourier(java.lang.Boolean courier){
		this.courier=courier;
	}
	
	public java.lang.String getImgUrl(){
		return imgUrl; 
	}
	
	public void setImgUrl(java.lang.String imgUrl){
		this.imgUrl=imgUrl;
	}
	
	public java.lang.Integer getEvenSpacing(){
		return evenSpacing;
	}
	
	public void setEvenSpacing(java.lang.Integer evenSpacing){
		this.evenSpacing=evenSpacing;
	}
	
	public java.lang.Double getFnt(){
		return fnt;
	}
	
   public void setFnt(java.lang.Double fnt){
	   this.fnt=fnt;
	   
   }
   
   public java.lang.Double getFnz() {
		return fnz;
	}

	public void setFnz(java.lang.Double fnz) {
		this.fnz = fnz;
	}

	public java.lang.Double getFat() {
		return fat;
	}

	public void setFat(java.lang.Double fat) {
		this.fat = fat;
	}

	public java.lang.Double getFaz() {
		return faz;
	}

	public void setFaz(java.lang.Double faz) {
		this.faz = faz;
	}

	public java.lang.Double getFpt() {
		return fpt;
	}

	public void setFpt(java.lang.Double fpt) {
		this.fpt = fpt;
	}

	public java.lang.Double getFpz() {
		return fpz;
	}

	public void setFpz(java.lang.Double fpz) {
		this.fpz = fpz;
	}

	public java.lang.Double getSnt() {
		return snt;
	}

	public void setSnt(java.lang.Double snt) {
		this.snt = snt;
	}

	public java.lang.Double getSnz() {
		return snz;
	}

	public void setSnz(java.lang.Double snz) {
		this.snz = snz;
	}

	public java.lang.Double getSat() {
		return sat;
	}

	public void setSat(java.lang.Double sat) {
		this.sat = sat;
	}

	public java.lang.Double getSaz() {
		return saz;
	}

	public void setSaz(java.lang.Double saz) {
		this.saz = saz;
	}

	public java.lang.Double getSpt() {
		return spt;
	}

	public void setSpt(java.lang.Double spt) {
		this.spt = spt;
	}

	public java.lang.Double getSpz() {
		return spz;
	}

	public void setSpz(java.lang.Double spz) {
		this.spz = spz;
	}
	
	
	public java.lang.Double getFnw() {
		return fnw;
	}

	public java.lang.Double getFnh() {
		return fnh;
	}

	public java.lang.Double getFaw() {
		return faw;
	}

	public java.lang.Double getFah() {
		return fah;
	}

	public java.lang.Double getFpw() {
		return fpw;
	}

	public java.lang.Double getFph() {
		return fph;
	}

	public java.lang.Double getSnw() {
		return snw;
	}

	public java.lang.Double getSnh() {
		return snh;
	}

	public java.lang.Double getSaw() {
		return saw;
	}

	public java.lang.Double getSah() {
		return sah;
	}

	public java.lang.Double getSpw() {
		return spw;
	}

	public java.lang.Double getSph() {
		return sph;
	}

	public void setFnw(java.lang.Double fnw) {
		this.fnw = fnw;
	}

	public void setFnh(java.lang.Double fnh) {
		this.fnh = fnh;
	}

	public void setFaw(java.lang.Double faw) {
		this.faw = faw;
	}

	public void setFah(java.lang.Double fah) {
		this.fah = fah;
	}

	public void setFpw(java.lang.Double fpw) {
		this.fpw = fpw;
	}

	public void setFph(java.lang.Double fph) {
		this.fph = fph;
	}

	public void setSnw(java.lang.Double snw) {
		this.snw = snw;
	}

	public void setSnh(java.lang.Double snh) {
		this.snh = snh;
	}

	public void setSaw(java.lang.Double saw) {
		this.saw = saw;
	}

	public void setSah(java.lang.Double sah) {
		this.sah = sah;
	}

	public void setSpw(java.lang.Double spw) {
		this.spw = spw;
	}

	public void setSph(java.lang.Double sph) {
		this.sph = sph;
	}
  
	public java.lang.String getFnwh() {
		return fnwh;
	}
	 public void setFnwh(java.lang.String Fnwh){
		 this.fnwh=Fnwh;
	 }

	public java.lang.String getFawh() {
		return fawh;
	}
   public void setFawh(java.lang.String fawh){
	   this.fawh=fawh;
   }
	public java.lang.String getFpwh() {
		return fpwh;
	}
    public void setFpwh(java.lang.String fpwh){
    	this.fpwh=fpwh;
    }
	public java.lang.String getSnwh() {
		return snwh;
	}
	public void setSnwh(java.lang.String snwh){
		this.snwh=snwh;
	}

	public java.lang.String getSawh() {
		return sawh;
	}
    public void setSawh(java.lang.String sawh){
    	this.sawh=sawh;
    }
	public java.lang.String getSpwh() {
		return spwh;
	}
    public void setSpwh(java.lang.String spwh){
    	this.spwh=spwh;
    }
	
	/**
	 * Return the value associated with the column: logisticsTextSet
	 */
	public java.util.Set<com.jspgou.cms.entity.LogisticsText> getLogisticsTextSet () {
		return logisticsTextSet;
	}

	/**
	 * Set the value related to the column: logisticsTextSet
	 * @param logisticsTextSet the logisticsTextSet value
	 */
	public void setLogisticsTextSet (java.util.Set<com.jspgou.cms.entity.LogisticsText> logisticsTextSet) {
		this.logisticsTextSet = logisticsTextSet;
	}



	@Override
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jspgou.cms.entity.Logistics)) return false;
		else {
			com.jspgou.cms.entity.Logistics logistics = (com.jspgou.cms.entity.Logistics) obj;
			if (null == this.getId() || null == logistics.getId()) return false;
			else return (this.getId().equals(logistics.getId()));
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


}