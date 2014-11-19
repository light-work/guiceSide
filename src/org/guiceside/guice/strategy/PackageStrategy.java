package org.guiceside.guice.strategy;
/**
 * 
 * <p>
 * 通过package扫描class接口策略
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-28
 * 
 **/
public interface PackageStrategy {
	public void addActionPackages(String packages);
}
