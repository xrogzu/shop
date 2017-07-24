package com.jspgou.cms.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspgou.cms.dao.WebserviceDao;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.Webservice;
import com.jspgou.cms.entity.WebserviceParam;
import com.jspgou.cms.manager.WebserviceMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;

@Service
@Transactional
public class WebserviceMngImpl implements WebserviceMng {	
	@Override
	@Transactional(readOnly=true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page=dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Webservice> getList(String type) {
		return dao.getList(type);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean hashWebservice(String type) {
		if(getList(type).size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String callWebService(Webservice webservice, Map<String, String> paramsValues) {
		String endpoint =webservice.getAddress();
		org.apache.axis.client.Service service=new org.apache.axis.client.Service();
		Call call;
		String res=null;
		try{
			call=(Call)service.createCall();
			call.setTargetEndpointAddress(endpoint);  //为Call设置服务的位置
			call.setOperationName(new QName(webservice.getTargetNamespace(),webservice.getOperate()));
			List<WebserviceParam>params=webservice.getParams();
			Object[]values=new Object[params.size()];
			for(int i=0;i<params.size();i++){
				WebserviceParam p=params.get(i);
				String defaultValue=p.getDefaultValue();
				String pValue=paramsValues.get(p.getParamName());
				if(StringUtils.isBlank(pValue)){
					values[i]=defaultValue;
				}else{
					values[i]=pValue;
				}
			}
			res=(String)call.invoke(values);
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	@Override
	@Transactional(readOnly=true)
	public Webservice findById(Integer id) {
		Webservice entity=dao.findById(id);
		return entity;
	}

	@Override
	public Webservice save(Webservice bean, String[] paramName,
			String[] defaultValue) {
		bean=dao.save(bean);
		//保存参数
		if(paramName!=null&&paramName.length>0){
			for(int i=0,len=paramName.length;i<len;i++){
				if(!StringUtils.isBlank(paramName[i])){
					bean.addToParams(paramName[i], defaultValue[i]);
				}
			}
		}
		return null;
	}

	@Override
	public Webservice update(Webservice bean, String[] paramName,
			String[] defaultValue) {
		Updater<Webservice> updater = new Updater<Webservice>(bean);
		Webservice entity = dao.updateByUpdater(updater);
		entity.getParams().clear();
		if (paramName != null && paramName.length > 0) {
			for (int i = 0, len = paramName.length; i < len; i++) {
				if (!StringUtils.isBlank(paramName[i])) {
					entity.addToParams(paramName[i], defaultValue[i]);
				}
			}
		}
		return entity;
	}

	@Override
	public Webservice deleteById(Integer id) {
		Webservice bean=dao.deleteById(id);
		return bean;
	}

	@Override
	public Webservice[] deleteByIds(Integer[] ids) {
		Webservice[] beans=new Webservice[ids.length];
		for(int i=0,len=ids.length;i<len;i++){
			beans[i]=deleteById(ids[i]);
		}
		return beans;
	}
	
	private WebserviceDao dao;
	
	@Autowired
	public void setDao(WebserviceDao dao){
		this.dao=dao;
	}

	@Override
	public void callWebService(String operate, Map<String, String> params) {
		List<Webservice> list=getList(operate);
		for(Webservice s:list){
			callWebService(s,params);
		}
		
	}

	@Override
	public void callWebService(String admin,String username, String password, String email,
			ShopMember shopmember, String operate) {
		if(hashWebservice(operate)){
			Map<String,String>paramsValues=new HashMap<String, String>();
			paramsValues.put("username", username);
			paramsValues.put("password", password);
			paramsValues.put("admin", admin);
			if(StringUtils.isNotBlank(email)){
				paramsValues.put("email", email);
			}
			if(shopmember!=null){
				if(StringUtils.isNotBlank(shopmember.getRealName())){
					paramsValues.put("realname", shopmember.getRealName());
				}
				if(shopmember.getGender()!=null){
					paramsValues.put("sex", shopmember.getGender().toString());
				}
				if(StringUtils.isNotBlank(shopmember.getMobile())){
					paramsValues.put("tel",shopmember.getMobile());
				}
			}
			callWebService(operate, paramsValues);
		}
		
	}
}
