package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.persistence.hibernate.dao.enums.ReturnType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 23:06:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class EachAdapter extends AbstractHQueryAdapter{
    public abstract void invoke(Object o);

    public void each(){
        loadCurrentObject();
        ReturnType returnType=getCurrentReturnType();
        if(returnType.equals(ReturnType.PLAIN)){
            invoke(t);
        }else if(returnType.equals(ReturnType.LIST)){
            List<?> eachList= (List<?>) t;
            if(eachList!=null&&!eachList.isEmpty()){
                for(Object o:eachList){
                    invoke(o);
                }
            }
        }else if(returnType.equals(ReturnType.ARRAY)){
            Object[] eachObjects = (Object[])t;
			if (eachObjects != null && eachObjects.length>0) {
                for(Object o:eachObjects){
                    invoke(o);
                }
			}
        }
    }

}
