package org.guiceside.persistence.entity.search;

import org.guiceside.persistence.hibernate.dao.enums.Relation;

import java.io.Serializable;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-2
 *
 **/
public class OrMode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String group;
	
	private String joinGroup="";
	
	private Relation joinRelation=Relation.NULL;

	@SuppressWarnings("unused")
	private OrMode(){
		
	}
	
	public OrMode(String group){
		setGroup(group);
	}
	public String getGroup() {
		return group;
	}

	public OrMode setGroup(String group) {
		this.group = group;
		return this;
	}

	public String getJoinGroup() {
		return joinGroup;
	}

	public OrMode setJoinGroup(String joinGroup) {
		this.joinGroup = joinGroup;
		return this;
	}

	public Relation getJoinRelation() {
		return joinRelation;
	}

	public OrMode setJoinRelation(Relation joinRelation) {
		this.joinRelation = joinRelation;
		return this;
	}
}
