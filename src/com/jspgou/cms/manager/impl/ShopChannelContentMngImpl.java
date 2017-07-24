package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopChannelContentDao;
import com.jspgou.cms.entity.ShopChannel;
import com.jspgou.cms.entity.ShopChannelContent;
import com.jspgou.cms.manager.ShopChannelContentMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopChannelContentMngImpl implements ShopChannelContentMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopChannelContent findById(Long id) {
		ShopChannelContent entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopChannelContent save(String content, ShopChannel channel) {
		ShopChannelContent bean = new ShopChannelContent();
		bean.setContent(content);
		bean.setChannel(channel);
		dao.save(bean);
		channel.setChannelContent(bean);
		return bean;
	}

	@Override
	public ShopChannelContent update(ShopChannelContent bean) {
		Updater<ShopChannelContent> updater = new Updater<ShopChannelContent>(
				bean);
		ShopChannelContent entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopChannelContent deleteById(Long id) {
		ShopChannelContent bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ShopChannelContent[] deleteByIds(Long[] ids) {
		ShopChannelContent[] beans = new ShopChannelContent[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopChannelContentDao dao;

	@Autowired
	public void setDao(ShopChannelContentDao dao) {
		this.dao = dao;
	}
}