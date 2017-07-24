package com.jspgou.core.manager;

import com.jspgou.core.entity.Admin;
import com.jspgou.core.entity.Website;
/**
* This class should preserve.
* @preserve
*/
public interface AdminMng{
	
	public Admin getByUsername(String username);
	
    public Admin getByUserId(Long userId, Long webId);

    public Admin register(String username, String password, String email, String ip, Boolean disabled, Website website,Boolean viewonlyAdmin);

    public Admin findById(Long id);

    public abstract Admin save(Admin bean);

    public abstract Admin update(Admin bean);

    public abstract Admin deleteById(Long id);

    public abstract Admin[] deleteByIds(Long[] ids);
    
    public void updateRole(Admin admin,Integer[] roleIds);
    
    public void addRole(Admin admin,Integer[] roleIds);
    
}
