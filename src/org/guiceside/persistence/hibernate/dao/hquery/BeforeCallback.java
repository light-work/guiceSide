package org.guiceside.persistence.hibernate.dao.hquery;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 23:33:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class BeforeCallback extends AbstractHQueryCallback{
    public abstract void invoke();
}
