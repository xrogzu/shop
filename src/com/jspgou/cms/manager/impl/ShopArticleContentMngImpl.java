package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ShopArticleContentDao;
import com.jspgou.cms.entity.ShopArticle;
import com.jspgou.cms.entity.ShopArticleContent;
import com.jspgou.cms.manager.ShopArticleContentMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopArticleContentMngImpl implements ShopArticleContentMng {
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public ShopArticleContent findById(Long id) {
		ShopArticleContent entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopArticleContent save(String content, ShopArticle article) {
		ShopArticleContent bean = new ShopArticleContent();
		bean.setContent(content);
		bean.setArticle(article);
		dao.save(bean);
		article.setArticleContent(bean);
		return bean;
	}

	@Override
	public ShopArticleContent update(ShopArticleContent bean) {
		Updater<ShopArticleContent> updater = new Updater<ShopArticleContent>(
				bean);
		ShopArticleContent entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopArticleContent deleteById(Long id) {
		ShopArticleContent bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ShopArticleContent[] deleteByIds(Long[] ids) {
		ShopArticleContent[] beans = new ShopArticleContent[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopArticleContentDao dao;

	@Autowired
	public void setDao(ShopArticleContentDao dao) {
		this.dao = dao;
	}
}