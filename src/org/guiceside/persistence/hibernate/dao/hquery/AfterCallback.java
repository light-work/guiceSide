package org.guiceside.persistence.hibernate.dao.hquery;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 23:27:19
 * To change this template use File | Settings | File Templates.
 */
public abstract class AfterCallback extends AbstractHQueryCallback {
    public abstract void invoke();
}
