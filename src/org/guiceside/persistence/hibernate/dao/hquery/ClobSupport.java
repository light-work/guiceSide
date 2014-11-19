package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.entity.IdEntity;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-17
 * Time: 10:25:41
 * To change this template use File | Settings | File Templates.
 */
public class ClobSupport implements Serializable{
    private Class<? extends IdEntity> entityClass;
    private Serializable id;
    private String[] clobPropertys;
    private String[] clobValues;
}
