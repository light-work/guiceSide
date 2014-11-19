package org.guiceside.persistence.hibernate.dao;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.guiceside.commons.Assert;
import org.guiceside.commons.Page;
import org.guiceside.commons.PageUtils;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.GenericsUtils;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.Permanent;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.impl.CriteriaImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 泛型HibernateDao 提供基本的数据操作 继承HibernateDaoSupport<br/>
 * 该类可为Singleton
 * </p>
 * User:zhenjiawang(zhenjiaWang@gmail.com) Date:2008-7-24 Time:下午02:26:07
 * Email:(zhenjiaWang@gmail.com) QQ:(119582291)
 * 
 */
@SuppressWarnings("deprecation")
@Singleton
@Deprecated
public class HibernateGeneralDao<T extends IdEntity, ID extends Serializable> extends
		HibernateDaoSupport {

	protected final Logger log = Logger.getLogger(getClass());

	protected Class<T> entityClass;

	protected String className;

	/**
	 * 
	 * 构造方法 通过泛型声明，获取子类T类型
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HibernateGeneralDao() {
		this.entityClass = GenericsUtils.getSuperClassGenricType(getClass());
		className = entityClass.getSimpleName();
	}

	/**
	 * 
	 * 通过id加载一个Entity  id为null将抛出IllegalArgumentException
	 * 
	 * @param id
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		Assert.notNull(id);
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 
	 * 通过id获取一个Entity  id为null将抛出IllegalArgumentException
	 * 
	 * @param id
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public T getById(ID id) {
		Assert.notNull(id);
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 
	 * 删除一个entity entity为null将抛出IllegalArgumentException<br/>
	 * 如果entity显示声明@Permanent则进行update
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		Assert.notNull(entity);
		if (entity.getClass().isAnnotationPresent(Permanent.class)) {
			Permanent permanent = entity.getClass().getAnnotation(
					Permanent.class);
			String column = permanent.column();
			String defaultValue = permanent.defaultValue();
			try {
				BeanUtils.setValue(entity, column, defaultValue);
				this.update(entity);
			} catch (Exception e) {
				throw new HibernateDataAccessException(e);
			}
		}else{
			getSession().delete(entity);
		}
		
	}

	/**
	 * 
	 * 保存一个entity entity为null将抛出IllegalArgumentException
	 * 
	 * @param entity
	 */
	public void save(T entity) {
		Assert.notNull(entity);
		getSession().save(entity);
	}

	/**
	 * 
	 * 更新一个entity entity为null将抛出IllegalArgumentException
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		Assert.notNull(entity);
		getSession().update(entity);
	}

	/**
	 * 
	 * 保存或更新一个entity entity为null将抛出IllegalArgumentException
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity) {
		Assert.notNull(entity);
		getSession().saveOrUpdate(entity);
	}

	/**
	 * 
	 * 获取所有结果集
	 * 
	 * @return List<T>
	 */
	public List<T> getAll() {
		return findByCriteria();
	}

	/**
	 * 
	 * 根据Criterion条件创建Criteria
	 * 
	 * @param criterions
	 * @return Criteria
	 */
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 
	 * 根据orderBy属性 为Criteria排序条件
	 * 
	 * @param orderBy
	 * @param isAsc
	 * @param criteria
	 * @return Criteria
	 */
	public Criteria addOrderby(String orderBy, boolean isAsc, Criteria  criteria) {
		if (isAsc) {
			criteria.addOrder(Order.asc(orderBy));
		} else {
			criteria.addOrder(Order.desc(orderBy));
		}
		return criteria;
	}
	
	/**
	 * 进行page对象构造
	 * @param limit
	 * @param start
	 * @param criteria
	 * @return Page<T>
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private Page<T> createCriteriaPage(int limit, int start, Criteria criteria) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		int totalRecord = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		if (totalRecord < 1)
			return null;
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		Page page = PageUtils.createPage(limit, start / limit + 1, totalRecord);
		criteria.setFirstResult(page.getBeginIndex()).setMaxResults(
				page.getEveryPage());
		return page;
	}

	/**
	 * 
	 * 根据Criterion条件获取对象结果集合
	 * 
	 * @param criterion
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	
	
	/**
	 * 
	 * 根据Criteria条件获取对象结果集合
	 * 
	 * @param criteria
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criteria criteria) {
		return criteria.list();
	}

	
	
	/**
	 * 
	 * 根据Criterion条件,Page信息获取对象结果集合
	 * 
	 * @param limit
	 * @param start
	 * @param criterion
	 * @return Page<T>
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findByCriteria(int limit, int start, Criterion... criterion) {
		Criteria criteria = createCriteria(criterion);
		Page<T> page = createCriteriaPage(limit, start, criteria);
		if (page == null){
			page=PageUtils.createPage(limit,start / limit + 1, 0);
		}else{
			page.setResultList(criteria.list());
		}		
		return page;
	}
	
	

	/**
	 * 
	 * 根据Criteria条件,Page信息获取对象结果集合
	 * 
	 * @param limit
	 * @param start
	 * @param criteria
	 * @return Page<T>
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findByCriteria(int limit, int start, Criteria criteria) {
		Page<T> page = createCriteriaPage(limit, start, criteria);
		if (page == null){
			page=PageUtils.createPage(limit,start / limit + 1, 0);
		}else{
			page.setResultList(criteria.list());
		}
		return page;
	}
	
	
	
	/**
	 * 
	 * 根据Criterion条件,获取记录总数
	 * 
	 * @param criterion
	 * @return int
	 */
	public int rowCountByCriteria(Criterion... criterion) {
		Criteria criteria = createCriteria(criterion);
		criteria.setProjection(Projections.rowCount());
		criteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalRecord = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		return totalRecord;
	}

	/**
	 * 
	 * 根据Criteria条件,获取记录总数
	 * 
	 * @param criteria
	 * @return int
	 */
	public int rowCountByCriteria(Criteria criteria) {
		criteria.setProjection(Projections.rowCount());
		criteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalRecord = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		return totalRecord;
	}
	
	/**
	 * 
	 * 根据Criterion条件获取对象
	 * 
	 * @param criterion
	 * @return Object
	 */
	public Object findObjectByCriteria(Criterion... criterion) {
		return createCriteria(criterion).uniqueResult();
	}

	
	/**
	 * 
	 * 根据Criteria条件获取对象
	 * 
	 * @param criteria
	 * @return Object
	 */
	public Object findObjectByCriteria(Criteria criteria) {
		return criteria.uniqueResult();
	}

	
	
	/**
	 * 
	 * 刷新session缓存对象 实现缓存对象与数据库对象数据立即同步
	 * 
	 */
	public void flush() {
		getSession().flush();
	}
}
