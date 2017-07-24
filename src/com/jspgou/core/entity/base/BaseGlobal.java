package com.jspgou.core.entity.base;

import com.jspgou.core.entity.Global;
import java.io.Serializable;

/**
 * This is an object that contains data related to the jo_authentication table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jo_authentication"
 * This class should preserve.
 * @preserve
*/

public abstract class BaseGlobal implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String REF = "Global";
    public static String PROP_CONTEXT_PATH = "contextPath";
    public static String PROP_PORT = "port";
    public static String PROP_ID = "id";
	// constructors
    public BaseGlobal(){
        initialize();
    }

	/**
	 * Constructor for primary key
	 */
    public BaseGlobal(Long id){
        this.setId(id);
        initialize();
    }

    protected void initialize(){}
    
	private int hashCode = Integer.MIN_VALUE;
	
	// primary key
    private Long id;
    
	// fields
    private String contextPath;
    private Integer port;
	private java.lang.String defImg;
	private java.lang.String treaty;
	private Integer activeScore;
	private Integer stockWarning;
	private java.lang.String processUrl;
	private java.util.Map<java.lang.String, java.lang.String> attr;
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    public String getContextPath(){
        return contextPath;
    }

    public void setContextPath(String contextPath){
        this.contextPath = contextPath;
    }
    
    public Integer getPort(){
        return port;
    }

    public void setPort(Integer port){
        this.port = port;
    }

    public java.lang.String getDefImg() {
		return defImg;
	}

	public void setDefImg(java.lang.String defImg) {
		this.defImg = defImg;
	}

	public java.lang.String getTreaty() {
		return treaty;
	}

	public void setTreaty(java.lang.String treaty) {
		this.treaty = treaty;
	}

	public Integer getActiveScore() {
		return activeScore;
	}

	public void setActiveScore(Integer activeScore) {
		this.activeScore = activeScore;
	}
	
	public Integer getStockWarning() {
		return stockWarning;
	}

	public void setStockWarning(Integer stockWarning) {
		this.stockWarning = stockWarning;
	}
	
	public java.lang.String getProcessUrl() {
		return processUrl;
	}

	public void setProcessUrl(java.lang.String processUrl) {
		this.processUrl = processUrl;
	}
	
	

	public java.util.Map<java.lang.String, java.lang.String> getAttr () {
		return attr;
	}


	public void setAttr (java.util.Map<java.lang.String, java.lang.String> attr) {
		this.attr = attr;
	}
	
	@Override
	public boolean equals(Object obj){
        if(null == obj) return false;
        if(!(obj instanceof Global)) return false;
        else{
            Global global = (Global)obj;
            if(null == getId() || null == global.getId())return false;
            else return (getId().equals(global.getId()));
        }
    }

    @Override
	public int hashCode(){
        if(Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId())  return super.hashCode();
            else{
               String hashStr = this.getClass().getName()+":"+this.getId().hashCode();
               this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    @Override
	public String toString(){
        return super.toString();
    }

}
