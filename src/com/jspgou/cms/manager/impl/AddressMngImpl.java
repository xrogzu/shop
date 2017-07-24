package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.AddressDao;
import com.jspgou.cms.entity.Address;
import com.jspgou.cms.manager.AddressMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class AddressMngImpl implements AddressMng {
	@Override
	public List<Address> getListById(Long parentId){
		return dao.getListById(parentId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Override
	public Pagination getPageByParentId(Long parentId,int pageNo, int pageSize){
		Pagination page = dao.getPageByParentId(parentId,pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Address findById(Long id) {
		Address entity = dao.findById(id);
		return entity;
	}

	@Override
	public Address save(Address bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public Address update(Address bean) {
		Updater<Address> updater = new Updater<Address>(bean);
		Address entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public Address deleteById(Long id) {
		Address bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Address[] deleteByIds(Long[] ids) {
		Address[] beans = new Address[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Override
	public Address[] updatePriority(Long[] ids, Integer[] priority) {
		Address[] beans = new Address[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}

	private AddressDao dao;

	@Autowired
	public void setDao(AddressDao dao) {
		this.dao = dao;
	}
}