package org.guiceside.persistence.hibernate.dao.hquery;

import ognl.OgnlException;
import org.apache.commons.beanutils.PropertyUtils;
import org.guiceside.commons.Page;
import org.guiceside.commons.PageUtils;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.Persistent;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.guiceside.persistence.hibernate.dao.enums.SelectorType;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 16:34:16
 * To change this template use File | Settings | File Templates.
 */
public class HQueryCore extends HQuerySupport {


    /**
     * HQuery通过解析选择器 进行求值
     *
     * @param entityClass
     */
    
    protected void evaluation(Class entityClass, int firstResult, int maxResult) {
        if (getCurrentObject() != null) {
            return;
        }
        Object result = null;
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Selector selector : getCurrentSelectors()) {
            if(selector.getSelectorType().equals(SelectorType.ALIAS)){
                criteria.createAlias(selector.getAliasColumn(),selector.getAliasAs());
            }else if(selector.getSelectorType().equals(SelectorType.RESTRICTION)){
                criteria.add(selector.getCriterion());
            }else if(selector.getSelectorType().equals(SelectorType.PROJECTION)){
                criteria.setProjection(selector.getProjection());
            }else if(selector.getSelectorType().equals(SelectorType.ORDER)){
                criteria.addOrder(selector.getOrder());
            }
        }
        ReturnType returnType = getCurrentReturnType();
        if (returnType.equals(ReturnType.PLAIN)) {
            result = criteria.uniqueResult();
        } else if (returnType.equals(ReturnType.LIST)) {
            result = criteria.list();
        } else if (returnType.equals(ReturnType.ARRAY)) {
            List tempList = criteria.list();
            if (tempList != null && !tempList.isEmpty()) {
                result = tempList.toArray();
            }
        } else if (returnType.equals(ReturnType.PAGE)) {
            int totalRecord = totalRecord(criteria);
            if (firstResult >= totalRecord) {
                firstResult = firstResult - maxResult;
            }
            Page page = PageUtils.createPage(maxResult,
                    firstResult / maxResult + 1,
                    totalRecord);
            criteria.setFirstResult(page.getBeginIndex()).setMaxResults(
                    page.getEveryPage());
            page.setResultList(criteria.list());
            result=page;
        }
        setCurrentObject(result);
    }


    private int totalRecord(final Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) BeanUtils.forceGetProperty(impl, "orderEntries");
            BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {

        }
        int totalCount = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(projection);

        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            criteria.setResultTransformer(transformer);
        }
        try {
            BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
        }
        return totalCount;
    }


    /**
     * 进行持久化操作
     *
     * @param persistent
     */
    protected void invoke(Persistent persistent) {
        invokeAllCallBack();
        invokeBeforeCallBack();


        Object object = getCurrentObject();
        if (object == null) {
            return;
        }

        ReturnType returnType = getCurrentReturnType();
        switch (returnType) {
            case PLAIN:
                persistentPlain(persistent, object);
                break;
            case ARRAY:
                persistentArray(persistent, object);
                break;
            case LIST:
                persistentCollection(persistent, object);
                break;
        }
        invokeAllCallBack();
        invokeAfterCallBack();
    }

    private void persistentPlain(Persistent persistent, Object object) {
        switch (persistent) {
            case SAVE:
                getSession().save(object);
                break;
            case UPDATE:
                getSession().update(object);
                break;
            case SAVEORUPDATE:
                getSession().saveOrUpdate(object);
                break;
            case DELETE:
                getSession().delete(object);
                break;
        }
        setPersistentCurrentObject(persistent, object);
    }

    private void persistentArray(Persistent persistent, Object object) {
        Object[] objects = (Object[]) object;
        if (objects == null) {
            return;
        }
        for (Object o : objects) {
            switch (persistent) {
                case SAVE:
                    getSession().save(o);
                    break;
                case UPDATE:
                    getSession().update(o);
                    break;
                case SAVEORUPDATE:
                    getSession().saveOrUpdate(o);
                    break;
                case DELETE:
                    getSession().delete(o);
                    break;
            }
        }
        setPersistentCurrentObject(persistent, object);
    }

    private void persistentCollection(Persistent persistent, Object object) {
        Collection collection = (Collection) object;
        if (collection == null) {
            return;
        }
        for (Object o : collection) {
            switch (persistent) {
                case SAVE:
                    getSession().save(o);
                    break;
                case UPDATE:
                    getSession().update(o);
                    break;
                case SAVEORUPDATE:
                    getSession().saveOrUpdate(o);
                    break;
                case DELETE:
                    getSession().delete(o);
                    break;
            }
        }
        setPersistentCurrentObject(persistent, object);
    }

    private void setPersistentCurrentObject(Persistent persistent, Object object) {
        switch (persistent) {
            case SAVE:
                setCurrentObject(object);
                break;
            case UPDATE:
                setCurrentObject(object);
                break;
            case SAVEORUPDATE:
                setCurrentObject(object);
                break;
            case DELETE:
                setCurrentObject(null);
                break;
        }
    }


    


    public <T extends IdEntity> T get(Class<? extends IdEntity> entityClass) {
        setCurrentReturnType(ReturnType.PLAIN);
        evaluation(entityClass,-1,-1);
        Object value = getCurrentObject();
        clean();
        return (T) value;
    }


    /**
     * 
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T extends IdEntity> List<T> list(Class<? extends IdEntity> entityClass) {
        setCurrentReturnType(ReturnType.LIST);
        evaluation(entityClass,-1,-1);
        Object value = getCurrentObject();
        clean();
        return (List<T>) value;
    }

    /**
     * 
     * @param entityClass
     * @param type
     * @param <T>
     * @return
     */
   public <T> List<T> list(Class<? extends IdEntity> entityClass,Class<T> type) {
       setCurrentReturnType(ReturnType.LIST);
        evaluation(entityClass,-1,-1);
        Object value = getCurrentObject();
        clean();
        return (List<T>) value;
    }


    /**
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T extends IdEntity> T[] array(Class<? extends IdEntity> entityClass) {
        setCurrentReturnType(ReturnType.ARRAY);
        evaluation(entityClass,-1,-1);
        Object value = getCurrentObject();
        clean();
        return (T[]) value;
    }

    /**
     *
     * @param entityClass
     * @param firstResult
     * @param maxResults
     * @param <T>
     * @return
     */
    public <T extends IdEntity> Page<T> page(Class<? extends IdEntity> entityClass, int firstResult, int maxResults) {
        setCurrentReturnType(ReturnType.PAGE);
        evaluation(entityClass,firstResult,maxResults);
        Object value = getCurrentObject();
        clean();
        return (Page<T>) value;
    }

    /**
     *
     * @param entityClass
     * @param type
     * @param <T>
     * @return
     */
    public <T> T value(Class<? extends IdEntity> entityClass, Class<T> type) {
        setCurrentReturnType(ReturnType.PLAIN);
        evaluation(entityClass,-1,-1);
        Object value = getCurrentObject();
        clean();
        if(type.toString().equals(Integer.class.toString())){
            if(value.getClass().toString().equals(Long.class.toString())){
                value=((Number)value).intValue();
                return (T) value;
            }else{
                return (T) value;
            }
        }else{
            return (T) value;
        }
    }


    /**
     *
     * @param returnType
     * @return
     */
    public HQueryCore returnType(ReturnType returnType) {
        setCurrentReturnType(returnType);
        return this;
    }

    
    private void invokeBeforeCallBack() {
        HQueryCallBack hQueryCallBack = getCurrentHQueryCallBack();
        if (hQueryCallBack instanceof BeforeCallback) {
            hQueryCallBack.init(getHQueryContent());
            hQueryCallBack.invoke();
        }
    }

    private void invokeAfterCallBack() {
        HQueryCallBack hQueryCallBack = getCurrentHQueryCallBack();
        if (hQueryCallBack instanceof AfterCallback) {
            hQueryCallBack.init(getHQueryContent());
            hQueryCallBack.invoke();
        }
    }

    private void invokeAllCallBack() {
        HQueryCallBack hQueryCallBack = getCurrentHQueryCallBack();
        if (hQueryCallBack instanceof AllCallback) {
            hQueryCallBack.init(getHQueryContent());
            hQueryCallBack.invoke();
        }
    }

    protected Object invokeAttr(String column) {
        Object o = getCurrentObject();
        if (o == null) {
            return null;
        }
        Object value = getAttr(o, column);
        clean();
        return value;
    }

    protected void invokeAttr(String column, Object value) {
        Object o = getCurrentObject();
        if (o == null) {
            return;
        }
        setAttr(o, column, value);
    }

    private void setAttr(Object object, String column, Object value) {
        ReturnType returnType = getCurrentReturnType();
        if (returnType.equals(ReturnType.PLAIN)) {
            setValue(object, column, value);
        } else if (returnType.equals(ReturnType.LIST)) {
            List<?> eachList = (List<?>) object;
            if (eachList != null && !eachList.isEmpty()) {
                for (Object o : eachList) {
                    setValue(o, column, value);
                }
            }
        } else if (returnType.equals(ReturnType.ARRAY)) {
            Object[] eachObjects = (Object[]) object;
            if (eachObjects != null && eachObjects.length > 0) {
                for (Object o : eachObjects) {
                    setValue(o, column, value);
                }
            }
        }
    }

    private Object getAttr(Object object, String column) {
        ReturnType returnType = getCurrentReturnType();
        Class<?> propertyType = getPropertyType(object, column);

        Object value = null;
        if (returnType.equals(ReturnType.PLAIN)) {
            value = getValue(object, column);
            if (object == null) {
                return value;
            }
            value = BeanUtils.convertValue(value.toString(), propertyType);
        } else if (returnType.equals(ReturnType.LIST)) {
            List<?> eachList = (List<?>) object;
            if (eachList != null && !eachList.isEmpty()) {
                List<Object> values = new ArrayList<Object>();
                for (Object o : eachList) {
                    Object v = getValue(o, column);
                    if (v != null) {
                        v = BeanUtils.convertValue(v.toString(), propertyType);
                    }
                    values.add(v);
                }
                value = values;
            }
        } else if (returnType.equals(ReturnType.ARRAY)) {
            Object[] eachObjects = (Object[]) object;
            if (eachObjects != null && eachObjects.length > 0) {
                Object[] values = new Object[eachObjects.length];
                int i = 0;
                for (Object o : eachObjects) {
                    Object v = getValue(o, column);
                    if (v != null) {
                        v = BeanUtils.convertValue(v.toString(), propertyType);
                    }
                    values[i] = v;
                    i++;
                }
                value = values;
            }
        }
        return value;
    }

    private Class<?> getPropertyType(Object obj, String property)
            throws HQueryException {
        Class<?> propertyType = null;
        try {
            propertyType = PropertyUtils.getPropertyType(obj, property);
        } catch (Exception e) {
            throw new HQueryException("get object ["
                    + obj.getClass().getName() + "] Property+{" + property
                    + "} Type failed", e);
        }
        return propertyType;
    }

    private Object getValue(Object obj, String property)
            throws HQueryException {
        Object value = null;
        try {
            value = BeanUtils.getValue(obj, property);
        } catch (OgnlException e) {
            throw new HQueryException("get object ["
                    + obj.getClass().getName() + "] Property+{" + property
                    + "} Value failed", e);
        }
        return value;
    }

    private void setValue(Object obj, String property, Object value)
            throws HQueryException {
        try {
            BeanUtils.setValue(obj, property, value);
        } catch (OgnlException e) {
            throw new HQueryException("set object ["
                    + obj.getClass().getName() + "] Property+{" + property
                    + "} Value failed", e);
        }
    }
}
