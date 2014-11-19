package org.guiceside.persistence.entity;

import java.util.Date;

/**
 * <p>
 * 所有实现Tracker的类需要提供四个属性<br/>
 * created、createdBy、updated、updatedBy</br>
 * 在Action中进行saveorupdate时将自动绑定四属性
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public interface Tracker {
	public Date getCreated();

	public void setCreated(Date date);

	public String getCreatedBy();

	public void setCreatedBy(String string);

	public Date getUpdated();

	public void setUpdated(Date date);

	public String getUpdatedBy();

	public void setUpdatedBy(String string);
}
