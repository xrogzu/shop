package com.jspgou.cms.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.ShopMemberGroupDao;
import com.jspgou.cms.entity.ShopMemberGroup;
import com.jspgou.cms.manager.ShopMemberGroupMng;

/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class ShopMemberGroupMngImpl implements ShopMemberGroupMng {
	/**
	 * @see ShopMemberGroupMng#findGroupByScore(Long, int)
	 */
	@Override
	public ShopMemberGroup findGroupByScore(Long webId, int score) {
		List<ShopMemberGroup> groupList = dao.getList(webId, true);
		int size = groupList.size();
		if (size < 1) {
			throw new IllegalStateException(
					"ShopMmeberGroup not found in website id=" + webId);
		} else if (size == 1) {
			return groupList.get(0);
		}
		ShopMemberGroup group = groupList.get(0);
		ShopMemberGroup temp;
		for (int i = size - 1; i > 0; i--) {
			temp = groupList.get(i);
			if (score > temp.getScore()) {
				group = temp;
				break;
			}
		}
		return group;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ShopMemberGroup> getList(Long webId) {
		return dao.getList(webId, false);
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<ShopMemberGroup> getList() {
		return dao.getList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ShopMemberGroup findById(Long id) {
		ShopMemberGroup entity = dao.findById(id);
		return entity;
	}

	@Override
	public ShopMemberGroup save(ShopMemberGroup bean) {
		dao.save(bean);
		return bean;
	}

	@Override
	public ShopMemberGroup update(ShopMemberGroup bean) {
		Updater<ShopMemberGroup> updater = new Updater<ShopMemberGroup>(bean);
		ShopMemberGroup entity = dao.updateByUpdater(updater);
		return entity;
	}

	@Override
	public ShopMemberGroup deleteById(Long id) {
		ShopMemberGroup bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public ShopMemberGroup[] deleteByIds(Long[] ids) {
		ShopMemberGroup[] beans = new ShopMemberGroup[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ShopMemberGroupDao dao;

	@Autowired
	public void setDao(ShopMemberGroupDao dao) {
		this.dao = dao;
	}
}