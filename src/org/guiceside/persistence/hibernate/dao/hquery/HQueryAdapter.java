package org.guiceside.persistence.hibernate.dao.hquery;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-31
 * Time: 22:59:54
 * To change this template use File | Settings | File Templates.
 */
public interface HQueryAdapter {
    
    void invoke(Object o);
    
    void init(HQueryContent hQueryContent);
}

