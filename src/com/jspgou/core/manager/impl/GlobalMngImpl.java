package com.jspgou.core.manager.impl;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.core.dao.GlobalDao;
import com.jspgou.core.entity.ConfigAttr;
import com.jspgou.core.entity.Global;
import com.jspgou.core.manager.GlobalMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class GlobalMngImpl  implements GlobalMng{

	
	@Transactional(readOnly = true)
	public Global get() {
		Global entity = dao.findById(1L);
		return entity;
	}
	
	
    @Override
	public Global findById(Long id){
        return dao.findById(id);
    }

    @Override
	public Global update(Global bean){
        return dao.update(bean);
    }

    public void updateConfigAttr(ConfigAttr configAttr) {
    	findIdkey().getAttr().putAll(configAttr.getAttr());
	}
    
 
    public Global findIdkey(){
		return dao.findIdkey();
	}

	private GlobalDao dao;
    
	@Autowired
    public void setDao(GlobalDao dao){
        this.dao = dao;
    }

}
