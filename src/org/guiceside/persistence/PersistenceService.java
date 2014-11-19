package org.guiceside.persistence;

/**
 * <p>
 * 持久化服务对像,提供start()方法初始化第三方中间件
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
public abstract class PersistenceService {

	public abstract void start();

	/**
	 * 根据PersistenceFlavor初始化第三方组件
	 * @param persistenceFlavor
	 * @return PersistenceModule
	 */
	public static PersistenceModule enable(
			PersistenceFlavor persistenceFlavor) {
		return new PersistenceModule(
				persistenceFlavor);
	}
}
