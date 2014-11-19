package org.guiceside.persistence.hibernate.dao.hquery;

import ognl.OgnlException;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.hibernate.dao.enums.Persistent;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.lob.SerializableClob;

import java.io.Serializable;
import java.sql.Clob;

/**
 * 该查询只加载 不会进行查询查询操作
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-30
 * Time: 12:32:32
 * To change this template use File | Settings | File Templates.
 */
public class HQueryLoad extends HQueryCore {


    public HQueryLoad(HQueryContent hQueryContent) {
        setHQueryContent(hQueryContent);
    }

    /**
     * 从持久成删除对象
     */
    public void delete() {
        invoke(Persistent.DELETE);
        clean();
    }

    /**
     * 从持久层删除对象
     * 支持回调
     * @param hQueryCallBack
     */
    public void delete(HQueryCallBack hQueryCallBack) {
        setCurrentHQueryCallBack(hQueryCallBack);
        invoke(Persistent.DELETE);
        clean();
    }


    /**
     * 持久化对象
     */
    public void save() {
        invoke(Persistent.SAVEORUPDATE);
        clean();
    }

    /**
     * 持久化对象
     */
    public void add() {
        invoke(Persistent.SAVE);
        clean();
    }

    /**
     * 针对oracle10g一下不包含10g的客户端 在更新包含clob对象的表时候建立的调用方法
     * @param clobPropertys
     * @param clobValues
     */
    public void updateWithClob(String[] clobPropertys, String[] clobValues){
        invoke(Persistent.SAVEORUPDATE);
        Object cuurentObj = getCurrentObject();
        Serializable id=null;
        try {
            id= (Serializable) BeanUtils.getValue(cuurentObj,"id");
        } catch (OgnlException e) {
            return;
        }
        Object updateObj=getSession().load(cuurentObj.getClass(),id,LockMode.UPGRADE);
        //第1步 循环设置为空值
		for(String expression:clobPropertys){
			try {
				oracle.sql.CLOB empty_clob=oracle.sql.CLOB.empty_lob();
				BeanUtils.setValue(updateObj, expression, empty_clob);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				return;
			}
		}
        //强制flush 将SQL 强制执行 insert
		getSession().flush();
		//强制执行 SQL forUpdate
		getSession().refresh(updateObj, LockMode.UPGRADE);

		int index=0;
		//第2步 将记录锁定 然后循环取出CLOB值进行更新
		for(String expression:clobPropertys){
            if(clobValues[index]==null){
                index++;
                continue;
            }
            String value=clobValues[index].toString();
            SerializableClob sc= null;
            try {
                sc = (SerializableClob) BeanUtils.getValue(updateObj, expression);
            } catch (OgnlException e) {
                return;
            }
            Clob cc=sc.getWrappedClob();
			oracle.sql.CLOB clob = (oracle.sql.CLOB) cc;
	        java.io.Writer pw;
	        try {
				pw = clob.getCharacterOutputStream();
				pw.write(value); 
		        pw.flush();
		        pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			index++;
		}
        getSession().saveOrUpdate(updateObj);
        clean();
    }

    /**
     * oracle10g一下不包含10g的客户端 在保存包含clob对象的表时候建立的调用方法
     * @param clobPropertys
     * @param clobValues
     */
    public void saveWithClob(String[] clobPropertys, String[] clobValues) {
        Object obj = getCurrentObject();
        //第1步 将entity里clob字段初始化
        Clob newClob = Hibernate.createClob(" ");
        for (String expression : clobPropertys) {
            try {
                BeanUtils.setValue(obj, expression, newClob);
            } catch (OgnlException e) {
                return;
            }
        }
        invoke(Persistent.SAVEORUPDATE);
        //强制flush 将SQL 强制执行 insert
        getSession().flush();
        //强制执行 SQL forUpdate
        getSession().refresh(obj, LockMode.UPGRADE);
        int index = 0;
        //第2步 将记录锁定 然后循环取出CLOB值进行更新
        for (String expression : clobPropertys) {
            if(clobValues[index]==null){
                index++;
                continue;
            }
            String value=clobValues[index].toString();
            SerializableClob sc = null;
            try {
                sc = (SerializableClob) BeanUtils.getValue(obj, expression);
            } catch (OgnlException e) {
                return;
            }
            Clob cc = sc.getWrappedClob();
            oracle.sql.CLOB clob = (oracle.sql.CLOB) cc;
            java.io.Writer pw;
            try {
                pw = clob.getCharacterOutputStream();
                pw.write(value);
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            index++;
        }
        getSession().saveOrUpdate(obj);
        clean();
    }

    /**
     * 持久化对象
     * 支持回调
     * @param hQueryCallBack
     */
    public void save(HQueryCallBack hQueryCallBack) {
        setCurrentHQueryCallBack(hQueryCallBack);
        invoke(Persistent.SAVEORUPDATE);
        clean();
    }

   

    @Override
    public HQueryLoad returnType(ReturnType returnType) {
        super.returnType(returnType);    //To change body of overridden methods use File | Settings | File Templates.
        return getCurrentHQueryLoad();
    }

    /**
     * 遍历对象
     * 支持回调
     * @param eachAdapter
     * @return
     */
    public HQueryLoad each(EachAdapter eachAdapter) {
        eachAdapter.init(getHQueryContent());
        eachAdapter.each();
        return getCurrentHQueryLoad();
    }

    /**
     * 从持久层获取指定某一column值
     * @param column
     * @return
     */
    public Object attr(String column) {
        return invokeAttr(column);
    }

    /**
     * 从持久层设置指定某一column值
     * @param column
     * @param value
     * @return
     */
    public HQueryLoad attr(String column, Object value) {
        invokeAttr(column, value);
        return getCurrentHQueryLoad();
    }


}
