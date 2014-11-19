package org.guiceside.persistence.hibernate.dao.hquery;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-31
 * Time: 23:01:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractHQueryAdapter extends HQuerySupport implements HQueryAdapter {
    public Object t;

    protected void loadCurrentObject(){
        t=getCurrentObject();
    }
    
    public void init(HQueryContent hQueryContent) {
        setHQueryContent(hQueryContent);
    }

}
