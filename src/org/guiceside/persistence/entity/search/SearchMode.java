package org.guiceside.persistence.entity.search;

import org.guiceside.persistence.hibernate.dao.enums.Condition;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.enums.Relation;

import java.io.Serializable;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-2
 * 
 **/
public class SearchMode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Condition condition =Condition.EQ;
	
	private String column;
	
	private Object value;
	
	private Class<?> type;
	
	private boolean ignoreCase = false;
	
	private boolean not = false;
	
	private boolean ignoreBank = false;
	
	private Relation relation = Relation.NULL;

	private OrMode orMode;
	
	private AndMode andMode;
	
	private AliasMode aliasMode;
	
	private Match match=Match.ANYWHERE;
	
	@SuppressWarnings("unused")
	private SearchMode(){
		
	}
	public SearchMode(String column){
		this.setColumn(column);
	}
	
	public SearchMode(AliasMode aliasMode){
		this.setAliasMode(aliasMode);
	}
	
	public Condition getCondition() {
		return condition;
	}

	public SearchMode setCondition(Condition condition) {
		this.condition = condition;
		return this;
	}

	public String getColumn() {
		return column;
	}

	public SearchMode setColumn(String column) {
		this.column = column;
		return this;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public SearchMode setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	public boolean isNot() {
		return not;
	}

	public SearchMode setNot(boolean not) {
		this.not = not;
		return this;
	}

	public boolean isIgnoreBank() {
		return ignoreBank;
	}

	public SearchMode setIgnoreBank(boolean ignoreBank) {
		this.ignoreBank = ignoreBank;
		return this;
	}

	public Relation getRelation() {
		return relation;
	}

	public SearchMode setRelation(Relation relation) {
		this.relation = relation;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public SearchMode setValue(Object value) {
		this.value = value;
		return this;
	}

	public Class<?> getType() {
		return type;
	}

	public SearchMode setType(Class<?> type) {
		this.type = type;
		return this;
	}

	public OrMode getOrMode() {
		return orMode;
	}

	public SearchMode setOrMode(OrMode orMode) {
		this.orMode = orMode;
		return this;
	}

	public AndMode getAndMode() {
		return andMode;
	}

	public SearchMode setAndMode(AndMode andMode) {
		this.andMode = andMode;
		return this;
	}

	public Match getMatch() {
		return match;
	}

	public SearchMode setMatch(Match match) {
		this.match = match;
		return this;
	}
	public AliasMode getAliasMode() {
		return aliasMode;
	}
	public SearchMode setAliasMode(AliasMode aliasMode) {
		this.aliasMode = aliasMode;
		return this;
	}

}
