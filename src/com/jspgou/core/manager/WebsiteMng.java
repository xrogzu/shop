package com.jspgou.core.manager;
import com.jspgou.common.page.Pagination;
import com.jspgou.core.entity.Website;
import java.util.List;
import java.util.Map;
/**
* This class should preserve.
* @preserve
*/
public interface WebsiteMng
{
	
    public Website getWebsite(String s);

    public Pagination getAllPage(int i, int j);

    public List<Website> getAllList();

    public Website findById(Long id);

    public Website save(Website bean);

    public Website update1(Website bean,Integer uploadFtpId,Integer syncPageFtpId);
    public Website update(Website bean);
    
    public Website deleteById(Long id);

    public Website[] deleteByIds(Long[] ids);
    
    public Website get();
    
    public void updateSsoAttr(Map<String,String> ssoAttr);
    
    public void updateAttr(Long siteId,Map<String,String>attr);
    
}
