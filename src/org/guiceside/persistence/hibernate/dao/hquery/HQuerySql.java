package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.SQLQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 会进行查询
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 12:32:48
 * To change this template use File | Settings | File Templates.
 */
public class HQuerySql extends HQueryCore {


    public HQuerySql(HQueryContent hQueryContent) {
        setHQueryContent(hQueryContent);
    }


    /**
     * 通过SQL加载指定entityClass数据 不建议使用
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T extends IdEntity> List<T> list(Class<? extends IdEntity> entityClass) {
        setCurrentReturnType(ReturnType.LIST);
        SQLQuery query = null;
        query = getSession()
                .createSQLQuery(getCurrentSql());
        query.addEntity(entityClass.getName().toLowerCase(), entityClass);
        Iterator iterator = query.list().iterator();
        if (iterator != null) {
            List<Object> idEntityList = new ArrayList<Object>();
            Object object = null;
            while (iterator.hasNext()) {
                object = iterator.next();
                idEntityList.add(object);
            }
            setCurrentObject(idEntityList);
        }

        Object value = getCurrentObject();
        clean();
        return (List<T>) value;
    }


}