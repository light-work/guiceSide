package org.guiceside.persistence.hibernate.dao.hquery;

import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.hibernate.dao.enums.Condition;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.enums.Relation;
import org.guiceside.persistence.hibernate.dao.enums.SelectorType;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-1
 * Time: 16:16:28
 * To change this template use File | Settings | File Templates.
 */
public class HQuerySelector extends HQuerySupport {
    private final static boolean defaultIgnoreCase = false;
    private final static boolean defaultIgnoreBank = true;
    private final static Match defaultMatch = Match.ANYWHERE;

    private boolean $validator(String column) {
        if (StringUtils.isBlank(column))
            return false;
        return true;
    }

    /**
     * 通过ID进行查询 ID必须为可序列化的值 默认ID名称为"id"
     * @param id
     * @return
     */
    public Selector $id(Serializable id) {
        return $createSelector(Condition.EQ, "id", id, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 通过给定的id列名以及ID进行查询 ID必须为可序列化的值
     * @param column
     * @param id
     * @return
     */
    public Selector $id(String column, Serializable id) {
        return $createSelector(Condition.EQ, column, id, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }


    /**
     * 构建相等
     * @param column
     * @param value
     * @return
     */
    public Selector $eq(String column, Object value) {
        return $eq(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建相等
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $eq(String column, Object value, boolean ignoreCase) {
        return $eq(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建相等
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @return
     */
    public Selector $eq(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.EQ, column, value, ignoreCase, ignoreBank, defaultMatch);
    }


    /**
     * 构建大于
     * @param column
     * @param value
     * @return
     */
    public Selector $gt(String column, Object value) {
        return $gt(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建大于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $gt(String column, Object value, boolean ignoreCase) {
        return $gt(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建大于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略非空
     * @return
     */
    public Selector $gt(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.GT, column, value, ignoreCase, ignoreBank, defaultMatch);
    }



    /**
     * 构建大于等于
     * @param column
     * @param value
     * @return
     */
    public Selector $ge(String column, Object value) {
        return $ge(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建大于等于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $ge(String column, Object value, boolean ignoreCase) {
        return $ge(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建大于等于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @return
     */
    public Selector $ge(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.GE, column, value, ignoreCase, ignoreBank, defaultMatch);
    }


    /**
     * 构建小于
     * @param column
     * @param value
     * @return
     */
    public Selector $lt(String column, Object value) {
        return $lt(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建小于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $lt(String column, Object value, boolean ignoreCase) {
        return $lt(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建小于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @return
     */
    public Selector $lt(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.LT, column, value, ignoreCase, ignoreBank, defaultMatch);
    }


    /**
     * 构建小于等于
     * @param column
     * @param value
     * @return
     */
    public Selector $le(String column, Object value) {
        return $le(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建小于等于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $le(String column, Object value, boolean ignoreCase) {
        return $le(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建小于等于
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @return
     */
    public Selector $le(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.LE, column, value, ignoreCase, ignoreBank, defaultMatch);
    }


    /**
     * 构建模糊查询
     * @param column
     * @param value
     * @return
     */
    public Selector $like(String column, Object value) {
        return $like(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    /**
     * 构建模糊查询
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public Selector $like(String column, Object value, boolean ignoreCase) {
        return $like(column, value, ignoreCase, defaultIgnoreBank);
    }

    /**
     * 构建模糊查询
     * @param column
     * @param value
     * @param match 匹配方式
     * @return
     */
    public Selector $like(String column, Object value, Match match) {

        return $like(column, value, defaultIgnoreCase, defaultIgnoreBank, match);
    }

    /**
     * 构建模糊查询
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @return
     */
    public Selector $like(String column, Object value, boolean ignoreCase, boolean ignoreBank) {

        return $like(column, value, ignoreCase, ignoreBank, defaultMatch);
    }

    /**
     * 构建模糊查询
     * @param column
     * @param value
     * @param ignoreCase 是否忽略大小写
     * @param ignoreBank 是否忽略空值
     * @param match 匹配关系
     * @return
     */
    public Selector $like(String column, Object value, boolean ignoreCase, boolean ignoreBank, Match match) {

        return $createSelector(Condition.LIKE, column, value, ignoreCase, ignoreBank, match);
    }

    /**
     * 构建IN
     * @param column
     * @param inData
     * @return
     */
    public Selector $in(String column, List<?> inData) {
        return $createSelector(Condition.IN, column, inData, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 构建空
     * @param column
     * @return
     */
    public Selector $null(String column) {
        return $createSelector(Condition.NULL, column, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 构建非空
     * @param column
     * @return
     */
    public Selector $notNull(String column) {
        return $createSelector(Condition.NOTNULL, column, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 构建空字符串
     * @param column
     * @return
     */
    public Selector $empty(String column) {
        return $createSelector(Condition.EMPTY, column, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 构建非空字符串
     * @param column
     * @return
     */
    public Selector $notEmpty(String column) {
        return $createSelector(Condition.NOTEMPTY, column, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * 构建非
     * @param selector
     * @return
     */
    public Selector $not(Selector selector) {
        return $createSelector(selector);
    }


    /**
     * 构建OR
     * @param selectors
     * @return
     */
    public Selector $or(Selector... selectors) {
        List<Selector> relationSelectors = new ArrayList<Selector>();
        for (Selector selector : selectors) {
            if (selector == null)
                continue;
            relationSelectors.add(selector);
        }
        if (relationSelectors.size() < 2) {
            return null;
        }
        return $createSelector(Relation.OR, relationSelectors);
    }


    /**
     * 构建AND
     * @param selectors
     * @return
     */
    public Selector $and(Selector... selectors) {
        List<Selector> relationSelectors = new ArrayList<Selector>();
        for (Selector selector : selectors) {
            if (selector == null)
                continue;
            relationSelectors.add(selector);
        }
        if (relationSelectors.size() < 2) {
            return null;
        }
        return $createSelector(Relation.AND, relationSelectors);
    }



    /**
     * 构建别名
     * @param column
     * @param alias
     * @return
     */
    public Selector $alias(String column, String alias) {
        Selector selector = new Selector();
        selector.setSelectorType(SelectorType.ALIAS);
        selector.setAliasColumn(column);
        selector.setAliasAs(alias);
        return selector;
    }


    /**
     * 构建排序 默认ASC
     * @param column
     * @return
     */
    public Selector $order(String column) {
        return $order(column, true);
    }

    /**
     * 构建排序
     * @param column
     * @param asc 是否ASC
     * @return
     */
    public Selector $order(String column, boolean asc) {
        Selector selector = new Selector();
        Order order = null;
        if (asc) {
            order = Order.asc(column);
        } else {
            order = Order.desc(column);
        }
        selector.setSelectorType(SelectorType.ORDER);
        selector.setOrder(order);
        return selector;
    }



    /**
     * 构建COUNT
     * @param column
     * @return
     */
    public Selector $count(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.count(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }



    /**
     * 构建countDistinct
     * @param column
     * @return
     */
    public Selector $countDistinct(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.countDistinct(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }


    /**
     * 构建AVG
     * @param column
     * @return
     */
    public Selector $avg(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.avg(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }


    /**
     * 构建SUM
     * @param column
     * @return
     */
    public Selector $sum(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.sum(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }


    /**
     * 构建MIN
     * @param column
     * @return
     */
    public Selector $min(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.min(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }


    /**
     * 构建MAX
     * @param column
     * @return
     */
    public Selector $max(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.max(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    
    /**
     * 构建distinct
     * @param column
     * @return
     */
    public Selector $distinct(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.distinct(Projections.property(column));
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    
    private Selector $createSelector(Relation relation, List<Selector> selectors) {
        Selector selector = new Selector();
        Criterion criterion = sequenceRelation(relation, selectors, null);
        selector.setCriterion(criterion);
        return selector;
    }

    private Selector $createSelector(Selector selectors) {
        Selector selector = new Selector();
        Criterion criterion = Restrictions.not(selectors.getCriterion());
        selector.setCriterion(criterion);
        return selector;
    }

    private Criterion sequenceRelation(Relation relation, List<Selector> selectors, Criterion returnCriterion) {
        if (selectors != null && selectors.isEmpty()) {
            //此处如果成立 则表示returnCriterion不为NULL 至少被回调一次
            return returnCriterion;
        }
        if (selectors.size() == 2) {
            Selector orSelector1 = selectors.get(0);
            Selector orSelector2 = selectors.get(1);
            switch (relation) {
                case OR:
                    returnCriterion = Restrictions.or(orSelector1.getCriterion(), orSelector2.getCriterion());
                    break;
                case AND:
                    returnCriterion = Restrictions.and(orSelector1.getCriterion(), orSelector2.getCriterion());
                    break;
            }
            return returnCriterion;
        }
        List<Selector> sequenceSelectors = new ArrayList<Selector>();

        int i = 0;
        for (Selector selector : selectors) {
            if (sequenceSelectors.size() == 2) {//满足匹配数目 并且存在比2还大的OR关系
                Selector newSelector = new Selector();//创建新的Selector存放新的or匹配
                Selector orSelector1 = sequenceSelectors.get(0);
                Selector orSelector2 = sequenceSelectors.get(1);
                Criterion criterion = null;
                switch (relation) {
                    case OR:
                        criterion = Restrictions.or(orSelector1.getCriterion(), orSelector2.getCriterion());
                        break;
                    case AND:
                        criterion = Restrictions.and(orSelector1.getCriterion(), orSelector2.getCriterion());
                        break;
                }
                newSelector.setCriterion(criterion);
                /**
                 * 补齐前后Selector 并把当前的newSelector放置何时的位置
                 */
                List<Selector> newSelectors = new ArrayList<Selector>();
                int current = i - 2;
                for (int n = 0; n < current; n++) {
                    newSelectors.add(selectors.get(n));
                }
                newSelectors.add(newSelector);
                for (int n = i; n < selectors.size(); n++) {
                    newSelectors.add(selectors.get(n));
                }
                /**
                 * 进行回调
                 */
                return sequenceRelation(relation, newSelectors, criterion);
            }
            sequenceSelectors.add(selector);//将当前Selector添加
            i++;
        }
        return returnCriterion;
    }

    private Selector $createSelector(Condition condition, String column, Object value, boolean ignoreCase, boolean ignoreBank, Match match) {
        if (!$validator(column))
            return null;
        if (value == null) {
            if (condition == Condition.NULL || condition == Condition.NOTNULL || condition == Condition.EMPTY || condition == Condition.NOTEMPTY) {
                
            } else {
                if (ignoreBank) {
                    return null;
                } else {
                    return null;
                }
            }
        }
        Selector selector = new Selector();
        Criterion criterion = null;
        switch (condition) {
            case EQ:
                criterion = Restrictions.eq(column, value);
                break;
            case GT:
                criterion = Restrictions.gt(column, value);
                break;
            case GE:
                criterion = Restrictions.ge(column, value);
                break;
            case LT:
                criterion = Restrictions.lt(column, value);
                break;
            case LE:
                criterion = Restrictions.le(column, value);
                break;
            case LIKE:
                MatchMode mode = null;
                switch (match) {
                    case START:
                        mode = MatchMode.START;
                        break;
                    case END:
                        mode = MatchMode.END;
                    case ANYWHERE:
                        mode = MatchMode.ANYWHERE;
                        break;
                }
                criterion = Restrictions.like(column, value.toString(), mode);
                break;
            case IN:
                Object[] arrayObject = null;
                if (value instanceof Collection<?>) {
                    Collection<?> collection = (Collection<?>) value;
                    arrayObject = collection.toArray();
                } else if (value instanceof Object[]) {
                    arrayObject = (Object[]) value;
                }
                criterion = Restrictions.in(column, arrayObject);
                break;
            case NULL:
                criterion = Restrictions.isNull(column);
                break;
            case NOTNULL:
                criterion = Restrictions.isNotNull(column);
                break;
            case EMPTY:
                criterion = Restrictions.isEmpty(column);
                break;
            case NOTEMPTY:
                criterion = Restrictions.isNotEmpty(column);
                break;
        }
        if (criterion != null) {
            try {
                criterion = $isIgnoreCase((SimpleExpression) criterion, ignoreCase);
            } catch (ClassCastException e) {

            }
        }
        selector.setCriterion(criterion);
        return selector;
    }

    private Criterion $isIgnoreCase(SimpleExpression simpleExpression, boolean ignoreCase) {
        return ignoreCase ? simpleExpression.ignoreCase() : simpleExpression;
    }
}
