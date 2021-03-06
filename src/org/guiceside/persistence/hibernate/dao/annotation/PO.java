package org.guiceside.persistence.hibernate.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@Persistence时候必须引用
 * 标识一个Persistence Object
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *          
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PO {

}
