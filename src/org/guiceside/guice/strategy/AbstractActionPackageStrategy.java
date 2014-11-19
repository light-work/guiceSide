package org.guiceside.guice.strategy;

import com.google.inject.AbstractModule;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.guiceside.commons.lang.ClassUtils;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.dispatcher.mapper.ActionResourceException;
import org.guiceside.web.dispatcher.mapper.ActionResourceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 将提供的actionPackages下的所有符合Action规范的class<br/>
 * 移交给ActionResourceManager
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-28
 * 
 */
public abstract class AbstractActionPackageStrategy extends AbstractModule
		implements PackageStrategy {
	private static final Logger log = Logger.getLogger(AbstractActionPackageStrategy.class);

	private Set<Class<?>> classes =null;

	protected Set<String> actionPackeages=new HashSet<String>();
	
	@Override
	protected void configure() {
		addActionClasses();
		if(classes!=null&&!classes.isEmpty()){
			for (Class<?> cls : classes) {

				if (cls.isAnnotationPresent(Action.class)) {

					Action gsAction = cls.getAnnotation(Action.class);
					try {
						ActionResourceManager.put(gsAction.namespace(),
								gsAction.name(), cls);
						if(log.isDebugEnabled()){
							log.debug("addActionResource ["+gsAction.namespace()+"/"+gsAction.name()+"] mapping to "+cls.getName());
						}
					} catch (ActionResourceException e) {
						log.error("There was an error in the load ActionMapping", e.getCause());
					}
				}
			}
		}
	}
	
	private void addActionClasses() {
		if (actionPackeages != null && !actionPackeages.isEmpty()) {
			classes= new HashSet<Class<?>>();
			for (String packages : actionPackeages) {
				if (StringUtils.isNotBlank(packages)) {
					classes.addAll(ClassUtils.getClasses(packages));
				}
			}
		}
	}
}
