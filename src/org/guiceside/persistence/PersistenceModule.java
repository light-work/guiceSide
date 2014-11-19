package org.guiceside.persistence;

import com.google.inject.AbstractModule;
import org.guiceside.persistence.hibernate.HibernateBindingSupport;

/**
 * <P>
 * 根据PersistenceFlavor进行相应组件bind
 * </P>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class PersistenceModule extends AbstractModule {

	private final PersistenceFlavor persistenceFlavor;

	public PersistenceModule(PersistenceFlavor persistenceFlavor) {
		// 接收persistenceFlavor
		this.persistenceFlavor = persistenceFlavor;

	}

	@Override
	protected void configure() {

		switch (persistenceFlavor) {
		case HIBERNATE:
			HibernateBindingSupport.addBindings(binder());
			break;
		}

	}

}
