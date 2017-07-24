package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.CustomerServiceDao;
import com.jspgou.cms.entity.CustomerService;
import com.jspgou.cms.manager.CustomerServiceMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class CustomerServiceMngImpl implements CustomerServiceMng{

	@Override
	public CustomerService findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Pagination getPagination(Boolean disable,int pageNo, int pageSize){
		return dao.getPagination(disable, pageNo, pageSize);
	}
	
	@Override
	public List<CustomerService> getList(){
		return dao.getList(false);
	}

	@Override
	public CustomerService update(CustomerService bean) {
		Updater<CustomerService> updater = new Updater<CustomerService>(bean);
		CustomerService entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public CustomerService save(CustomerService bean) {
		return dao.save(bean);
	}
	
	@Override
	public CustomerService[] updatePriority(Long[] ids, Integer[] priority) {
		CustomerService[] beans = new CustomerService[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}
	
	@Override
	public CustomerService[] deleteByIds(Long[] ids) {
		CustomerService[] beans = new CustomerService[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = dao.deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private CustomerServiceDao dao;
}

