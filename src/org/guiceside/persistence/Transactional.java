package org.guiceside.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 声明事务Annotation
 * 默认为3个参数 type为TransactionType.READ_WRITE和TransactionType.READ_ONLY
 * 区别在于FlushMode参数(针对Hibernate而言) rollbackOn为错误类型
 * 表示如果事务过程中异常为RuntimeException.class则不能进行提交 exceptOn为排除错误类型
 * 表示如果事务过程中异常为指定异常错误类型 则可忽略,事务允许提交
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    
	TransactionType type() default TransactionType.READ_WRITE;

	Class<? extends Exception>[] rollbackOn() default RuntimeException.class;

	Class<? extends Exception>[] exceptOn() default {};
}
