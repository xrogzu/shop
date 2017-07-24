package com.jspgou.core.manager.impl;




import com.jspgou.core.manager.FtpMng;
import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.cache.CoreCacheSvc;
import com.jspgou.core.cache.DomainCacheSvc;
import com.jspgou.core.dao.WebsiteDao;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.WebsiteMng;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* This class should preserve.
* @preserve
*/
@Service
@Transactional
public class WebsiteMngImpl implements WebsiteMng{

	@Override
	@Transactional(readOnly=true)
    public Website getWebsite(String s){
        Integer count = coreCacheSvc.getWebsiteCount();
        int i;
        if(count == null){
            i = dao.countWebsite();
            coreCacheSvc.putWebsiteCount((new Integer(i)).intValue());
        } else{
            i = count.intValue();
        }
        Long id;
        Website website;
        if(i == 1){
            id = coreCacheSvc.getWebsiteId();
            if(id != null){
                website = findById(id);
            } else{
                website = dao.getUniqueWebsite();
                coreCacheSvc.putWebsiteId(website.getId());
            }
        } else if(i > 1){
            id = domainCacheSvc.get(s);
            if(id != null){
                website = findById(id);
            } else{
                website = dao.findByDomain(s);
                if(website != null){
                    domainCacheSvc.put(website.getDomain(), website.getDomainAliasArray(), website.getId());
                }
            }
        } else{
            throw new IllegalStateException("no website data in database, please init database!");
        }
        return website;
    }

    @Override
	public Pagination getAllPage(int pageNo, int pageSize){
        return dao.getAllPage(pageNo, pageSize);
    }

    @Override
	public List<Website> getAllList(){
        return dao.getAllList();
    }

    @Override
	public Website findById(Long id){
        return dao.findById(id);
    }
    
    @Transactional(readOnly = true)
	public Website findById1(Long id) {
    	Website entity = dao.findById1(id);
		return entity;
	}

    @Override
	public Website save(Website bean){
        Website entity = dao.save(bean);
        fireOnSave(entity);
        return entity;
    }
    @Override
    public Website update1(Website bean, Integer uploadFtpId,Integer syncPageFtpId) {
    	Website entity = findById1(bean.getId());
		if (uploadFtpId != null) {
			entity.setUploadFtp(ftpMng.findById(uploadFtpId));
		} else {
			entity.setUploadFtp(null);
		}
		if (syncPageFtpId != null) {
			entity.setSyncPageFtp(ftpMng.findById(syncPageFtpId));
		} else {
			entity.setSyncPageFtp(null);
		}
	        String domain = entity.getDomain();
	        String[] as = entity.getDomainAliasArray();
	        entity = dao.updateByUpdater(new Updater<Website>(bean));
	        fireOnUpdate(domain, as, entity);
		return entity;
	}
    
    @Override
	public Website update(Website bean){
        Website entity = findById(bean.getId());
        String domain = entity.getDomain();
        String[] as = entity.getDomainAliasArray();
        entity = dao.updateByUpdater(new Updater<Website>(bean));
        fireOnUpdate(domain, as, entity);
        return entity;
    }

    @Override
	public Website deleteById(Long id) {
        Website entity = dao.deleteById(id);
        fireOnDelete(entity);
        return entity;
    }

    @Override
	public Website[] deleteByIds(Long[] ids){
        Website[] beans= new Website[ids.length];
        for(int i = 0; i < ids.length; i++){
        	beans[i] = dao.deleteById(ids[i]);
        }
        fireOnDeleteBatch(beans);
        return beans;
    }
    
    
    public void updateAttr(Long siteId,Map<String,String>attr){
	
		 Website site = findById(siteId);
		site.getAttr().putAll(attr);
	}
    
    @SuppressWarnings("unchecked")
	private void resetWebsiteCache(){
        List<Website> list = dao.getAllList();
        int i = list.size();
        if(i == 0){
            throw new IllegalStateException("no website data in database, please init database!");
        }
        coreCacheSvc.putWebsiteCount(i);
        Object object;
        if(i == 1){
            Website entity = list.get(0);
            coreCacheSvc.putWebsiteId(entity.getId());
            domainCacheSvc.removeAll();
            domainCacheSvc.put(entity.getDomain(), entity.getDomainAliasArray(), entity.getId());
        } else{
            coreCacheSvc.removeWebsiteId();
            domainCacheSvc.removeAll();
            object = list.iterator();
            while (((Iterator)object).hasNext()){
              Website localWebsite = (Website)((Iterator)object).next();
              this.domainCacheSvc.put(localWebsite.getDomain(), localWebsite.getDomainAliasArray(), localWebsite.getId());
            }
        }
    }

    private void onDomainUpdated(String s, String[] as, Website website){
        String domain = website.getDomain();
        String[] as1= website.getDomainAliasArray();
        if(!domain.equals(s) || !Arrays.equals(as1, as)){
            domainCacheSvc.remove(s, as);
            domainCacheSvc.put(domain, as1, website.getId());
        }
    }

    private void onDomainDelete(Website bean){
        resetWebsiteCache();
    }

    private void onDomainDeleteBatch(Website[] beans){
        resetWebsiteCache();
    }

    private void onDomainSave(Website bean){
        resetWebsiteCache();
    }

    private void fireOnUpdate(String s, String as[], Website bean){
        onDomainUpdated(s, as, bean);
    }

    private void fireOnDelete(Website bean){
        onDomainDelete(bean);
    }

    private void fireOnDeleteBatch(Website[] beans){
        onDomainDeleteBatch(beans);
    }

    private void fireOnSave(Website bean){
        onDomainSave(bean);
    }
    
    @Autowired
    private FtpMng ftpMng;
    private CoreCacheSvc coreCacheSvc;
    private DomainCacheSvc domainCacheSvc;
    private WebsiteDao dao;
    
	@Autowired
    public void setCoreCacheSvc(CoreCacheSvc coreCacheSvc){
        this.coreCacheSvc = coreCacheSvc;
    }
	@Autowired
    public void setDomainCacheSvc(DomainCacheSvc domainCacheSvc){
        this.domainCacheSvc = domainCacheSvc;
    }
	
	@Override
	@Transactional(readOnly=true)
	public Website get(){
		Website entity=dao.findById((long) 1);
		return entity;
	}
	
	@Override
	public void updateSsoAttr(Map<String,String> ssoAttr){
		Map<String,String> oldAttr=get().getAttr();
		Iterator<String> keys=oldAttr.keySet().iterator();
		String key=null;
		while(keys.hasNext()){
			key=keys.next();
			if(key.startsWith("sso_")){
				keys.remove();
			}
		}
		oldAttr.putAll(ssoAttr);
	}
	
	@Autowired
    public void setDao(WebsiteDao dao){
        this.dao = dao;
    }
}
