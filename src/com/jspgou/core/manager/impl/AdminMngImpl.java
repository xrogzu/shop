package com.jspgou.core.manager.impl;
import com.jspgou.cms.manager.ShopMemberGroupMng;
import com.jspgou.cms.manager.WebserviceMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.core.dao.AdminDao;
import com.jspgou.core.entity.Admin;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.AdminMng;
import com.jspgou.core.manager.RoleMng;
import com.jspgou.core.manager.UserMng;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class AdminMngImpl  implements AdminMng{
	
	@Override
	public Admin getByUsername(String username){
		return dao.getByUsername(username);
	}
	
    @Override
	public Admin getByUserId(Long userId, Long webId){
        return dao.getByUserId(userId, webId);
    }

    @Override
	public Admin register(String username, String password, String email,
    		String ip, Boolean disabled,Website website,Boolean viewonlyAdmin){
        Admin entity = new Admin();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        com.jspgou.core.entity.User user = userMng.register(username, password, email, ip, timestamp);
        entity.setCreateTime(timestamp);
        entity.setUser(user);
        entity.setWebsite(website);
        entity.setDisabled(disabled);
        entity.setViewonlyAdmin(viewonlyAdmin);
        return save(entity);
    }

    @Override
	public Admin findById(Long id){
        return dao.findById(id);
    }

    @Override
	public Admin save(Admin bean){
        bean.init();
        return dao.save(bean);
    }

    @Override
	public Admin update(Admin bean){
        return dao.updateByUpdater(new Updater<Admin>(bean));
    }

    @Override
	public Admin deleteById(Long id){
        return dao.deleteById(id);
    }

    @Override
	public Admin[] deleteByIds(Long[] ids){
        Admin[] beans = new Admin[ids.length];
        for(int i = 0; i < ids.length; i++){
        	beans[i] = deleteById(ids[i]);
        }
        return beans;
    }
    
    @Override
	public void updateRole(Admin admin,Integer[] roleIds){
    	// 更新角色
		admin.getRoles().clear();
		if (roleIds != null) {
			for (Integer rid : roleIds) {
				admin.addToRoles(roleMng.findById(rid));
			}
		}
    }
    
    @Override
	public void addRole(Admin admin,Integer[] roleIds){
    	if (roleIds != null) {
			for (Integer rid : roleIds) {
				admin.addToRoles(roleMng.findById(rid));
			}
		}
        
    }
    
    @Autowired
    private WebserviceMng webserviceMng;
    @Autowired
    private ShopMemberGroupMng shopmemberGroupMng;
    private UserMng userMng;
    private AdminDao dao;
    protected RoleMng roleMng;

	@Autowired
    public void setDao(AdminDao dao){
        this.dao = dao;
    }

	@Autowired
    public void setUserMng(UserMng userMng){
        this.userMng = userMng;
    }
	
	@Autowired
    public void setRoleMng(RoleMng roleMng){
        this.roleMng = roleMng;
    }

}
