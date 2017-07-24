package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jspgou.cms.dao.PosterDao;
import com.jspgou.cms.entity.Poster;
import com.jspgou.cms.manager.PosterMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class PosterMngImpl implements PosterMng {
	@Override
	@Transactional(readOnly = true)
	public Poster findById(Integer id) {
		Poster entity = dao.findById(id);
		return entity;
	}

	@Override
	public Poster saveOrUpdate(Poster bean){
		dao.saveOrUpdate(bean);
		return bean;
		
	}
	
	@Override
	public List<Poster> getPage(){
		return dao.getPage();
	}		

	@Override
	public Poster update(Poster Poster) {
		return dao.update(Poster);
	}
	
	@Override
	public void deleteByIds(Integer[] ids){
		for(Integer id : ids){
			this.deleteById(id);
		}
	}

	@Override
	public Poster deleteById(Integer id) {
		Poster bean = dao.deleteById(id);
		return bean;
	}

	private PosterDao dao;
	
	@Autowired
	public void setDao(PosterDao dao) {
		this.dao = dao;
	}
}