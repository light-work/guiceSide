package org.guiceside.persistence.entity.search;
/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-2
 *
 **/
public class AndModeUtils {

	public static AndMode setGroup(String group){
		return new AndMode(group);
	}
}
