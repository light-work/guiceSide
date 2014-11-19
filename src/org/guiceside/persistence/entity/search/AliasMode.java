package org.guiceside.persistence.entity.search;

import java.io.Serializable;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-2
 *
 **/
public class AliasMode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String column;
	
	private String alias;

	@SuppressWarnings("unused")
	private AliasMode(){
		
	}
	
	public AliasMode(String column){
		setColumn(column);
	}
	public String getColumn() {
		return column;
	}

	public AliasMode setColumn(String column) {
		this.column = column;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public AliasMode setAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
