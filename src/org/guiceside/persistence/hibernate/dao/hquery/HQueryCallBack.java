package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.entity.IdEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-31
 * Time: 14:14:36
 * To change this template use File | Settings | File Templates.
 */
public interface HQueryCallBack {

    void invoke();

    <T extends IdEntity> T get(Class<? extends IdEntity> entityClass);

    <T extends IdEntity> List<T> list(Class<? extends IdEntity> entityClass);

    <T extends IdEntity> T[] array(Class<? extends IdEntity> entityClass);

    void init(HQueryContent hQueryContent);
}
