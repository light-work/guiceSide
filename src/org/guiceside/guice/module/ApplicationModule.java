package org.guiceside.guice.module;


import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import org.apache.log4j.Logger;
import org.guiceside.config.Configuration;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.guice.strategy.PackageStrategy;
import org.guiceside.persistence.InitializerPersistence;
import org.guiceside.persistence.PersistenceFlavor;
import org.guiceside.persistence.PersistenceService;
import org.guiceside.persistence.hibernate.dao.interceptor.*;
import org.guiceside.web.context.module.RequestDataModule;
import org.guiceside.web.context.module.ServletContextModule;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * GuiceSide实例Injector唯一Module<br/>
 * 所有配置在guiceSide.xml里的Module Action<br/>
 * Interceptor都在这里统一构造<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class ApplicationModule implements Module {

	private static final  Logger log = Logger.getLogger(ApplicationModule.class);

	private Configuration configuration;

	/**
	 * 
	 * 构造方法接收guiceSide.xml封装的Configuration
	 * 
	 * @param configuration
	 */
	public ApplicationModule(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * 
	 * 通过guiceSide.xml封装的Configuration加载各Module模块
	 * 
	 * @param binder
	 */
	public void configure(Binder binder) {
		boolean enablePersistence = false;
		PersistenceFlavor flavor = configuration.getPersistenceFlavor();
		if (flavor != null) {
			binder.install(PersistenceService.enable(flavor));
			if (log.isDebugEnabled()) {
				log.debug("Install " + flavor + " Module Successful");
			}
			enablePersistence = true;
		}
		
		binder.install(new ServletModule());
		binder.install(new ServletContextModule());
        binder.install(new RequestDataModule());
		log.debug("HibernatePackeages " + configuration.getHibernatePackeages());
		builderPackages(binder,configuration.getHibernatePackeages(),new HibernateModule());
		
		
		List<AbstractInterceptorStrategy> interceptors=configuration.getInterceptors();
		if(interceptors!=null&&!interceptors.isEmpty()){
			for(AbstractInterceptorStrategy iObj:interceptors){
				binder.install(iObj);
				if (log.isDebugEnabled()) {
					log.debug("Install " + iObj.getClass().getSimpleName() + " Module Successful");
				}
			}
		}

		binder.install(new HibernateLocalTxnModule());
		if (log.isDebugEnabled()) {
			log.debug("Install HibernateLocalTxnModule Module Successful");
		}

//		binder.install(new FinderByIdInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install FinderByIdInterceptorModule Module Successful");
//		}
//		binder.install(new SaveInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install SaveInterceptorModule Module Successful");
//		}
//		binder.install(new UpdateInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install UpdateInterceptorModule Module Successful");
//		}
//		binder.install(new SaveOrUpdateInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install SaveOrUpdateInterceptorModule Module Successful");
//		}
//		binder.install(new DeleteInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install DeleteInterceptorModule Module Successful");
//		}
//		binder.install(new FinderByCriteriaInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install FinderByCriteriaInterceptorModule Module Successful");
//		}
//		binder.install(new FinderByHqlInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install FinderByHqlInterceptorModule Module Successful");
//		}
//		binder.install(new FinderBySqlInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install FinderBySqlInterceptorModule Module Successful");
//		}
		
//		binder.install(new QueryByHqlInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install QueryByHqlInterceptorModule Module Successful");
//		}
		
//		binder.install(new QueryBySqlInterceptorModule());
//		if (log.isDebugEnabled()) {
//			log.debug("Install QueryBySqlInterceptorModule Module Successful");
//		}

        binder.install(new ThreadSafeInterceptorModule());
        if (log.isDebugEnabled()) {
			log.debug("Install ThreadSafeInterceptorModule Module Successful");
		}
		if (enablePersistence) {
			binder.bind(InitializerPersistence.class).asEagerSingleton();
			if (log.isDebugEnabled()) {
				log.debug("Initialization" + flavor + " StartUp");
			}
		}
		builderPackages(binder, configuration.getActionPackeages(), new ActionPackageModule());
	}
	private void builderPackages(Binder binder,Set<String> packagesArr,PackageStrategy packageStrategy){
		if (packagesArr != null&&!packagesArr.isEmpty()) {
			for (String packages : packagesArr) {
				log.debug("packages " + packages);
				packageStrategy.addActionPackages(packages);
			}
			binder.install((Module) packageStrategy);
		}
	}
}
