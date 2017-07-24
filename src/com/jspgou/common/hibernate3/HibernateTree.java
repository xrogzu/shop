package com.jspgou.common.hibernate3;

/**
* This class should preserve.
* @preserve
*/
public interface HibernateTree{
	/**
	 * 默认树左边属性名称
	 */
	public static final String DEF_LEFT_NAME = "lft";
	/**
	 * 默认树右边属性名称
	 */
	public static final String DEF_RIGHT_NAME = "rgt";
	/**
	 * 默认父节点属性名称
	 */
	public static final String DEF_PARENT_NAME = "parent";
	/**
	 * 实体类别名
	 */
	public static final String ENTITY_ALIAS = "bean";

	/**
	 * 获得树左边属性名称
	 * 
	 * @return
	 */
	public String getLftName();

	/**
	 * 获得树右边属性名称
	 * 
	 * @return
	 */
	public String getRgtName();

	/**
	 * 获得父节点属性名称
	 * 
	 * @return
	 */
	public String getParentName();

	/**
	 * 设置树左边值
	 * 
	 * @param lft
	 */
    public Integer getLft();

	/**
	 * 设置树左边值
	 * 
	 * @param lft
	 */
    public void setLft(Integer integer);

	/**
	 * 获得树右边值
	 * 
	 * @return
	 */
    public Integer getRgt();

	/**
	 * 设置数右边值
	 * 
	 * @param rgt
	 */
    public void setRgt(Integer integer);

	/**
	 * 获得父节点ID
	 * 
	 * @return 如果没有父节点，则返回null。
	 */
    public Long getParentId();

	/**
	 * 获得树ID
	 * 
	 * @return
	 */
    public Long getId();
    
	/**
	 * 获得附加条件
	 * 
	 * 通过附加条件可以维护多棵树相互独立的树，附加条件使用hql语句，实体别名为bean。例如：bean.website.id=5
	 * 
	 * @return 为null则不添加任何附加条件
	 * @see HibernateTree#ENTITY_ALIAS
	 */
	public String getTreeCondition();

}
