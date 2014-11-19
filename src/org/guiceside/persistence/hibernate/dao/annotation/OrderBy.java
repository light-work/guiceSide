package org.guiceside.persistence.hibernate.dao.annotation;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在FinderByCriteria使用<br/>
 * 创建基本的Orderby,加至条件的最末尾<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public @interface OrderBy {
	/**
	 * 
	 * @return 返回需要进行排序的字段名称
	 */
	String column();

	/**
	 * 
	 * @return 返回是否为升序排列
	 */
	boolean isAsc() default true;
}
