package com.jspgou.cms.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ConsultDao;
import com.jspgou.cms.entity.Consult;
import com.jspgou.cms.manager.ConsultMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ShopMemberMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ConsultMngImpl implements ConsultMng {
	@Override
	@Transactional(readOnly = true)
	public Consult findById(Long id) {
		Consult entity = dao.findById(id);
		return entity;
	}

	@Override
	public Consult saveOrUpdate(Long productId,String content,Long memberId){
		Consult bean=new Consult();
		bean.setConsult(content);
		bean.setMember(memberMng.findById(memberId));
		bean.setTime(new Date());
		bean.setProduct(productMng.findById(productId));
		Consult consult=dao.getSameConsult(memberId);
		Long time=System.currentTimeMillis();
		if(consult==null){
			return dao.saveOrUpdate(bean);
		}else{
			if((time-consult.getTime().getTime())/1000<30){
				return null;
			}else{
				return dao.saveOrUpdate(bean);
			}
		}
		
	}
	
	@Override
	public List<Consult> findByProductId(Long productId) {
		return dao.findByProductId(productId);
	}
	
	@Override
	public Pagination getPageByMember(Long memberId,int pageNo,int pageSize,boolean cache){
		return dao.getPageByMember(memberId, pageNo, pageSize, cache);
	}
	
	@Override
	public Pagination getPage(Long productId,String userName,String productName,
			Date startTime,Date endTime,int pageNo,int pageSize,Boolean cache){
		return dao.getPage(productId,userName,productName,startTime,endTime,pageNo, pageSize, cache);
	}


	@Override
	public Pagination getVisiblePage(String userName,
			String productName, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		return dao.getVisiblePage(userName,productName,startTime,endTime,pageNo, pageSize);
	}
	
	@Override
	public Consult update(Consult Consult) {
		return dao.update(Consult);
	}

	@Override
	public Consult deleteById(Long id) {
		Consult bean = dao.deleteById(id);
		return bean;
	}
	
	@Override
	public Consult[] deleteByIds(Long[] ids){
		Consult[] beans = new Consult[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private ShopMemberMng memberMng;
	private ProductMng productMng;
	private ConsultDao dao;
	
	@Autowired
	public void setProductMng(ProductMng productMng) {
		this.productMng = productMng;
	}

	@Autowired
	public void setDao(ConsultDao dao) {
		this.dao = dao;
	}

}