package org.guiceside.persistence;

import com.google.inject.Inject;



/**
 User:zhenjiawang(zhenjiaWang@gmail.com)
 Date:2008-7-23
 Time:下午05:01:24
 Email:(zhenjiaWang@gmail.com) QQ:(119582291)
 <p>
 初始化持久层第三方组件
 </p>
 **/
public class InitializerPersistence {
	private final PersistenceService persistenceService;
	
	@Inject
	public InitializerPersistence(PersistenceService persistenceService){
		this.persistenceService=persistenceService;
		this.persistenceService.start();
       
	}
	
}
