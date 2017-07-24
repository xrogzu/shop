package com.jspgou.cms.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.cms.dao.DataBackupDao;
import com.jspgou.cms.entity.DataBackup;
import com.jspgou.cms.manager.DataBackupMng;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class DataBackupMngImpl implements DataBackupMng {


	@Override
	public DataBackup getDataBackup() {
		return dao.getDataBackup();
	}
	
	@Override
	public DataBackup update(DataBackup bean) {
		Updater<DataBackup> updater = new Updater<DataBackup>(bean);
		DataBackup entity = dao.updateByUpdater(updater);
		return entity;
	}
	
	@Autowired
	private DataBackupDao dao;
}

