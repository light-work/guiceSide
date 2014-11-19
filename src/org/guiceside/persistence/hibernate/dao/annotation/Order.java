package org.guiceside.persistence.hibernate.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在方法中使用<br/>
 * 标识需要进行排序的排序方式参数 类型为boolean<br/>
 * group:配置与@By(group)一样的组名<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *          
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
	String group();
}
