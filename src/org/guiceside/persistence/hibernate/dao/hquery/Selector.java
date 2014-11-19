package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.hibernate.dao.enums.Relation;
import org.guiceside.persistence.hibernate.dao.enums.SelectorType;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-29
 * Time: 14:20:48
 * To change this template use File | Settings | File Templates.
 */
public class Selector implements Serializable{
    private Criterion criterion;
    /**
     * 连接方法
     */
    private Relation relation= Relation.NULL;


    private SelectorType selectorType= SelectorType.RESTRICTION;

    private String aliasColumn;

    private String aliasAs;

    private Projection projection;

    private Order order;

    /**
     * 返回对象之间的连接方法
     * @return
     */
    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }


    /**
     * 返回Criterion
     * @return
     */
    public Criterion getCriterion() {
        return criterion;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    /**
     * 返回SelectorType
     * @return
     */
    public SelectorType getSelectorType() {
        return selectorType;
    }

    public void setSelectorType(SelectorType selectorType) {
        this.selectorType = selectorType;
    }

    /**
     * 返回别名列
     * @return
     */
    public String getAliasColumn() {
        return aliasColumn;
    }

    public void setAliasColumn(String aliasColumn) {
        this.aliasColumn = aliasColumn;
    }

    /**
     * 返回别名
     * @return
     */
    public String getAliasAs() {
        return aliasAs;
    }

    public void setAliasAs(String aliasAs) {
        this.aliasAs = aliasAs;
    }

    /**
     * 返回投影
     * @return
     */
    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    /**
     * 返回排序
     * @return
     */
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
