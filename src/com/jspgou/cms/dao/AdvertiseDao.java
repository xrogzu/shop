package com.jspgou.cms.dao;

import java.util.List;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.Advertise;

/**
* This class should preserve.
* @preserve
*/
public interface AdvertiseDao {
	public Pagination getPage( Integer adspaceId,
			Boolean enabled, int pageNo, int pageSize);

	public List<Advertise> getList(Integer adspaceId, Boolean enabled);

	public Advertise findById(Integer id);

	public Advertise save(Advertise bean);

	public Advertise updateByUpdater(Updater<Advertise> updater);

	public Advertise deleteById(Integer id);
}