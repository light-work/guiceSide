package org.guiceside.persistence.entity.search;
/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-2
 *
 **/
public  class SearchModeUtils {
	public static SearchMode setColumn(String column){
		return new SearchMode(column);
	}
	public static SearchMode setAliasMode(AliasMode aliasMode){
		
		return new SearchMode(aliasMode);
	}
}
