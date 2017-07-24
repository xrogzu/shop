package com.jspgou.plug.store.dao;


import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.plug.store.entity.PlugStoreConfig;

public interface PlugStoreConfigDao {
	public Pagination getPage(int pageNo, int pageSize);

	public PlugStoreConfig findById(Integer id);

	public PlugStoreConfig save(PlugStoreConfig bean);

	public PlugStoreConfig updateByUpdater(Updater<PlugStoreConfig> updater);

	public PlugStoreConfig deleteById(Integer id);

    public Class<PlugStoreConfig> getEntityClass();
}