package org.guiceside.persistence.hibernate.dao.hquery;

import ognl.OgnlException;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.hibernate.dao.enums.Persistent;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;

import java.io.Serializable;
import java.sql.Clob;

/**
 * 该查询只加载 不会进行查询查询操作
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 12:32:32
 * To change this template use File | Settings | File Templates.
 */
public class HQueryLoad extends HQueryCore {


    public HQueryLoad(HQueryContent hQueryContent) {
        setHQueryContent(hQueryContent);
    }

    /**
     * 从持久成删除对象
     */
    public void delete() {
        invoke(Persistent.DELETE);
        clean();
    }

    /**
     * 从持久层删除对象
     * 支持回调
     * @param hQueryCallBack
     */
    public void delete(HQueryCallBack hQueryCallBack) {
        setCurrentHQueryCallBack(hQueryCallBack);
        invoke(Persistent.DELETE);
        clean();
    }


    /**
     * 持久化对象
     */
    public void save() {
        invoke(Persistent.SAVEORUPDATE);
        clean();
    }

    /**
     * 持久化对象
     */
    public void add() {
        invoke(Persistent.SAVE);
        clean();
    }



    /**
     * 持久化对象
     * 支持回调
     * @param hQueryCallBack
     */
    public void save(HQueryCallBack hQueryCallBack) {
        setCurrentHQueryCallBack(hQueryCallBack);
        invoke(Persistent.SAVEORUPDATE);
        clean();
    }

   

    @Override
    public HQueryLoad returnType(ReturnType returnType) {
        super.returnType(returnType);    //To change body of overridden methods use File | Settings | File Templates.
        return getCurrentHQueryLoad();
    }

    /**
     * 遍历对象
     * 支持回调
     * @param eachAdapter
     * @return
     */
    public HQueryLoad each(EachAdapter eachAdapter) {
        eachAdapter.init(getHQueryContent());
        eachAdapter.each();
        return getCurrentHQueryLoad();
    }

    /**
     * 从持久层获取指定某一column值
     * @param column
     * @return
     */
    public Object attr(String column) {
        return invokeAttr(column);
    }

    /**
     * 从持久层设置指定某一column值
     * @param column
     * @param value
     * @return
     */
    public HQueryLoad attr(String column, Object value) {
        invokeAttr(column, value);
        return getCurrentHQueryLoad();
    }


}
