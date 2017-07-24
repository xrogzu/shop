package com.jspgou.core.dao;

import com.jspgou.common.hibernate3.Updater;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Website;
import java.util.List;
/**
* This class should preserve.
* @preserve
*/
public interface WebsiteDao{
    public Website getUniqueWebsite();

    public int countWebsite();

    public Website findByDomain(String name);

    public Pagination getAllPage(int pageNo, int pageSize);

    public List<Website> getAllList();

    public Website findById(Long id);
    
    public Website findById1(Long id);
    
    public Website save(Website bean);

    public Website updateByUpdater(Updater<Website> updater);

    public Website deleteById(Long id);
    
}
