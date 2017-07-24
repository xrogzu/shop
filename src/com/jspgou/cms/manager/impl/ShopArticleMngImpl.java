package com.jspgou.cms.manager.impl;

import java.nio.channels.Channel;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopArticleDao;
import com.jspgou.cms.entity.ShopArticle;
import com.jspgou.cms.entity.ShopArticleContent;
import com.jspgou.cms.entity.ShopChannel;
import com.jspgou.cms.manager.ShopArticleContentMng;
import com.jspgou.cms.manager.ShopArticleMng;
import com.jspgou.cms.manager.ShopChannelMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopArticleMngImpl implements ShopArticleMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Long channelId,Long webId, int pageNo, int pageSize) {
		Pagination page = dao.getPage(channelId,webId, pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPageForTag(Long webId, Long channelId, int pageNo,
			int pageSize) {
		Pagination page;
		if (channelId != null) {
			page = dao.getPageByChannel(channelId, pageNo, pageSize, true);
		} else {
			page = dao.getPageByWebsite(webId, pageNo, pageSize, true);
		}
		return page;
	}

	@Override
	public List<ShopArticle> getListForTag(Long webId, Long channelId,
			int firstResult, int maxResults) {
		List<ShopArticle> list;
		if (channelId != null) {
			list = dao.getListByChannel(channelId, firstResult, maxResults,
					true);
		} else {
			list = dao.getListByWebsite(webId, firstResult, maxResults, true);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopArticle findById(Long id) {
		ShopArticle entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopArticle save(ShopArticle bean, Long channelId, String content) {
		ShopChannel channel = shopChannelMng.findById(channelId);
		bean.setChannel(channel);
		bean.init();
		dao.save(bean);
		if (content != null) {
			shopArticleContentMng.save(content, bean);
		}
		return bean;
	}

	@Override
	public ShopArticle update(ShopArticle bean, Long channelId, String content) {
		ShopArticle entity = findById(bean.getId());
		ShopArticleContent c = entity.getArticleContent();
		if (c != null) {
			c.setContent(content);
		} else {
			shopArticleContentMng.save(content, entity);
		}
		if (channelId != null) {
			entity.setChannel(shopChannelMng.findById(channelId));
		}
		Updater<ShopArticle> updater = new Updater<ShopArticle>(bean);
		entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopArticle deleteById(Long id) {
		ShopArticle bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ShopArticle[] deleteByIds(Long[] ids) {
		ShopArticle[] beans = new ShopArticle[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopArticleContentMng shopArticleContentMng;
	private ShopChannelMng shopChannelMng;
	private ShopArticleDao dao;

	@Autowired
	public void setDao(ShopArticleDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setShopChannelMng(ShopChannelMng shopChannelMng) {
		this.shopChannelMng = shopChannelMng;
	}

	@Autowired
	public void setShopArticleContentMng(
			ShopArticleContentMng shopArticleContentMng) {
		this.shopArticleContentMng = shopArticleContentMng;
	}

}