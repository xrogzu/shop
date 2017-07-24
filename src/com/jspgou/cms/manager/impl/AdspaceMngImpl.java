package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.AdspaceDao;
import com.jspgou.cms.entity.Adspace;
import com.jspgou.cms.manager.AdspaceMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class AdspaceMngImpl implements AdspaceMng {

	@Override
	public Adspace deleteById(Integer id) {
		return dao.deleteById(id);
	}

	@Override
	public Adspace[] deleteByIds(Integer[] ids) {
		Adspace[] beans = new Adspace[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
		
	}

	@Override
	public Adspace findById(Integer id) {
		return dao.findById(id);
	}
	
	@Override
	public List<Adspace> getList(){
		return dao.getList();
	}

	@Override
	public Adspace save(Adspace bean) {
		return dao.save(bean);
	}
	
	@Override
	public Adspace updateByAdspacenumb(Integer AdspaceId,Integer AdspaceNumb,Integer shopMemberId){
		return null;
	}

	@Override
	public Adspace updateByUpdater(Adspace bean) {
		Updater<Adspace> updater = new Updater<Adspace>(bean);
			return dao.updateByUpdater(updater);
		
	}
	
      @Autowired
      private AdspaceDao dao;
}