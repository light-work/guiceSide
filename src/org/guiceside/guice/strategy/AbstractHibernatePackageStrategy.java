package org.guiceside.guice.strategy;

import com.google.inject.AbstractModule;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.guiceside.commons.lang.ClassUtils;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Hibernate默认Module
 * 继承AbstractModule实现了configure()<br/>
 * 为Configuration提供对象
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public abstract class AbstractHibernatePackageStrategy extends AbstractModule implements PackageStrategy{

	protected Set<String> hibernatePackages=new HashSet<String>();
	
	private static final Logger log =Logger.getLogger(AbstractHibernatePackageStrategy.class);
	private Set<Class<?>> classes;
	
	
	public Configuration getConfiguration() {
		addHibernateClasses();
		AnnotationConfiguration configuration=new AnnotationConfiguration();
		configuration=configuration.configure();
		if(classes!=null&&!classes.isEmpty()){
			for (Class<?> cls : classes) {
				if (cls.isAnnotationPresent(Entity.class)) {
					configuration.addAnnotatedClass(cls);
					if(log.isDebugEnabled()){
						log.debug("addAnnotatedClass ["+cls.getSimpleName()+"] successful");
					}
				}
			}
		}
		return configuration;
	}

	@Override
	protected void configure() {
		binder().bind(Configuration.class).toInstance(getConfiguration());
	}
	
	private void addHibernateClasses() {
		if (hibernatePackages != null && !hibernatePackages.isEmpty()) {
			classes= new HashSet<Class<?>>();
			for (String packages : hibernatePackages) {
				if (StringUtils.isNotBlank(packages)) {
					classes.addAll(ClassUtils.getClasses(packages));
				}
			}
		}
	}
	

}
