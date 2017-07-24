package com.jspgou.cms.manager;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.entity.Shipments;
/**
* This class should preserve.
* @preserve
*/
public interface ShipmentsMng {
	
	public List<Logistics> getAllList();
	
	public Pagination getPage(Boolean isPrint,int pageNo, int pageSize);
	
	public List<Shipments> getlist(Long orderId);
	
	public void deleteByorderId(Long orderId);

	public Shipments findById(Long id);

	public Shipments save(Shipments bean);

	public Shipments update(Shipments bean);

	public Shipments deleteById(Long id);
	
	public Shipments[] deleteByIds(Long[] ids);
}