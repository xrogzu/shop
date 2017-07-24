package com.jspgou.cms.manager;

import java.util.List;
import com.jspgou.cms.entity.Address;
import com.jspgou.common.page.Pagination;
/**
* This class should preserve.
* @preserve
*/
public interface AddressMng {
	public List<Address> getListById(Long parentId);
	
	public Pagination getPage(int pageNo, int pageSize);
	
	public Pagination getPageByParentId(Long parentId,int pageNo, int pageSize);

	public Address findById(Long id);

	public Address save(Address bean);

	public Address update(Address bean);

	public Address deleteById(Long id);
	
	public Address[] deleteByIds(Long[] ids);
	
	/**
	 * 更新排列顺序
	 */
	public Address[] updatePriority(Long[] ids, Integer[] priority);
}