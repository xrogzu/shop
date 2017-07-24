package com.jspgou.core.manager;

import com.jspgou.core.entity.ConfigAttr;
import com.jspgou.core.entity.Global;
/**
* This class should preserve.
* @preserve
*/
public interface GlobalMng{
	
	public Global get();

    public Global findById(Long id);

    public Global update(Global bean);
    
	public void updateConfigAttr(ConfigAttr configAttr);
	
	public Global findIdkey();
}
