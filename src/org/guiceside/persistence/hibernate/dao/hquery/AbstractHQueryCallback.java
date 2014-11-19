package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.entity.IdEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-31
 * Time: 15:30:14
 * To change this template use File | Settings | File Templates.
 */
public abstract  class AbstractHQueryCallback extends HQuerySupport implements HQueryCallBack{
    public Object t;


    private void loadCurrentObject(){
        t=getCurrentObject();
    }
    public <T extends IdEntity> T get(Class<? extends IdEntity> entityClass) {
        loadCurrentObject();
        if(t==null)
        return null;
        return (T)t;
    }

    public <T extends IdEntity> List<T> list(Class<? extends IdEntity> entityClass) {
        loadCurrentObject();
        if(t==null)
        return null;
        return (List<T>)t;
    }

    public <T extends IdEntity> T[] array(Class<? extends IdEntity> entityClass) {
        loadCurrentObject();
        if(t==null)
        return null;
        return (T[])t;
    }

    public void init(HQueryContent hQueryContent) {
        setHQueryContent(hQueryContent);
    }

}
