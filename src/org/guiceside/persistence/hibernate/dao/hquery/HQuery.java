package org.guiceside.persistence.hibernate.dao.hquery;


import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 22:25:01
 * To change this template use File | Settings | File Templates.
 */
public class HQuery extends HQuerySelector {

    protected HQueryGet $(Serializable id) {
        //因为Selector也属于Serializable 所以进行方法跳转
        if (id instanceof Selector) {
            Selector idSelector = (Selector) id;
            Selector[] newSelectors = new Selector[1];
            newSelectors[0] = idSelector;
            return $(newSelectors);
        }
        setHQueryContent(new HQueryContent(null, null, new ArrayList<Selector>(), null, ReturnType.PLAIN));
        setCurrentHQueryGet(new HQueryGet(getHQueryContent()));
        getCurrentHQueryGet().setCurrentHQueryGet(getCurrentHQueryGet());
        if (id != null) {
            $core($id(id));
        }
        return getCurrentHQueryGet();
    }


    protected HQueryGet $(Serializable id, Selector... selectors) {
        //因为Selector也属于Serializable 所以进行方法跳转
        if (id instanceof Selector) {
            Selector idSelector = (Selector) id;
            Selector[] newSelectors = new Selector[selectors.length + 1];
            newSelectors[0] = idSelector;
            int i = 1;
            for (Selector selector : selectors) {
                newSelectors[i] = selector;
                i++;
            }
            return $(newSelectors);
        }
        setHQueryContent(new HQueryContent(null, null, new ArrayList<Selector>(), null, ReturnType.PLAIN));
        setCurrentHQueryGet(new HQueryGet(getHQueryContent()));
        getCurrentHQueryGet().setCurrentHQueryGet(getCurrentHQueryGet());
        if (id != null) {
            $core($id(id));
        }
        if (selectors != null) {
            for (Selector selector : selectors) {
                $core(selector);
            }
        }
        return getCurrentHQueryGet();
    }

    protected HQueryGet $(List<Selector> selectorList,String matching,String... indexFields) {
        setHQueryContent(new HQueryContent(null, null, new ArrayList<Selector>(), matching,null, ReturnType.LIST,indexFields));
        setCurrentHQueryGet(new HQueryGet(getHQueryContent()));
        getCurrentHQueryGet().setCurrentHQueryGet(getCurrentHQueryGet());
        if (selectorList != null) {
            for (Selector selector : selectorList) {
                $core(selector);
            }
        }
        return getCurrentHQueryGet();
    }

    protected HQueryGet $(List<Selector> selectorList) {
        setHQueryContent(new HQueryContent(null, null, new ArrayList<Selector>(), null, ReturnType.LIST));
        setCurrentHQueryGet(new HQueryGet(getHQueryContent()));
        getCurrentHQueryGet().setCurrentHQueryGet(getCurrentHQueryGet());
        if (selectorList != null) {
            for (Selector selector : selectorList) {
                $core(selector);
            }
        }
        return getCurrentHQueryGet();
    }

    protected HQueryGet $(Selector... selectors) {
        setHQueryContent(new HQueryContent(null, null, new ArrayList<Selector>(), null, ReturnType.LIST));
        setCurrentHQueryGet(new HQueryGet(getHQueryContent()));
        getCurrentHQueryGet().setCurrentHQueryGet(getCurrentHQueryGet());
        if (selectors != null) {
            for (Selector selector : selectors) {
                $core(selector);
            }
        }
        return getCurrentHQueryGet();
    }

    protected HQueryLoad $(IdEntity o) {
        setHQueryContent(new HQueryContent(o, null, null, null, ReturnType.PLAIN));
        setCurrentHQueryLoad(new HQueryLoad(getHQueryContent()));
        getCurrentHQueryLoad().setCurrentHQueryLoad(getCurrentHQueryLoad());
        return getCurrentHQueryLoad();
    }

    protected HQueryLoad $(Collection<? extends IdEntity> o) {
        setHQueryContent(new HQueryContent(o, null, null, null, ReturnType.LIST));
        setCurrentHQueryLoad(new HQueryLoad(getHQueryContent()));
        getCurrentHQueryLoad().setCurrentHQueryLoad(getCurrentHQueryLoad());
        return getCurrentHQueryLoad();
    }

    protected HQuerySql $(String sql) {
        setHQueryContent(new HQueryContent(null, null, null, sql, ReturnType.LIST));
        setCurrentHQuerySql(new HQuerySql(getHQueryContent()));
        getCurrentHQuerySql().setCurrentHQuerySql(getCurrentHQuerySql());
        return getCurrentHQuerySql();
    }

    private void $core(Selector selector) {
        if (selector != null) {
            List<Selector> selectors = getCurrentSelectors();
            selectors.add(selector);
        }
    }
}
