package org.guiceside.guice.module;

import org.guiceside.guice.strategy.AbstractActionPackageStrategy;





/**
 * <p>
 * 添加需要装载的actionPackage
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see AbstractActionPackageStrategy
 */
public class ActionPackageModule extends AbstractActionPackageStrategy {

	public void addActionPackages(String packages) {
		actionPackeages.add(packages);
	}
}
