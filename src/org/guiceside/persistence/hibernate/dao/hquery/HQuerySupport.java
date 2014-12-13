package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.commons.Assert;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 16:25:45
 * To change this template use File | Settings | File Templates.
 */
public class HQuerySupport {

    private SessionFactory sessionFactory;

    private HQueryContent hQueryContent;

    public static final ThreadLocal tLocalsess = new ThreadLocal();
    
    public HQuerySupport() {
        sessionFactory = SessionFactoryHolder.getCurrentSessionFactory();
    }

    /**
     * @return 返回当前session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * @param clazz
     * @return 返回PO的ID属性名称
     */
    protected String getIdName(Class clazz) {
        Assert.notNull(clazz);
        ClassMetadata meta = sessionFactory.getClassMetadata(clazz);
        return meta.getIdentifierPropertyName();
	}

    /**
     * 返回HQuery上下文
     * @return
     */
    protected HQueryContent getHQueryContent() {
        hQueryContent=(HQueryContent)tLocalsess.get();
        if(hQueryContent==null){
            throw new HQueryException("");
        }
        return hQueryContent;
    }

    /**
     * 设置HQuery上下文
     * @param hQueryContent
     */
    protected void setHQueryContent(HQueryContent hQueryContent) {
        tLocalsess.set(hQueryContent);
        this.hQueryContent = hQueryContent;
    }

    /**
     * 返回当前操作HQUERY上下文的ID
     * @return
     */
    protected Serializable getCurrentId(){
        return getHQueryContent().getId();
    }

    /**
     * 设置Hquery上下文的ID
     * @param currentId
     */
    protected void setCurrentId(Serializable currentId){
        getHQueryContent().setId(currentId);
    }

    /**
     * 返回当前Hquery上下文的对象
     * @return
     */
    protected Object getCurrentObject(){
        return getHQueryContent().getObject();
    }

    /**
     * 设置Hquery上下文的对象
     * @param currentObject
     */
    protected void setCurrentObject(Object currentObject){
        getHQueryContent().setObject(currentObject);
    }

    /**
     * 返回当前Hquery上下文的GET对象
     * @return
     */
    protected HQueryGet getCurrentHQueryGet(){
        return getHQueryContent().getHQueryGet();
    }

    /**
     * 设置Hquery上下文的Get对象
     * @param currentHQueryGet
     */
    protected void setCurrentHQueryGet(HQueryGet currentHQueryGet){
        getHQueryContent().setHQueryGet(currentHQueryGet);
    }

    /**
     * 返回当前Hquery上下文的Load对象
     * @return
     */
    protected HQueryLoad getCurrentHQueryLoad(){
        return getHQueryContent().getHQueryLoad();
    }

    /**
     * 设置Hquery上下文的Load对象
     * @param currentHQueryLoad
     */
    protected void setCurrentHQueryLoad(HQueryLoad currentHQueryLoad){
        getHQueryContent().setHQueryLoad(currentHQueryLoad);
    }

    /**
     * 返回当前Hquery上下文SQL对象
     * @return
     */
    protected HQuerySql getCurrentHQuerySql(){
        return getHQueryContent().getHQuerySql();
    }

    /**
     * 设置Hquery上下文的SQL对象
     * @param currentHQuerySql
     */
    protected void setCurrentHQuerySql(HQuerySql currentHQuerySql){
        getHQueryContent().setHQuerySql(currentHQuerySql);
    }

    /**
     * 返回当前上下文的SQL
     * @return
     */
    protected String getCurrentSql(){
        return getHQueryContent().getSql();
    }

    /**
     * 设置当前上下文的SQL
     * @param sql
     */
    protected void setCurrentSql(String sql){
        getHQueryContent().setSql(sql);
    }

    /**
     * 返回当前Hquery上下文上下文的Selector对象
     * @return
     */
    protected List<Selector> getCurrentSelectors(){
        return getHQueryContent().getSelectors();
    }

    /**
     * 设置Hquery上下文的Selector对象
     * @param currentSelectors
     */
    protected void setCurrentSelectors (List<Selector> currentSelectors){
        getHQueryContent().setSelectors(currentSelectors);
    }

    /**
     * 返回当前Hquery上下文上下文的IndexFields对象
     * @return
     */
    protected String[] getCurrentIndexFields(){
        return getHQueryContent().getIndexFields();
    }

    /**
     * 设置Hquery上下文的Selector对象
     * @param indexFields
     */
    protected void setCurrentIndexFields (String[] indexFields){
        getHQueryContent().setIndexFields(indexFields);
    }

    /**
     * 返回当前Hquery上下文上下文的IndexFields对象
     * @return
     */
    protected String getCurrentMatching(){
        return getHQueryContent().getMatching();
    }

    /**
     * 设置Hquery上下文的Selector对象
     * @param matching
     */
    protected void setCurrentMatching (String matching){
        getHQueryContent().setMatching(matching);
    }


    /**
     * 返回当前Hquery上下文的ReturnType对象
     * @return
     */
    protected ReturnType getCurrentReturnType(){
        return getHQueryContent().getReturnType();
    }

    /**
     * 设置当前Hquery上下文的ReturnType对象
     * @param currentReturnType
     */
    protected void setCurrentReturnType(ReturnType currentReturnType){
        getHQueryContent().setReturnType(currentReturnType);
    }

    /**
     * 返回当前Hquery上下文的HqueryCallBack对象
     * @return
     */
    protected HQueryCallBack getCurrentHQueryCallBack(){
        return getHQueryContent().getHQueryCallBack();
    }

    /**
     * 设置Hquery上下文的HqueryCallBack对象
     * @param currentHQueryCallBack
     */
    protected void setCurrentHQueryCallBack(HQueryCallBack currentHQueryCallBack){
        getHQueryContent().setHQueryCallBack(currentHQueryCallBack);
    }

    /**
     * 清理当前Hquery上下文
     */
    protected void clean() {
        tLocalsess.remove();
//        getHQueryContent().clean();
//        setHQueryContent(null);
    }
    
}
