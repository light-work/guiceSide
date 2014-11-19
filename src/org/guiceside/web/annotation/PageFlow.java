package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 定义跳转契约注解<br/>
 * result() 为@Result注解
 * 缺少该注释将选用guiceSide.xml配置文件里global-results节点进行解析跳转契约<br/>
 * 一般而言,除了异步ajax类型请求不需要定义@PageFlow
 * <p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ResultInterceptor
 * @see Result
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageFlow {
	/**
	 * 指定@result
	 * @return 返回@Result数组
	 */
	Result[] result();
}
