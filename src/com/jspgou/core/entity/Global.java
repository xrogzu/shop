package com.jspgou.core.entity;
import com.jspgou.core.entity.ConfigAttr;
import com.jspgou.core.entity.base.BaseGlobal;
/**
* This class should preserve.
* @preserve
*/
public class Global extends BaseGlobal{
    private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
    public Global(){
    	super();
    }
	/**
	 * Constructor for primary key
	 */
    public Global(Long id) {
        super(id);
    }
	/* [CONSTRUCTOR MARKER END] */
    
    
    public ConfigAttr getConfigAttr() {
		ConfigAttr configAttr = new ConfigAttr(getAttr());
		return configAttr;
	}

	public void setConfigAttr(ConfigAttr configAttr) {
		getAttr().putAll(configAttr.getAttr());
	}
	
	public Boolean getQqEnable(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqEnable();
	}
	
	public Boolean getSinaEnable(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getSinaEnable();
	}
	
	public Boolean getQqWeboEnable(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqWeboEnable();
	}
	
	public String getQqID(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqID();
	}
	
	public String getSinaID(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getSinaID();
	}
	
	public String getQqWeboID(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqWeboID();
	}
	
	public Boolean getWeixinEnable(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinEnable();
	}
	
	public String getWeixinID(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinID();
	}
	
	public String getWeixinKey(){
		ConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinKey();
	}


}
