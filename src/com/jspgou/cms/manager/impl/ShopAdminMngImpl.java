package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Admin;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.AdminMng;
import com.jspgou.core.manager.UserMng;
import com.jspgou.core.manager.WebsiteMng;
import com.jspgou.cms.dao.ShopAdminDao;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.manager.ShopAdminMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopAdminMngImpl implements ShopAdminMng {
	@Override
	public ShopAdmin getByUserId(Long userId, Long webId) {
		Admin admin = adminMng.getByUserId(userId, webId);
		if (admin == null) {
			return null;
		}
		return findById(admin.getId());
	}

	@Override
	public ShopAdmin register(String username, String password,Boolean viewonlyAdmin,String email,
			String ip, boolean disabled, Long webId, 
			ShopAdmin shopAdmin) {
		Website web = websiteMng.findById(webId);
		Admin admin = adminMng.register(username, password, email, ip,disabled,web,viewonlyAdmin);
		shopAdmin.setAdmin(admin);
		shopAdmin.setWebsite(web);
		return save(shopAdmin);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long webId, int pageNo, int pageSize) {
		Pagination page = dao.getPage(webId, pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopAdmin findById(Long id) {
		ShopAdmin entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopAdmin save(ShopAdmin bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopAdmin update(ShopAdmin bean, String password, Boolean disabled,
			String email,Boolean viewonlyAdmin) {
		ShopAdmin entity = findById(bean.getId());
		Admin admin = entity.getAdmin();
		userMng.updateUser(admin.getUser().getId(), password, email);
		if (disabled != null) {
			admin.setDisabled(disabled);
		}
		/*admin.setViewonlyAdmin(viewonlyAdmin);*/
		Updater<ShopAdmin> updater = new Updater<ShopAdmin>(bean);
		entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopAdmin deleteById(Long id) {
		ShopAdmin bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ShopAdmin[] deleteByIds(Long[] ids) {
		ShopAdmin[] beans = new ShopAdmin[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
     

	//*添加方法,findByName
	public ShopAdmin findByName(String name){
		
		return dao.findByName(name);
	}
	
	
	
	private UserMng userMng;
	private WebsiteMng websiteMng;
	private AdminMng adminMng;
	private ShopAdminDao dao;

	@Autowired
	public void setDao(ShopAdminDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setAdminMng(AdminMng adminMng) {
		this.adminMng = adminMng;
	}

	@Autowired
	public void setWebsiteMng(WebsiteMng websiteMng) {
		this.websiteMng = websiteMng;
	}

	@Autowired
	public void setUserMng(UserMng userMng) {
		this.userMng = userMng;
	}

	@Override
	public ShopAdmin getByUsername(String username) {
		return dao.getByUsername(username);
	}
}