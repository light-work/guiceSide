package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.Page;
import org.guiceside.commons.PageUtils;
import org.guiceside.commons.UnknownType;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.search.AliasMode;
import org.guiceside.persistence.entity.search.AndMode;
import org.guiceside.persistence.entity.search.OrMode;
import org.guiceside.persistence.entity.search.SearchMode;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.*;
import org.guiceside.persistence.hibernate.dao.enums.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.internal.CriteriaImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 应用于显示声明@FinderByCriteria方法的拦截机<br/>
 * 进行ByCriteria查询操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class FinderByCriteriaInterceptor implements MethodInterceptor {

	private final Map<Method, FinderByCriteriaDescriptor> finderByCriteriaCache =new ConcurrentHashMap<Method, FinderByCriteriaDescriptor>();
	
	// 存放between条件的缓存
	private final ThreadLocal<Map<String, List>> betweenCacheThreadLocal = new ThreadLocal<Map<String, List>>();

	// 存放or条件的缓存
	private final ThreadLocal<Map<String, List<Criterion>>> orCacheThreadLocal = new ThreadLocal<Map<String, List<Criterion>>>();

	// 存放@or描述的缓存
	private final ThreadLocal<Map<String, OR>> orDescriptiorCacheThreadLocal = new ThreadLocal<Map<String, OR>>();
	
	// 存放@or描述的缓存
	private final ThreadLocal<Map<String, OrMode>> orModeDescriptiorCacheThreadLocal = new ThreadLocal<Map<String, OrMode>>();

	// 存放@or(group)描述的缓存
	private final ThreadLocal<List<String>> orGroupCacheThreadLocal = new ThreadLocal<List<String>>();

	// 存放and条件的缓存
	private final ThreadLocal<Map<String, List<Criterion>>> andCacheThreadLocal = new ThreadLocal<Map<String, List<Criterion>>>();

	// 存放@and描述的缓存
	private final ThreadLocal<Map<String, AND>> andDescriptiorCacheThreadLocal = new ThreadLocal<Map<String, AND>>();
	
	private final ThreadLocal<Map<String, AndMode>> andModeDescriptiorCacheThreadLocal = new ThreadLocal<Map<String, AndMode>>();

	// 存放@and(group)描述的缓存
	private final ThreadLocal<List<String>> andGroupCacheThreadLocal = new ThreadLocal<List<String>>();

	// 存放是否存在@FirstResult参数的缓存
	private final ThreadLocal<Boolean> firstResultCacheThreadLocal = new ThreadLocal<Boolean>();

	// 存放是否存在@MaxResults参数的缓存
	private final ThreadLocal<Boolean> maxResultsCacheThreadLocal = new ThreadLocal<Boolean>();

	// 存放如果存在@FirstResult参数值的缓存
	private final ThreadLocal<Integer> firstResultValueCacheThreadLocal = new ThreadLocal<Integer>();

	// 存放如果存在@MaxResults参数值的缓存
	private final ThreadLocal<Integer> maxResultsValueCacheThreadLocal = new ThreadLocal<Integer>();

	// 存放order排序类型的缓存
	private final ThreadLocal<Map<String, Boolean>> orderCacheThreadLocal = new ThreadLocal<Map<String, Boolean>>();

	// 存放by排序字段的缓存
	private final ThreadLocal<Map<String, String>> byCacheThreadLocal = new ThreadLocal<Map<String, String>>();

	// 存放orderby(group)描述的缓存
	private final ThreadLocal<List<String>> orderByGroupCacheThreadLocal = new ThreadLocal<List<String>>();

	private void initThreadLocalVariable() {
		betweenCacheThreadLocal.set(new ConcurrentHashMap<String, List>());
		orCacheThreadLocal
				.set(new ConcurrentHashMap<String, List<Criterion>>());
		orDescriptiorCacheThreadLocal.set(new ConcurrentHashMap<String, OR>());
		orModeDescriptiorCacheThreadLocal.set( new ConcurrentHashMap<String, OrMode>());
		orGroupCacheThreadLocal.set(new ArrayList<String>());

		andCacheThreadLocal
				.set(new ConcurrentHashMap<String, List<Criterion>>());
		andDescriptiorCacheThreadLocal
				.set(new ConcurrentHashMap<String, AND>());
		andGroupCacheThreadLocal.set(new ArrayList<String>());
		andModeDescriptiorCacheThreadLocal.set(new ConcurrentHashMap<String, AndMode>());
		
		firstResultCacheThreadLocal.set(new Boolean(false));
		maxResultsCacheThreadLocal.set(new Boolean(false));

		firstResultValueCacheThreadLocal.set(new Integer(-1));
		maxResultsValueCacheThreadLocal.set(new Integer(-1));

		orderCacheThreadLocal.set(new ConcurrentHashMap<String, Boolean>());

		byCacheThreadLocal.set(new ConcurrentHashMap<String, String>());

		orderByGroupCacheThreadLocal.set(new ArrayList<String>());
	}

	private void destoryThreadLocalVariable() {
		
		betweenCacheThreadLocal.set(null);
		orCacheThreadLocal.set(null);
		orDescriptiorCacheThreadLocal.set(null);
		orGroupCacheThreadLocal.set(null);
		orModeDescriptiorCacheThreadLocal.set(null);
		andCacheThreadLocal.set(null);
		andDescriptiorCacheThreadLocal.set(null);
		andGroupCacheThreadLocal.set(null);
		andModeDescriptiorCacheThreadLocal.set(null);
		firstResultCacheThreadLocal.set(null);
		maxResultsCacheThreadLocal.set(null);

		firstResultValueCacheThreadLocal.set(null);
		maxResultsValueCacheThreadLocal.set(null);

		orderCacheThreadLocal.set(null);

		byCacheThreadLocal.set(null);

		orderByGroupCacheThreadLocal.set(null);
	}

	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		initThreadLocalVariable();
		FinderByCriteriaDescriptor finderByCriteriaDescriptor = getFinderByCriteriaDescriptior(invocation);
		if (finderByCriteriaDescriptor == null) {
			destoryThreadLocalVariable();
			throw new HibernateDataAccessException(
					"在使用@FinderByCriteria查询对象发生错误,当前方法的finderByCriteriaDescriptior必须存在");
		}
		Object result = null;
		Object args[] = invocation.getArguments();

		Session session = SessionFactoryHolder.getCurrentSessionFactory()
				.getCurrentSession();
		Criteria criteria = session
				.createCriteria(finderByCriteriaDescriptor.entityClass);

		Alias[] aliass = finderByCriteriaDescriptor.finderByCriteria.alias();
		if (aliass != null && aliass.length > 0) {
			for (Alias alias : aliass) {
				if (StringUtils.isNotBlank(alias.column())
						&& StringUtils.isNotBlank(alias.alias())) {
					criteria.createAlias(alias.column(), alias.alias());
				}
			}
		}
		// 加动态参数
		int index = 0;
		for (Object value : args) {
			Object annotation = finderByCriteriaDescriptor.parameterAnnotations[index];
			Class<?> defaultType = finderByCriteriaDescriptor.parameterTypes[index];
			if (annotation == null) {
				index++;
				continue;
			}
			if (annotation instanceof EQ) {
				EQ eq = (EQ) annotation;
				String column = eq.column();
				boolean ignoreCase = eq.ignoreCase();
				boolean not = eq.not();
				boolean ignoreBank = eq.ignoreBank();
				Relation relation = eq.relation();
				Class<? extends Object> forciblyType=eq.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = eq.or();
				AND and = eq.and();
				createSimpleRestriction(criteria, Condition.EQ, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						null);
			} else if (annotation instanceof GE) {
				GE ge = (GE) annotation;
				String column = ge.column();
				boolean ignoreCase = ge.ignoreCase();
				boolean not = ge.not();
				boolean ignoreBank = ge.ignoreBank();
				Relation relation = ge.relation();
				Class<? extends Object> forciblyType=ge.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = ge.or();
				AND and = ge.and();
				createSimpleRestriction(criteria, Condition.GE, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						null);
			} else if (annotation instanceof GT) {
				GT gt = (GT) annotation;
				String column = gt.column();
				boolean ignoreCase = gt.ignoreCase();
				boolean not = gt.not();
				boolean ignoreBank = gt.ignoreBank();
				Relation relation = gt.relation();
				Class<? extends Object> forciblyType=gt.forciblyType();	
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = gt.or();
				AND and = gt.and();
				createSimpleRestriction(criteria, Condition.GT, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						null);
			} else if (annotation instanceof LE) {
				LE le = (LE) annotation;
				String column = le.column();
				boolean ignoreCase = le.ignoreCase();
				boolean not = le.not();
				boolean ignoreBank = le.ignoreBank();
				Relation relation = le.relation();
				Class<? extends Object> forciblyType=le.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = le.or();
				AND and = le.and();
				createSimpleRestriction(criteria, Condition.LE, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						null);
			} else if (annotation instanceof LT) {
				LT lt = (LT) annotation;
				String column = lt.column();
				boolean ignoreCase = lt.ignoreCase();
				boolean not = lt.not();
				boolean ignoreBank = lt.ignoreBank();
				Relation relation = lt.relation();
				Class<? extends Object> forciblyType=lt.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = lt.or();
				AND and = lt.and();
				createSimpleRestriction(criteria, Condition.LT, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						null);
			} else if (annotation instanceof LIKE) {
				LIKE like = (LIKE) annotation;
				String column = like.column();
				boolean ignoreCase = like.ignoreCase();
				boolean not = like.not();
				boolean ignoreBank = like.ignoreBank();
				Match match = like.matchMode();
				Relation relation = like.relation();
				Class<? extends Object> forciblyType=like.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = like.or();
				AND and = like.and();
				createSimpleRestriction(criteria, Condition.LIKE, value, defaultType,
						column, ignoreCase, not, ignoreBank, relation, or, and,
						match);
			}if (annotation instanceof IN) {
				IN in= (IN) annotation;
				String column = in.column();	
				boolean not = in.not();
				Relation relation = in.relation();		
				OR or = in.or();
				AND and = in.and();
				createSimpleRestriction(criteria, Condition.IN, value, defaultType,
						column, false, not, false, relation, or, and,
						null);
			}  else if (annotation instanceof Between) {
				Between between = (Between) annotation;
				String column = between.column();
				String group = between.group();
				boolean not = between.not();
				boolean ignoreBank = between.ignoreBank();
				Relation relation = between.relation();
				Class<? extends Object> forciblyType=between.forciblyType();
				if(isForciblyType(forciblyType)){
					defaultType=forciblyType;
				}
				OR or = between.or();
				AND and = between.and();
				createBetweenRestriction(criteria, value, defaultType, column, group,
						not, ignoreBank, relation, or, and);
			} else if (annotation instanceof FirstResult) {
				Boolean firstResultCache = firstResultCacheThreadLocal.get();
				firstResultCache = true;
				firstResultCacheThreadLocal.set(firstResultCache);
				value = BeanUtils.convertValue(value, defaultType);
				Integer firstResultValueCache = firstResultValueCacheThreadLocal
						.get();
				firstResultValueCache = (Integer) value;
				firstResultValueCacheThreadLocal.set(firstResultValueCache);
			} else if (annotation instanceof MaxResults) {
				Boolean maxResultsCache = maxResultsCacheThreadLocal.get();
				maxResultsCache = true;
				maxResultsCacheThreadLocal.set(maxResultsCache);
				value = BeanUtils.convertValue(value, defaultType);
				Integer maxResultsValueCache = maxResultsValueCacheThreadLocal
						.get();
				maxResultsValueCache = (Integer) value;
				maxResultsValueCacheThreadLocal.set(maxResultsValueCache);
			} else if (annotation instanceof org.guiceside.persistence.hibernate.dao.annotation.Order) {
				org.guiceside.persistence.hibernate.dao.annotation.Order order = (org.guiceside.persistence.hibernate.dao.annotation.Order) annotation;
				Map<String, Boolean> orderCache = orderCacheThreadLocal.get();
				String group = order.group();
				Boolean isAsc = orderCache.get(group);
				if (isAsc == null) {
					value = BeanUtils.convertValue(value, defaultType);
					isAsc = (Boolean) value;
					orderCache.put(group, isAsc);
				}
			} else if (annotation instanceof By) {
				By by = (By) annotation;
				Map<String, String> byCache = byCacheThreadLocal.get();

				String group = by.group();
				String byColumn = byCache.get(group);
				if (byColumn == null) {
					value = BeanUtils.convertValue(value, defaultType);
					if (value == null) {
						index++;
						continue;
					}
					byColumn = (String) value;
					byCache.put(group, byColumn);
				}
				List<String> orderByGroupCache = orderByGroupCacheThreadLocal
						.get();
				if (!orderByGroupCache.contains(group)) {
					orderByGroupCache.add(group);
				}

			}else if(annotation instanceof SearchModeList){
				if(value!=null){
					List<?> searchModes=(List<?>) value;
					if(searchModes!=null&&!searchModes.isEmpty()){
						for(int i=0;i<searchModes.size();i++){
							SearchMode mode=(SearchMode) searchModes.get(i);
							if(mode.getAliasMode()!=null){
								AliasMode aliasMode=mode.getAliasMode();
								if (StringUtils.isNotBlank(aliasMode.getColumn())
										&& StringUtils.isNotBlank(aliasMode.getAlias())) {
									criteria.createAlias(aliasMode.getColumn(), aliasMode.getAlias());
								}
							}
							if(StringUtils.isNotBlank(mode.getColumn())){
								if(mode.getCondition().compareTo(Condition.BETWEEN)==0){
									
								}else{
									Class<?> defaultModeType=Object.class;
									if(mode.getValue()!=null){
										defaultModeType=mode.getValue().getClass();
										if(mode.getType()!=null){
											if(isForciblyType(mode.getType())){
												defaultModeType=mode.getType();
											}
										}
									}
									createSimpleRestrictionBySearchMode(criteria, mode.getCondition(), 
											mode.getValue(), defaultModeType, mode.getColumn(), mode.isIgnoreCase(), mode.isNot(), mode.isIgnoreBank(),
											mode.getRelation(), mode.getOrMode(), mode.getAndMode(), mode.getMatch());
								}
							}
						}
					}
				}
			}
			index++;
		}

		

		/** *************************************************************************** */
		// 加载固定参数
		Restriction[] restrictions = finderByCriteriaDescriptor.finderByCriteria
				.restriction();
		createBasicRestriction(criteria, restrictions);

		OrderBy[] orderBys = finderByCriteriaDescriptor.finderByCriteria
				.orderBy();
		createBasicOrderBy(criteria, orderBys);
		
		org.guiceside.persistence.hibernate.dao.annotation.Projection[] projections=finderByCriteriaDescriptor.finderByCriteria.projection();
		createBasicProjection(criteria, projections);
		/** *************************************************************************** */

		// or
		createOrRestriction(criteria);
		
		// or by searchMode
		createOrRestrictionBySearchMode(criteria);
		// and
		createAndRestriction(criteria);
		
		//and by searchMode
		createAndRestrictionBySearchMode(criteria);

		// orderBy
		createOrderBy(criteria);
		// paging
		Page page = createPagingRestriction(criteria);

		if (ReturnType.PAGE.equals(finderByCriteriaDescriptor.returnType)) {
			if (page == null){
				Integer firstResultValueCache = firstResultValueCacheThreadLocal
				.get();
				Integer maxResultsValueCache = maxResultsValueCacheThreadLocal
				.get();
				page=PageUtils.createPage(maxResultsValueCache,firstResultValueCache / maxResultsValueCache + 1, 0);
			}else{
				page.setResultList(criteria.list());
			}
			result = page;
		} else if (ReturnType.LIST
				.equals(finderByCriteriaDescriptor.returnType)) {
			result = getAsCollection(finderByCriteriaDescriptor, criteria
					.list());
		} else if (ReturnType.PLAIN
				.equals(finderByCriteriaDescriptor.returnType)) {
			result = criteria.uniqueResult();
		} else if (ReturnType.ARRAY
				.equals(finderByCriteriaDescriptor.returnType)) {
			List tempList = criteria.list();
			if (tempList != null && !tempList.isEmpty()) {
				result = tempList.toArray();
			}
			result = null;
		}
		destoryThreadLocalVariable();
		return result;
	}

	private void createBasicOrderBy(Criteria criteria, OrderBy[] orderBys) {
		if (orderBys != null && orderBys.length > 0) {
			for (OrderBy orderBy : orderBys) {
				if (StringUtils.isBlank(orderBy.column())) {
					continue;
				}

				addOrderby(orderBy.column(), orderBy.isAsc(), criteria);
			}
		}
	}

	private boolean isForciblyType(Class<? extends Object> forciblyType){	
		return !forciblyType.getName().equals(UnknownType.class.getName());
	}
	/**
	 * 创建基本投影 通过FinderByCriteria声明的@Projection
	 * 
	 * @param criteria
	 * @param projections
	 */
	private void createBasicProjection(Criteria criteria,
			org.guiceside.persistence.hibernate.dao.annotation.Projection[] projections) {
		if (projections != null && projections.length > 0) {
			for (org.guiceside.persistence.hibernate.dao.annotation.Projection projection : projections) {
				if (StringUtils.isBlank(projection.column())
						|| projection.type().equals(ProjectionType.NULL)) {
					continue;
				}
				String column = projection.column();
				ProjectionType type=projection.type();
				switch (type) {
				case COUNT:
					criteria.setProjection(Projections.count(column));
					break;
				case COUNTDISTINCT:
					criteria.setProjection(Projections.countDistinct(column));
					break;
				case AVG:
					criteria.setProjection(Projections.avg(column));
					break;
				case SUM:
					criteria.setProjection(Projections.sum(column));
					break;
				case MIN:
					criteria.setProjection(Projections.min(column));
					break;
				case MAX:
					criteria.setProjection(Projections.max(column));
					break;
				case DISTINCT:
					criteria.setProjection(Projections.distinct(Projections.property(column)));
					break;
				}
			}
		}
	}
	
	/**
	 * 创建基本条件 通过FinderByCriteria声明的@Restriction
	 * 
	 * @param criteria
	 * @param restrictions
	 */
	private void createBasicRestriction(Criteria criteria,
			Restriction[] restrictions) {
		if (restrictions != null && restrictions.length > 0) {
			for (Restriction restriction : restrictions) {
				if (StringUtils.isBlank(restriction.column())
						|| StringUtils.isBlank(restriction.value())) {
					continue;
				}
				String column = restriction.column();
				Object value = restriction.value();
				Object value1 = restriction.value1();
				Class<? extends Object> valueType = restriction.valueType();
				Class<? extends Object> value1Type = restriction.value1Type();
				boolean ignoreCase = restriction.ignoreCase();
				boolean not = restriction.not();
				boolean ignoreBank = restriction.ignoreBank();
				Match match = restriction.matchMode();
				Relation relation = restriction.relation();
				OR or = restriction.or();
				AND and = restriction.and();
				Condition condition = restriction.condition();
				switch (condition) {
				case LIKE:
					createSimpleRestriction(criteria, condition, value,
							valueType, column, ignoreCase, not, ignoreBank,
							relation, or, and, match);
					break;
				case BETWEEN:
					createBasicBetweenRestriction(criteria, value, value1,
							valueType, value1Type, column, not, ignoreBank,
							relation, or, and);
					break;
				default:
					createSimpleRestriction(criteria, condition, value,
							valueType, column, ignoreCase, not, ignoreBank,
							relation, or, and, null);
					break;
				}
			}
		}
	}

	/**
	 * 添加排序条件
	 * 
	 * @param orderBy
	 * @param isAsc
	 * @param criteria
	 */
	private void addOrderby(String orderBy, boolean isAsc, Criteria criteria) {
		if (isAsc) {
			criteria.addOrder(Order.asc(orderBy));
		} else {
			criteria.addOrder(Order.desc(orderBy));
		}
	}

	/**
	 * 创建排序条件
	 * 
	 * @param criteria
	 */
	private void createOrderBy(Criteria criteria) {
		List<String> orderByGroupCache = orderByGroupCacheThreadLocal.get();
		Map<String, Boolean> orderCache = orderCacheThreadLocal.get();
		Map<String, String> byCache = byCacheThreadLocal.get();
		if (!orderByGroupCache.isEmpty()) {
			for (String group : orderByGroupCache) {
				Boolean isAsc = orderCache.get(group);
				String byColum = byCache.get(group);
				addOrderby(byColum, isAsc, criteria);
			}
		}
	}

	/**
	 * 创建paging对象
	 * 
	 * @param criteria
	 * @return 返回Page对象
	 */
	@SuppressWarnings("unchecked")
	private Page createPagingRestriction(Criteria criteria) {
		Boolean firstResultCache = firstResultCacheThreadLocal.get();
		Boolean maxResultsCache = maxResultsCacheThreadLocal.get();
		if (firstResultCache && maxResultsCache) {
			Integer firstResultValueCache = firstResultValueCacheThreadLocal
					.get();
			Integer maxResultsValueCache = maxResultsValueCacheThreadLocal
					.get();
			if (firstResultValueCache.intValue() > -1
					&& maxResultsValueCache.intValue() > -1) {
				CriteriaImpl impl = (CriteriaImpl) criteria;
				// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
				Projection projection = impl.getProjection();
				List<CriteriaImpl.OrderEntry> orderEntries;
				try {
					orderEntries = (List) BeanUtils.forceGetProperty(impl,
							"orderEntries");
					BeanUtils.forceSetProperty(impl, "orderEntries",
							new ArrayList());
				} catch (Exception e) {
					throw new InternalError(
							" Runtime Exception impossibility throw ");
				}
				int totalRecord = (Integer) criteria.setProjection(
						Projections.rowCount()).uniqueResult();
				if (totalRecord < 1)
					return null;
				criteria.setProjection(projection);
				if (projection == null) {
					criteria
							.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				}
				try {
					BeanUtils.forceSetProperty(impl, "orderEntries",
							orderEntries);
				} catch (Exception e) {
					throw new InternalError(
							" Runtime Exception impossibility throw ");
				}
				if(firstResultValueCache.intValue()>=totalRecord){
					firstResultValueCache=firstResultValueCache.intValue()-maxResultsValueCache.intValue();
				}
				Page page = PageUtils.createPage(maxResultsValueCache,
						firstResultValueCache / maxResultsValueCache + 1,
						totalRecord);
				criteria.setFirstResult(page.getBeginIndex()).setMaxResults(
						page.getEveryPage());
				return page;
			}
		}
		return null;
	}

	/**
	 * 验证忽略非空以后是否正确
	 * 
	 * @param value
	 * @return 是否不是非空对象
	 */
	private boolean validatorIgnoreBank(Object value) {
		if (value instanceof String) {
			if (value == null) {
				return false;
			}
			if (StringUtils.isBlank(value.toString())) {
				return false;
			}
		} else {
			if (value == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 创建简单约束条件 包含 EQ GE GT LE LT LIKE IN<br/> OR AND 分开进行创建
	 * 
	 * @param criteria
	 * @param condition
	 * @param value
	 * @param type
	 * @param column
	 * @param ignoreCase
	 * @param not
	 * @param ignoreBank
	 * @param relation
	 * @param or
	 * @param and
	 * @param matchMode
	 */
	private void createSimpleRestriction(Criteria criteria,
			Condition condition, Object value, Class<?> type, String column,
			boolean ignoreCase, boolean not, boolean ignoreBank,
			Relation relation, OR or, AND and, Match matchMode) {
		Criterion criterion = null;
		if(value instanceof Collection<?>||value instanceof Object[]){
			//排除会发生异常的类型转换 如集合类 与数组类型 通常会转换成String类型
		}else{
			value = BeanUtils.convertValue(value, type);
		}
		if (ignoreBank) {
			if (!validatorIgnoreBank(value)) {
				return;
			}
		}

		switch (condition) {
		case EQ:
			if(value.toString().equals(ValueConstants.ISEMPTY)){
				criterion =Restrictions.isEmpty(column);
			}else if(value.toString().equals(ValueConstants.ISNULL)){
				criterion =Restrictions.isNull(column);
			}else{
				criterion = ignoreCase ? Restrictions.eq(column, value)
						.ignoreCase() : Restrictions.eq(column, value);
			}
			break;
		case GT:
			criterion = ignoreCase ? Restrictions.gt(column, value)
					.ignoreCase() : Restrictions.gt(column, value);
			break;
		case GE:
			criterion = ignoreCase ? Restrictions.ge(column, value)
					.ignoreCase() : Restrictions.ge(column, value);
			break;
		case LT:
			criterion = ignoreCase ? Restrictions.lt(column, value)
					.ignoreCase() : Restrictions.lt(column, value);
			break;
		case LE:
			criterion = ignoreCase ? Restrictions.le(column, value)
					.ignoreCase() : Restrictions.le(column, value);
			break;
		case LIKE: {
			MatchMode mode = null;
			switch (matchMode) {
			case START:
				mode = MatchMode.START;
				break;
			case END:
				mode = MatchMode.END;
			case ANYWHERE:
				mode = MatchMode.ANYWHERE;
				break;
			}
			criterion = ignoreCase ? Restrictions.like(column,
					value != null ? value.toString() : "", mode).ignoreCase()
					: Restrictions.like(column, value != null ? value
							.toString() : "", mode);
		}
			break;
		case IN:{
			Object[] arrayObject=null;
			if(value instanceof Collection<?>){
				Collection<?> collection=(Collection<?>) value;
				arrayObject=collection.toArray();
			}else if(value instanceof Object[]){
				arrayObject=(Object[]) value;	
			}
			criterion=Restrictions.in(column, arrayObject);
			break;
		}
		}
		if (not) {
			criterion = Restrictions.not(criterion);
		}
		if (criterion == null) {
			return;
		}

		if (relation.equals(Relation.OR)) {// 如果是OR
			String group = or.group();// 得到OR组名
			Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
			Map<String, OR> orDescriptiorCache = orDescriptiorCacheThreadLocal
					.get();
			List<String> orGroupCache = orGroupCacheThreadLocal.get();
			List<Criterion> orCriterionCache = orCache.get(group);// 从缓存中获取OR
			OR orDCache = orDescriptiorCache.get(group);// 获取OR
			if (orCriterionCache == null) {// 如果是第一个OR关系 则创建缓存
				orCriterionCache = new ArrayList<Criterion>();
				orDCache = or;
				orDescriptiorCache.put(group, orDCache);
			}
			orCriterionCache.add(criterion);// 将当前条件添加至OR缓存
			orCache.put(group, orCriterionCache);
			if (!orGroupCache.contains(group)) {// 将当前OR组名添加至缓存 排除重复
				orGroupCache.add(group);
			}
		} else if (relation.equals(Relation.AND)) {// 如果是AND
			String group = and.group();// 得到AND组名
			Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
			Map<String, AND> andDescriptiorCache = andDescriptiorCacheThreadLocal
					.get();
			List<String> andGroupCache = andGroupCacheThreadLocal.get();
			List<Criterion> andCriterionCache = andCache.get(group);// 从缓存中获取AND
			AND andDCache = andDescriptiorCache.get(group);// 获取AND
			if (andCriterionCache == null) {// 如果是第一个AND关系 则创建缓存
				andCriterionCache = new ArrayList<Criterion>();
				andDCache = and;
				andDescriptiorCache.put(group, andDCache);
			}
			andCriterionCache.add(criterion);// 将当前条件添加至AND缓存
			andCache.put(group, andCriterionCache);
			if (!andGroupCache.contains(group)) {// 将当前AND组名添加至缓存 排除重复
				andGroupCache.add(group);
			}
		} else if (relation.equals(Relation.NULL)) {
			criteria.add(criterion);
		}
	}

	private void createSimpleRestrictionBySearchMode(Criteria criteria,
			Condition condition, Object value, Class<?> type, String column,
			boolean ignoreCase, boolean not, boolean ignoreBank,
			Relation relation, OrMode or, AndMode and, Match matchMode) {
		Criterion criterion = null;
		if(value instanceof Collection<?>||value instanceof Object[]){
			//排除会发生异常的类型转换 如集合类 与数组类型 通常会转换成String类型
		}else{
			value = BeanUtils.convertValue(value, type);
		}
		if (ignoreBank) {
			if (!validatorIgnoreBank(value)) {
				return;
			}
		}

		switch (condition) {
		case EQ:
			if(value.toString().equals(ValueConstants.ISEMPTY)){
				criterion =Restrictions.isEmpty(column);
			}else if(value.toString().equals(ValueConstants.ISNULL)){
				criterion =Restrictions.isNull(column);
			}else{
				criterion = ignoreCase ? Restrictions.eq(column, value)
						.ignoreCase() : Restrictions.eq(column, value);
			}
			break;
		case GT:
			criterion = ignoreCase ? Restrictions.gt(column, value)
					.ignoreCase() : Restrictions.gt(column, value);
			break;
		case GE:
			criterion = ignoreCase ? Restrictions.ge(column, value)
					.ignoreCase() : Restrictions.ge(column, value);
			break;
		case LT:
			criterion = ignoreCase ? Restrictions.lt(column, value)
					.ignoreCase() : Restrictions.lt(column, value);
			break;
		case LE:
			criterion = ignoreCase ? Restrictions.le(column, value)
					.ignoreCase() : Restrictions.le(column, value);
			break;
		case LIKE: {
			MatchMode mode = null;
			switch (matchMode) {
			case START:
				mode = MatchMode.START;
				break;
			case END:
				mode = MatchMode.END;
			case ANYWHERE:
				mode = MatchMode.ANYWHERE;
				break;
			}
			criterion = ignoreCase ? Restrictions.like(column,
					value != null ? value.toString() : "", mode).ignoreCase()
					: Restrictions.like(column, value != null ? value
							.toString() : "", mode);
		}
			break;
		case IN:{
			Object[] arrayObject=null;
			if(value instanceof Collection<?>){
				Collection<?> collection=(Collection<?>) value;
				arrayObject=collection.toArray();
			}else if(value instanceof Object[]){
				arrayObject=(Object[]) value;	
			}
			criterion=Restrictions.in(column, arrayObject);
			break;
		}
		}
		if (not) {
			criterion = Restrictions.not(criterion);
		}
		if (criterion == null) {
			return;
		}

		if (relation.equals(Relation.OR)) {// 如果是OR
			String group = or.getGroup();// 得到OR组名
			Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
			Map<String, OrMode> orModeDescriptiorCache=orModeDescriptiorCacheThreadLocal.get();
			
			List<String> orGroupCache = orGroupCacheThreadLocal.get();
			List<Criterion> orCriterionCache = orCache.get(group);// 从缓存中获取OR
			OrMode orModeDCache=orModeDescriptiorCache.get(group);//获取OR
			if (orCriterionCache == null) {// 如果是第一个OR关系 则创建缓存
				orCriterionCache = new ArrayList<Criterion>();
				orModeDCache = or;
				orModeDescriptiorCache.put(group, orModeDCache);
			}
			orCriterionCache.add(criterion);// 将当前条件添加至OR缓存
			orCache.put(group, orCriterionCache);
			if (!orGroupCache.contains(group)) {// 将当前OR组名添加至缓存 排除重复
				orGroupCache.add(group);
			}
		} else if (relation.equals(Relation.AND)) {// 如果是AND
			String group = and.getGroup();// 得到AND组名
			Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
			Map<String, AndMode> andModeDescriptiorCache=andModeDescriptiorCacheThreadLocal.get();
			List<String> andGroupCache = andGroupCacheThreadLocal.get();
			List<Criterion> andCriterionCache = andCache.get(group);// 从缓存中获取AND
			AndMode andModeDCache=andModeDescriptiorCache.get(group);// 获取AND
			if (andCriterionCache == null) {// 如果是第一个AND关系 则创建缓存
				andCriterionCache = new ArrayList<Criterion>();
				andModeDCache = and;
				andModeDescriptiorCache.put(group, andModeDCache);
			}
			andCriterionCache.add(criterion);// 将当前条件添加至AND缓存
			andCache.put(group, andCriterionCache);
			if (!andGroupCache.contains(group)) {// 将当前AND组名添加至缓存 排除重复
				andGroupCache.add(group);
			}
		} else if (relation.equals(Relation.NULL)) {
			criteria.add(criterion);
		}
	}
	/**
	 * 创建OR关系约束
	 * 
	 * @param criteria
	 */
	private void createOrRestriction(Criteria criteria) {
		Criterion criterion = null;
		List<String> orGroupCache = orGroupCacheThreadLocal.get();
		if (orGroupCache.isEmpty()) {// 如果存在or条件
			return;
		}
		Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
		Map<String, OR> orDescriptiorCache = orDescriptiorCacheThreadLocal
				.get();
		String temp=null;
		for (String group : orGroupCache) {// 循环取出缓存中OR
			if(temp==null||!group.equals(temp)){
				OR or = orDescriptiorCache.get(group);// 获取OR
				if (or == null) {
					continue;
				}

				List<Criterion> orCriterionCache = orCache.get(group);// 获取缓存中OR约束集合
				if (orCriterionCache == null || orCriterionCache.isEmpty()) {
					continue;
				}
				String joinGroup = or.joinGroup();// 获取连接组名称
				Relation joinRelation = or.joinRelation();// 获取连接组关系
				if (StringUtils.isBlank(joinGroup)) {
					// 没有任何连接关系，进行OR配对
					criterion = builderSimpleOr(orCriterionCache);
				} else {
					if (joinRelation.equals(Relation.NULL)) {
						throw new HibernateDataAccessException(
								"在使用@FinderByCriteria查询对象发生错误,@OR存在joinGroup但是不存在joinRelation");
					} else {
						criterion = builderRelationOr(group, joinGroup,
								joinRelation);
						temp=joinGroup;
					}
				}
				if (criterion != null) {
					criteria.add(criterion);// 添加至criteria
				}
			}
			
		}

	}
	
	private void createOrRestrictionBySearchMode(Criteria criteria) {
		Criterion criterion = null;
		List<String> orGroupCache = orGroupCacheThreadLocal.get();
		if (orGroupCache.isEmpty()) {// 如果存在or条件
			return;
		}
		Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
		Map<String, OrMode> orModeDescriptiorCache=orModeDescriptiorCacheThreadLocal.get();
		String temp=null;
		for (String group : orGroupCache) {// 循环取出缓存中OR
			if(temp==null||!group.equals(temp)){
				OrMode or = orModeDescriptiorCache.get(group);// 获取OR
				if (or == null) {
					continue;
				}

				List<Criterion> orCriterionCache = orCache.get(group);// 获取缓存中OR约束集合
				if (orCriterionCache == null || orCriterionCache.isEmpty()) {
					continue;
				}
				String joinGroup = or.getJoinGroup();// 获取连接组名称
				Relation joinRelation = or.getJoinRelation();// 获取连接组关系
				if (StringUtils.isBlank(joinGroup)) {
					// 没有任何连接关系，进行OR配对
					criterion = builderSimpleOr(orCriterionCache);
				} else {
					if (joinRelation.equals(Relation.NULL)) {
						throw new HibernateDataAccessException(
								"在使用@FinderByCriteria查询对象发生错误,@OR存在joinGroup但是不存在joinRelation");
					} else {
						criterion = builderRelationOr(group, joinGroup,
								joinRelation);
						temp=joinGroup;
					}
				}
				if (criterion != null) {
					criteria.add(criterion);// 添加至criteria
				}
			}
			
			
		}

	}

	private void createAndRestriction(Criteria criteria) {
		Criterion criterion = null;
		List<String> andGroupCache = andGroupCacheThreadLocal.get();
		if (andGroupCache.isEmpty()) {// 如果存在AND条件
			return;
		}
		Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
		Map<String, AND> andDescriptiorCache = andDescriptiorCacheThreadLocal
				.get();
		String temp=null;
		for (String group : andGroupCache) {// 循环取出缓存中AND
			if(temp==null||!group.equals(temp)){
				AND and = andDescriptiorCache.get(group);// 获取AND
				if (and == null) {
					continue;
				}
				List<Criterion> andCriterionCache = andCache.get(group);// 获取缓存中AND约束集合
				if (andCriterionCache == null || andCriterionCache.isEmpty()) {
					return;
				}
				String joinGroup = and.joinGroup();// 获取连接组名称
				Relation joinRelation = and.joinRelation();// 获取连接组关系
				if (StringUtils.isBlank(joinGroup)) {
					// 没有任何连接关系，进行AND配对
					criterion = builderSimpleAnd(andCriterionCache);
				} else {
					if (joinRelation.equals(Relation.NULL)) {
						throw new HibernateDataAccessException(
								"在使用@FinderByCriteria查询对象发生错误,@AND存在joinGroup但是不存在joinRelation");
					} else {
						criterion = builderRelationAnd(group, joinGroup,
								joinRelation);
						temp=joinGroup;
					}
				}
				if (criterion != null) {
					criteria.add(criterion);// 添加至criteria
				}
			}
			
		}

	}
	
	private void createAndRestrictionBySearchMode(Criteria criteria) {
		Criterion criterion = null;
		List<String> andGroupCache = andGroupCacheThreadLocal.get();
		if (andGroupCache.isEmpty()) {// 如果存在AND条件
			return;
		}
		Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
		Map<String, AndMode> andModeDescripitorCache=andModeDescriptiorCacheThreadLocal.get();
		String temp=null;
		for (String group : andGroupCache) {// 循环取出缓存中AND
			if(temp==null||!group.equals(temp)){
				AndMode and = andModeDescripitorCache.get(group);// 获取AND
				if (and == null) {
					continue;
				}
				List<Criterion> andCriterionCache = andCache.get(group);// 获取缓存中AND约束集合
				if (andCriterionCache == null || andCriterionCache.isEmpty()) {
					return;
				}
				String joinGroup = and.getJoinGroup();// 获取连接组名称
				Relation joinRelation = and.getJoinRelation();// 获取连接组关系
				if (StringUtils.isBlank(joinGroup)) {
					// 没有任何连接关系，进行AND配对
					criterion = builderSimpleAnd(andCriterionCache);
				} else {
					if (joinRelation.equals(Relation.NULL)) {
						throw new HibernateDataAccessException(
								"在使用@FinderByCriteria查询对象发生错误,@AND存在joinGroup但是不存在joinRelation");
					} else {
						criterion = builderRelationAnd(group, joinGroup,
								joinRelation);
						temp=joinGroup;
					}
				}
				if (criterion != null) {
					criteria.add(criterion);// 添加至criteria
				}
			}
			
		}

	}

	/**
	 * 创建有关系的OR约束
	 * 
	 * @param group
	 * @param joinGroup
	 * @param joinRelation
	 * @return 返回Criterion
	 */
	private Criterion builderRelationAnd(String group, String joinGroup,
			Relation joinRelation) {
		Criterion criterion = null;
		Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
		Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
		List<Criterion> sourceAndCriterionCache = andCache.get(group);// 获取AND约束源
		List<Criterion> joinAndCriterionCache = andCache.get(joinGroup);// 获取AND目标源
		List<Criterion> joinOrCriterionCache = orCache.get(joinGroup);// 获取OR目标源
		if (sourceAndCriterionCache != null
				&& !sourceAndCriterionCache.isEmpty()) {
			if ((joinOrCriterionCache != null && !joinOrCriterionCache
					.isEmpty())
					&& (joinAndCriterionCache != null && !joinAndCriterionCache
							.isEmpty())) {
				throw new HibernateDataAccessException(
						"在使用@FinderByCriteria查询对象发生错误,@AND:joinGroup存在两个约束条件,无法判断与其进行AND约束,生成失败");
			}
			if (joinAndCriterionCache != null
					&& !joinAndCriterionCache.isEmpty()) {
				criterion = buildJoinRelationRestriction(
						sourceAndCriterionCache, joinAndCriterionCache,
						joinRelation, "AND");
			} else if (joinOrCriterionCache != null
					&& !joinOrCriterionCache.isEmpty()) {
				criterion = buildJoinRelationRestriction(
						sourceAndCriterionCache, joinOrCriterionCache,
						joinRelation, "AND");
			} else {
				throw new HibernateDataAccessException(
						"在使用@FinderByCriteria查询对象发生错误,@AND:joinGroup约束条件生成失败,AND约束创建发生异常");
			}
		} else {
			throw new HibernateDataAccessException(
					"在使用@FinderByCriteria查询对象发生错误,@AND:group约束条件生成失败,AND约束创建发生异常");
		}
		return criterion;
	}

	/**
	 * 创建有关系的OR约束
	 * 
	 * @param group
	 * @param joinGroup
	 * @param joinRelation
	 * @return 返回Criterion
	 */
	private Criterion builderRelationOr(String group, String joinGroup,
			Relation joinRelation) {
		Criterion criterion = null;
		Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
		Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
		List<Criterion> sourceOrCriterionCache = orCache.get(group);// 获取OR约束源
		List<Criterion> joinOrCriterionCache = orCache.get(joinGroup);// 获取OR目标源
		List<Criterion> joinAndCriterionCache = andCache.get(joinGroup);// 获取AND目标源
		if (sourceOrCriterionCache != null && !sourceOrCriterionCache.isEmpty()) {
			if ((joinOrCriterionCache != null && !joinOrCriterionCache
					.isEmpty())
					&& (joinAndCriterionCache != null && !joinAndCriterionCache
							.isEmpty())) {
				throw new HibernateDataAccessException(
						"在使用@FinderByCriteria查询对象发生错误,@OR:joinGroup存在两个约束条件,无法判断与其进行OR约束,生成失败");
			}
			if (joinOrCriterionCache != null && !joinOrCriterionCache.isEmpty()) {
				criterion = buildJoinRelationRestriction(
						sourceOrCriterionCache, joinOrCriterionCache,
						joinRelation, "OR");

			} else if (joinAndCriterionCache != null
					&& !joinAndCriterionCache.isEmpty()) {
				criterion = buildJoinRelationRestriction(
						sourceOrCriterionCache, joinAndCriterionCache,
						joinRelation, "OR");
			} else {
				throw new HibernateDataAccessException(
						"在使用@FinderByCriteria查询对象发生错误,@OR:joinGroup约束条件生成失败,OR约束创建发生异常");
			}
		} else {
			throw new HibernateDataAccessException(
					"在使用@FinderByCriteria查询对象发生错误,@OR:group约束条件生成失败,OR约束创建发生异常");
		}
		return criterion;
	}

	private Criterion buildJoinRelationRestriction(
			List<Criterion> sourcerCriterionCache,
			List<Criterion> joinCriterionCache, Relation joinRelation,
			String type) {
		// 都不为空的情况下
		Criterion a = null;
		Criterion b = null;
		Criterion criterion = null;
		if (sourcerCriterionCache.size() == 1) {// 如果约束源为一个条件
			a = sourcerCriterionCache.get(0);// 将一个条件赋予a
		} else {// 如果约束源为多个条件
			a = builderSimpleOr(sourcerCriterionCache);// 将多个条件组合以后赋予a
		}
		if (joinCriterionCache.size() == 1) {// 如果目标源为一个条件
			b = joinCriterionCache.get(0);// 将一个条件赋予b
		} else {// 如果目标源为多个条件
			b = builderSimpleOr(joinCriterionCache);// 将多个条件组合以后赋予b
		}
		if (a != null && b != null) {
			criterion = buildRelationRestriction(a, b, joinRelation);
		} else {
			throw new HibernateDataAccessException(
					"在使用@FinderByCriteria查询对象发生错误,@" + type + ":其中一方约束生成失败,"
							+ type + "约束创建发生异常");
		}
		return criterion;
	}

	/**
	 * 构造简单OR关系
	 * 
	 * @param orCriterionCache
	 * @return 返回Criterion
	 */
	private Criterion builderSimpleOr(List<Criterion> orCriterionCache) {
		Criterion criterion = null;
		List<Criterion> tempCriterion = new ArrayList<Criterion>();// 声明内部临时缓存
		int size = orCriterionCache.size();// 得到or条件总数
		int index = 0;

		if (size % 2 == 0) {// 可双双入对
			while (true) {
				// 将位于第一，第二约束进行OR匹配 放入临时缓存
				tempCriterion.add(Restrictions.or(orCriterionCache.get(index),
						orCriterionCache.get(index + 1)));
				index += 2;// 跳至后两位
				if (tempCriterion.size() == 2) {// 满足匹配条件
					Criterion a = tempCriterion.get(0);// 取出临时缓存中第一个已设置好的OR条件
					Criterion b = tempCriterion.get(1);// 取出临时缓存中第二个已设置好的OR条件
					tempCriterion.clear();// 清空缓存
					tempCriterion.add(Restrictions.or(a, b));// 将第一个和第二个匹配完成的OR条件放入缓存，索引为0
				}
				if (index >= size) {
					break;
				}
			}
		} else {// 1双 1单 入对
			while (true) {
				if (index < size) {

					// 将位于第1约束放入临时缓存
					tempCriterion.add(orCriterionCache.get(index));
					index += 1;// 跳至后一位
					if (tempCriterion.size() == 2) {// 满足匹配条件
						Criterion a = tempCriterion.get(0);// 取出临时缓存中第一个已设置好的OR条件
						Criterion b = tempCriterion.get(1);// 取出临时缓存中第二个已设置好的OR条件
						tempCriterion.clear();// 清空缓存
						tempCriterion.add(Restrictions.or(a, b));// 将第一个和第二个匹配完成的OR条件放入缓存，索引为0
					}
				} else if (index >= size) {
					break;
				}
			}
		}
		if (tempCriterion.isEmpty()) {
			tempCriterion = null;// 清空缓存
			return criterion;
		}
		criterion = tempCriterion.get(0);// 将匹配完成OR条件返回
		tempCriterion = null;
		return criterion;
	}

	/**
	 * 构造简单AND关系
	 * 
	 * @param andCriterionCache
	 * @return 返回Criterion
	 */
	private Criterion builderSimpleAnd(List<Criterion> andCriterionCache) {
		Criterion criterion = null;
		List<Criterion> tempCriterion = new ArrayList<Criterion>();// 声明内部临时缓存
		int size = andCriterionCache.size();// 得到and条件总数
		int index = 0;

		if (size % 2 == 0) {// 可双双如对
			while (true) {
				// 将位于第一，第二约束进行AND匹配 放入临时缓存
				tempCriterion.add(Restrictions.and(
						andCriterionCache.get(index), andCriterionCache
								.get(index + 1)));
				index += 2;// 跳至后两位
				if (tempCriterion.size() == 2) {// 满足匹配条件
					Criterion a = tempCriterion.get(0);// 取出临时缓存中第一个已设置好的AND条件
					Criterion b = tempCriterion.get(1);// 取出临时缓存中第二个已设置好的AND条件
					tempCriterion.clear();// 清空缓存
					tempCriterion.add(Restrictions.and(a, b));// 将第一个和第二个匹配完成的AND条件放入缓存，索引为0
				}
				if (index >= size) {
					break;
				}
			}
		} else {// 1双 1单 入对
			while (true) {
				if (index < size) {
					// 将位于第1约束放入临时缓存
					tempCriterion.add(andCriterionCache.get(index));
					index += 1;// 跳至后一位
					if (tempCriterion.size() == 2) {// 满足匹配条件
						Criterion a = tempCriterion.get(0);// 取出临时缓存中第一个已设置好的AND条件
						Criterion b = tempCriterion.get(1);// 取出临时缓存中第二个已设置好的AND条件
						tempCriterion.clear();// 清空缓存
						tempCriterion.add(Restrictions.and(a, b));// 将第一个和第二个匹配完成的OR条件放入缓存，索引为0
					}
				} else if (index >= size) {
					break;
				}
			}
		}
		if (tempCriterion.isEmpty()) {
			tempCriterion = null;// 清空缓存
			return criterion;
		}
		criterion = tempCriterion.get(0);// 将匹配完成OR条件返回
		tempCriterion = null;
		return criterion;
	}

	/**
	 * 构造带有关系型的约束 用于OR AND之间
	 * 
	 * @param a
	 * @param b
	 * @param joinRelation
	 * @return 返回Criterion
	 */
	private Criterion buildRelationRestriction(Criterion a, Criterion b,
			Relation joinRelation) {
		Criterion criterion = null;
		switch (joinRelation) {
		case OR:
			criterion = Restrictions.or(a, b);
			break;
		case AND:
			criterion = Restrictions.and(a, b);
		}
		return criterion;
	}

	/**
	 * 创建between条件约束
	 * 
	 * @param criteria
	 * @param value
	 * @param type
	 * @param column
	 * @param not
	 * @param ignoreBank
	 */
	
	private void createBasicBetweenRestriction(Criteria criteria, Object value,
			Object value1, Class<? extends Object> type,
			Class<? extends Object> type1, String column, boolean not,
			boolean ignoreBank, Relation relation, OR or, AND and) {
		Criterion criterion = null;
		
		value =BeanUtils.convertValue(value, type);
		value1 = BeanUtils.convertValue(value1, type1);
		if (ignoreBank) {// 判断是否符合忽略条件
			if (!validatorIgnoreBank(value) || !validatorIgnoreBank(value1)) {
				return;
			}
		}

		criterion = Restrictions.between(column, value, value1);// 进行匹配
		if (not) {// 是否为反向条件
			criterion = Restrictions.not(criterion);
		}
		if (relation.equals(Relation.OR)) {// 如果是OR
			String orGroup = or.group();// 得到OR组名
			Map<String, List<Criterion>> orCache = orCacheThreadLocal.get();
			Map<String, OR> orDescriptiorCache = orDescriptiorCacheThreadLocal
					.get();
			List<String> orGroupCache = orGroupCacheThreadLocal.get();
			List<Criterion> orCriterionCache = orCache.get(orGroup);// 从缓存中获取OR
			OR orDCache = orDescriptiorCache.get(orGroup);// 获取OR
			if (orCriterionCache == null) {// 如果是第一个OR关系 则创建缓存
				orCriterionCache = new ArrayList<Criterion>();
				orDCache = or;
				orDescriptiorCache.put(orGroup, orDCache);
			}
			orCriterionCache.add(criterion);// 将当前条件添加至OR缓存
			orCache.put(orGroup, orCriterionCache);
			if (!orGroupCache.contains(orGroup)) {// 将当前OR组名添加至缓存 排除重复
				orGroupCache.add(orGroup);
			}
		} else if (relation.equals(Relation.AND)) {
			String andGroup = and.group();// 得到AND组名
			Map<String, List<Criterion>> andCache = andCacheThreadLocal.get();
			Map<String, AND> andDescriptiorCache = andDescriptiorCacheThreadLocal
					.get();
			List<String> andGroupCache = andGroupCacheThreadLocal.get();
			List<Criterion> andCriterionCache = andCache.get(andGroup);// 从缓存中获取AND
			AND andDCache = andDescriptiorCache.get(andGroup);// 获取AND
			if (andCriterionCache == null) {// 如果是第一个AND关系 则创建缓存
				andCriterionCache = new ArrayList<Criterion>();
				andDCache = and;
				andDescriptiorCache.put(andGroup, andDCache);
			}
			andCriterionCache.add(criterion);// 将当前条件添加至AND缓存
			andCache.put(andGroup, andCriterionCache);
			if (!andGroupCache.contains(andGroup)) {// 将当前AND组名添加至缓存
				// 排除重复
				andGroupCache.add(andGroup);
			}
		} else if (relation.equals(Relation.NULL)) {
			criteria.add(criterion);// 添加至criterion
		}
	}

	/**
	 * 创建between条件约束
	 * 
	 * @param criteria
	 * @param value
	 * @param type
	 * @param column
	 * @param group
	 * @param not
	 * @param ignoreBank
	 */
	@SuppressWarnings("unchecked")
	private void createBetweenRestriction(Criteria criteria, Object value,
			Class<?> type, String column, String group, boolean not,
			boolean ignoreBank, Relation relation, OR or, AND and) {
		Criterion criterion = null;
		Map<String, List> betweenCache = betweenCacheThreadLocal.get();
		List<String> betweenColumnCache = betweenCache.get(group + "Column");// 从缓存中获取bewteen列缓存
		List<Object> betweenValueCache = betweenCache.get(group + "Value");// 从缓存中获取bewteen值缓存
		if (betweenColumnCache == null && betweenValueCache == null) {
			betweenColumnCache = new ArrayList<String>();// 初始化
			betweenValueCache = new ArrayList<Object>();
		}
		if (betweenColumnCache.size() < 2 && betweenValueCache.size() < 2) {// 列于值大小不满足匹配条件
			value = BeanUtils.convertValue(value, type);// 进行类型转换
			if (ignoreBank) {// 判断是否符合忽略条件
				if (!validatorIgnoreBank(value)) {
					return;
				}
			}
			// 将当前约束添加至缓存
			betweenColumnCache.add(column);
			betweenValueCache.add(value);
			betweenCache.put(group + "Column", betweenColumnCache);
			betweenCache.put(group + "Value", betweenValueCache);
		}
		if (betweenColumnCache.size() < 2) {// 不满足条件
			return;
		}
		if (betweenColumnCache.size() == 2) {// 满足条件
			if (betweenColumnCache.get(0).equals(betweenColumnCache.get(1))) {
				criterion = Restrictions.between(column, betweenValueCache
						.get(0), betweenValueCache.get(1));// 进行匹配
				if (not) {// 是否为反向条件
					criterion = Restrictions.not(criterion);
				}
				if (relation.equals(Relation.OR)) {// 如果是OR
					String orGroup = or.group();// 得到OR组名
					Map<String, List<Criterion>> orCache = orCacheThreadLocal
							.get();
					Map<String, OR> orDescriptiorCache = orDescriptiorCacheThreadLocal
							.get();
					List<String> orGroupCache = orGroupCacheThreadLocal.get();
					List<Criterion> orCriterionCache = orCache.get(orGroup);// 从缓存中获取OR
					OR orDCache = orDescriptiorCache.get(orGroup);// 获取OR
					if (orCriterionCache == null) {// 如果是第一个OR关系 则创建缓存
						orCriterionCache = new ArrayList<Criterion>();
						orDCache = or;
						orDescriptiorCache.put(orGroup, orDCache);
					}
					orCriterionCache.add(criterion);// 将当前条件添加至OR缓存
					orCache.put(orGroup, orCriterionCache);
					if (!orGroupCache.contains(orGroup)) {// 将当前OR组名添加至缓存 排除重复
						orGroupCache.add(orGroup);
					}
				} else if (relation.equals(Relation.AND)) {
					String andGroup = and.group();// 得到AND组名
					Map<String, List<Criterion>> andCache = andCacheThreadLocal
							.get();
					Map<String, AND> andDescriptiorCache = andDescriptiorCacheThreadLocal
							.get();
					List<String> andGroupCache = andGroupCacheThreadLocal.get();
					List<Criterion> andCriterionCache = andCache.get(andGroup);// 从缓存中获取AND
					AND andDCache = andDescriptiorCache.get(andGroup);// 获取AND
					if (andCriterionCache == null) {// 如果是第一个AND关系 则创建缓存
						andCriterionCache = new ArrayList<Criterion>();
						andDCache = and;
						andDescriptiorCache.put(andGroup, andDCache);
					}
					andCriterionCache.add(criterion);// 将当前条件添加至AND缓存
					andCache.put(andGroup, andCriterionCache);
					if (!andGroupCache.contains(andGroup)) {// 将当前AND组名添加至缓存
						// 排除重复
						andGroupCache.add(andGroup);
					}
				} else if (relation.equals(Relation.NULL)) {
					criteria.add(criterion);// 添加至criterion
					betweenCache = null;// 清除缓存
				}
			}
		}

	}


	private FinderByCriteriaDescriptor getFinderByCriteriaDescriptior(
			MethodInvocation invocation) {
		Method method = invocation.getMethod();
		
		FinderByCriteriaDescriptor finderByCriteriaDescriptor = finderByCriteriaCache
				.get(method);
		if (finderByCriteriaDescriptor != null) {
			return finderByCriteriaDescriptor;
		}
		finderByCriteriaDescriptor = new FinderByCriteriaDescriptor();
		finderByCriteriaDescriptor.returnClass = invocation.getMethod()
				.getReturnType();
		finderByCriteriaDescriptor.returnType = determineReturnType(finderByCriteriaDescriptor.returnClass);
		finderByCriteriaDescriptor.parameterTypes = method.getParameterTypes();
		FinderByCriteria finderByCriteria = method
				.getAnnotation(FinderByCriteria.class);
		finderByCriteriaDescriptor.finderByCriteria = finderByCriteria;
		finderByCriteriaDescriptor.entityClass = finderByCriteria.entity();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Object[] parameterAnnotations = new Object[annotationArray.length];
		if (ReturnType.LIST
				.equals(finderByCriteriaDescriptor.returnType)) {
			finderByCriteriaDescriptor.returnCollectionType = finderByCriteria
					.returnAs();
			try {
				finderByCriteriaDescriptor.returnCollectionTypeConstructor = finderByCriteriaDescriptor.returnCollectionType
						.getConstructor();
				finderByCriteriaDescriptor.returnCollectionTypeConstructor
						.setAccessible(true); // UGH!
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"Finder's collection return type specified has no default constructor! returnAs: "
								+ finderByCriteriaDescriptor.returnCollectionType,
						e);
			}
		}
		int index = 0;
		if (annotationArray != null && annotationArray.length > 0) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					parameterAnnotations[index] = annotation;
				}
				index++;
			}
			finderByCriteriaDescriptor.parameterAnnotations = parameterAnnotations;
			finderByCriteriaCache.put(method, finderByCriteriaDescriptor);
		}
		return finderByCriteriaDescriptor;
	}

	/**
	 * 将结果集构造成方法返回类型的Collection
	 * 
	 * @param finderByCriteriaDescriptor
	 * @param results
	 * @return 返回Collection对象
	 */
	@SuppressWarnings("unchecked")
	private Object getAsCollection(
			FinderByCriteriaDescriptor finderByCriteriaDescriptor,
			List results) {
		Collection<?> collection;
		try {
			collection = (Collection) finderByCriteriaDescriptor.returnCollectionTypeConstructor
					.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated: "
							+ finderByCriteriaDescriptor.returnCollectionType,
					e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (do not have access privileges): "
							+ finderByCriteriaDescriptor.returnCollectionType,
					e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (it threw an exception): "
							+ finderByCriteriaDescriptor.returnCollectionType,
					e);
		}

		collection.addAll(results);
		return collection;
	}

	private static class FinderByCriteriaDescriptor {
		Class<? extends IdEntity> entityClass;

		ReturnType returnType;

		Class<?> returnClass;

		Object[] parameterAnnotations;

		Class<?>[] parameterTypes;

		Class<? extends Collection> returnCollectionType;

		Constructor returnCollectionTypeConstructor;

		FinderByCriteria finderByCriteria;
	}

	


	private ReturnType determineReturnType(Class<?> returnClass) {
		if (Collection.class.isAssignableFrom(returnClass)) {
			return ReturnType.LIST;
		} else if (returnClass.isArray()) {
			return ReturnType.ARRAY;
		} else if (returnClass.equals(Page.class)) {
			return ReturnType.PAGE;
		}
		return ReturnType.PLAIN;
	}
}
