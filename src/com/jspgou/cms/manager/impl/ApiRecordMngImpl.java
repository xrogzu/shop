package com.jspgou.cms.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.cms.dao.ApiRecordDao;
import com.jspgou.cms.entity.ApiInfo;
import com.jspgou.cms.entity.ApiRecord;
import com.jspgou.cms.manager.ApiAccountMng;
import com.jspgou.cms.manager.ApiInfoMng;
import com.jspgou.cms.manager.ApiRecordMng;

@Service
@Transactional
public class ApiRecordMngImpl implements ApiRecordMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiRecord findById(Long id) {
		ApiRecord entity = dao.findById(id);
		return entity;
	}

	public ApiRecord save(ApiRecord bean) {
		dao.save(bean);
		return bean;
	}

	public ApiRecord update(ApiRecord bean) {
		Updater<ApiRecord> updater = new Updater<ApiRecord>(bean);
		ApiRecord entity = dao.updateByUpdater(updater);
		return entity;
	}

	public ApiRecord deleteById(Long id) {
		ApiRecord bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiRecord[] deleteByIds(Long[] ids) {
		ApiRecord[] beans = new ApiRecord[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public void callApiRecord(String ipAddr, String appId, String apiUrl,String sign){
		ApiRecord apiRecord = new ApiRecord();
		apiRecord.setApiAccount(apiAccountMng.findByAppId(appId));
		if(apiInfoMng.findByApiUrl(apiUrl)!=null){
			apiRecord.setApiInfo(apiInfoMng.findByApiUrl(apiUrl));
		}else{
			ApiInfo apiInfo = new ApiInfo();
			apiInfo.setApiName("接口");
			apiInfo.setApiUrl(apiUrl);
			apiInfo.setApiCode("ApiCode");
			apiInfo.setDisabled(false);
			apiInfo.setLimitCallDay(0);
			apiRecord.setApiInfo(apiInfoMng.save(apiInfo));	
		}
		Date now = new Timestamp(System.currentTimeMillis());
		apiRecord.setApiCallTime(now);
		apiRecord.setApiIp(ipAddr);
		apiRecord.setSign(sign);
		apiRecord.setCallTimeStamp(System.currentTimeMillis());
		save(apiRecord);
	}
	
	public ApiRecord findBySign(String sign,String appId){
		return dao.findBySign(sign, appId);
	}

	private ApiRecordDao dao;

	@Autowired
	public void setDao(ApiRecordDao dao) {
		this.dao = dao;
	}
	
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiInfoMng apiInfoMng;
}