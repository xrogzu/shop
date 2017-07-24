package com.jspgou.core.cache.impl;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jspgou.core.cache.CoreCacheSvc;
/**
* This class should preserve.
* @preserve
*/
@Service
public class CoreCacheSvcImpl implements CoreCacheSvc{
	
    private static final String WEBSITE_COUNT = "core_website_count";
    private static final String WEBSITE_ID = "core_website_id";
    private Ehcache globalCache;
    
	@Autowired
    public void setGlobalCache(@Qualifier("global") Ehcache globalCache){
        this.globalCache = globalCache;
    }

    @Override
	public void putWebsiteCount(int paramInt){
        this.globalCache.put(new Element(WEBSITE_COUNT, Integer.valueOf(paramInt)));
    }

    @Override
	public Integer getWebsiteCount(){
        Element element = this.globalCache.get(WEBSITE_COUNT);
        if(element != null){
            return (Integer)element.getValue();
        }else{
            return null;
        }
    }

    @Override
	public void putWebsiteId(Long paramLong){
        this.globalCache.put(new Element(WEBSITE_ID, paramLong));
    }

    @Override
	public Long getWebsiteId(){
        Element element = globalCache.get(WEBSITE_ID);
        if(element != null){
            return (Long)element.getValue();
        }else{
            return null;
        }
    }

    @Override
	public boolean removeWebsiteId(){
        return this.globalCache.remove(WEBSITE_ID);
    }
}
