package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.Persistent;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;

/**
 * 会进行查询
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 12:32:48
 * To change this template use File | Settings | File Templates.
 */
public class HQueryGet extends HQueryCore {

    
    public HQueryGet(HQueryContent hQueryContent){
        setHQueryContent(hQueryContent);
    }

    /**
     * 通过enetityClass类别从持久层删除对象
     * @param entityClass
     */
    public void delete(Class<? extends IdEntity> entityClass) {
        evaluation(entityClass,-1,-1);
        invoke(Persistent.DELETE);
        clean();
    }
    /**
     * 通过enetityClass类别从持久层删除对象 支持回调方法
     * @param entityClass
     */
    public void delete(Class<? extends IdEntity> entityClass,HQueryCallBack hQueryCallBack) {
        evaluation(entityClass,-1,-1);
        setCurrentHQueryCallBack(hQueryCallBack);
        invoke(Persistent.DELETE);
        clean();
    }
    
    

    @Override
    /**
     * 设置当前returnType
     */
    public HQueryGet returnType(ReturnType returnType) {
        super.returnType(returnType);    //To change body of overridden methods use File | Settings | File Templates.
        return getCurrentHQueryGet();
    }

    /**
     * 通过enetityClass类别从持久层遍历对象
     * 支持遍历回调
     * @param entityClass
     * @param eachAdapter
     * @return
     */
    public HQueryLoad each(Class<? extends IdEntity> entityClass,EachAdapter eachAdapter) {
        evaluation(entityClass,-1,-1);
        eachAdapter.init(getHQueryContent());
        eachAdapter.each();    //To change body of overridden methods use File | Settings | File Templates.
        setCurrentHQueryLoad(new HQueryLoad(getHQueryContent()));//构造新的HQueryLoad
        return getCurrentHQueryLoad();
    }

    /**
     * 通过enetityClass类别从持久层获取指定某一column值
     * @param entityClass
     * @param column
     * @return
     */
    public Object attr(Class<? extends IdEntity> entityClass,String column){
        evaluation(entityClass,-1,-1);
        return invokeAttr(column);
    }
    /**
     * 通过enetityClass类别从持久层设置指定某一column值
     * @param entityClass
     * @param column
     * @return
     */
    public HQueryLoad attr(Class<? extends IdEntity> entityClass,String column,Object value){
        evaluation(entityClass,-1,-1);
        invokeAttr(column,value);
        setCurrentHQueryLoad(new HQueryLoad(getHQueryContent()));//构造新的HQueryLoad
        return getCurrentHQueryLoad();
    }

}
