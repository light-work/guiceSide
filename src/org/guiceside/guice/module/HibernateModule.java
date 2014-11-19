package org.guiceside.guice.module;



import org.guiceside.guice.strategy.AbstractHibernatePackageStrategy;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-2
 *
 **/
public class HibernateModule extends AbstractHibernatePackageStrategy{


	public void addActionPackages(String packages) {
		hibernatePackages.add(packages);
	}

}
