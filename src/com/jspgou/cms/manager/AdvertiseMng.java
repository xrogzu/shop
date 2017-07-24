package com.jspgou.cms.manager;

import java.util.List;
import java.util.Map;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Advertise;
/**
* This class should preserve.
* @preserve
*/
public interface AdvertiseMng {
	public Pagination getPage( Integer adspaceId,
			Boolean enabled, int pageNo, int pageSize);

	public List<Advertise> getList(Integer adspaceId, Boolean enabled);

	public Advertise findById(Integer id);

	public Advertise save(Advertise bean, Integer adspaceId,
			Map<String, String> attr);

	public Advertise update(Advertise bean, Integer adspaceId,
			Map<String, String> attr);

	public Advertise deleteById(Integer id);

	public Advertise[] deleteByIds(Integer[] ids);

	public void display(Integer id);

	public void click(Integer id);
}