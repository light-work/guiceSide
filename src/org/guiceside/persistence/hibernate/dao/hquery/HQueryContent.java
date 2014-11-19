package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.hibernate.dao.enums.ReturnType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-31
 * Time: 22:11:29
 * To change this template use File | Settings | File Templates.
 */
public class HQueryContent implements Serializable {

    private Object object;

    private List<Selector> selectors;

    private Serializable id;
    
    private ReturnType returnType;

    private HQueryLoad hQueryLoad;

    private HQueryGet hQueryGet;

    private HQueryCallBack hQueryCallBack;

    private HQuerySql hQuerySql;

    private String sql;

    public HQueryContent(Object object, Serializable id,List<Selector> selectors,String sql, ReturnType returnType) {
        this.object = object;
        this.id=id;
        this.selectors = selectors;
        this.returnType = returnType;
        this.sql=sql;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<Selector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<Selector> selectors) {
        this.selectors = selectors;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public HQueryLoad getHQueryLoad() {
        return hQueryLoad;
    }

    public void setHQueryLoad(HQueryLoad hQueryLoad) {
        this.hQueryLoad = hQueryLoad;
    }

    public HQueryGet getHQueryGet() {
        return hQueryGet;
    }

    public void setHQueryGet(HQueryGet hQueryGet) {
        this.hQueryGet = hQueryGet;
    }

    public HQueryCallBack getHQueryCallBack() {
        return hQueryCallBack;
    }

    public void setHQueryCallBack(HQueryCallBack hQueryCallBack) {
        this.hQueryCallBack = hQueryCallBack;
    }



    public HQuerySql getHQuerySql() {
        return hQuerySql;
    }

    public void setHQuerySql(HQuerySql hQuerySql) {
        this.hQuerySql = hQuerySql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
    public void clean() {
        this.object = null;
        this.selectors = null;
        this.returnType = null;
        this.hQueryLoad = null;
        this.hQueryGet = null;
        this.hQueryCallBack = null;
        this.hQuerySql=null;
        this.sql=null;
    }
}
