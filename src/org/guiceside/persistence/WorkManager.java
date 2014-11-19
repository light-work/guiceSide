package org.guiceside.persistence;

/**
 * <p>
 * 管理持久会话接口
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public interface WorkManager {
	void beginWork();

	void endWork();
}
