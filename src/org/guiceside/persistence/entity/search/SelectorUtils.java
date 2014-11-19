package org.guiceside.persistence.entity.search;

import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.hibernate.dao.enums.Condition;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.enums.Relation;
import org.guiceside.persistence.hibernate.dao.enums.SelectorType;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-8
 * Time: 17:31:44
 * To change this template use File | Settings | File Templates.
 */
public class SelectorUtils {
    private final static boolean defaultIgnoreCase = false;
    private final static boolean defaultIgnoreBank = true;
    private final static Match defaultMatch = Match.ANYWHERE;

    private static boolean $validator(String column) {
        if (StringUtils.isBlank(column))
            return false;
        return true;
    }

    public static Selector $id(Serializable id) {
        return $createSelector(Condition.EQ, "id", id, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $id(String column, Serializable id) {
        return $createSelector(Condition.EQ, column, id, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * ***********************************eq********************************
     */
    public static Selector $eq(String column, Object value) {
        return $eq(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $eq(String column, Object value, boolean ignoreCase) {
        return $eq(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $eq(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.EQ, column, value, null, ignoreCase, ignoreBank, defaultMatch);
    }

    /**
     * ***********************************gt********************************
     */
    public static Selector $gt(String column, Object value) {
        return $gt(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $gt(String column, Object value, boolean ignoreCase) {
        return $gt(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $gt(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.GT, column, value, null, ignoreCase, ignoreBank, defaultMatch);
    }


    /**
     * ***********************************ge********************************
     */
    public static Selector $ge(String column, Object value) {
        return $ge(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $ge(String column, Object value, boolean ignoreCase) {
        return $ge(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $ge(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.GE, column, value, null, ignoreCase, ignoreBank, defaultMatch);
    }

    /**
     * ***********************************lt********************************
     */
    public static Selector $lt(String column, Object value) {
        return $lt(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $lt(String column, Object value, boolean ignoreCase) {
        return $lt(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $lt(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.LT, column, value, null, ignoreCase, ignoreBank, defaultMatch);
    }

    /**
     * ***********************************le********************************
     */
    public static Selector $le(String column, Object value) {
        return $le(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $le(String column, Object value, boolean ignoreCase) {
        return $le(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $le(String column, Object value, boolean ignoreCase, boolean ignoreBank) {
        return $createSelector(Condition.LE, column, value, null, ignoreCase, ignoreBank, defaultMatch);
    }

    /**
     * ***********************************like********************************
     */
    public static Selector $like(String column, Object value) {
        return $like(column, value, defaultIgnoreCase, defaultIgnoreBank);
    }

    public static Selector $like(String column, Object value, boolean ignoreCase) {
        return $like(column, value, ignoreCase, defaultIgnoreBank);
    }

    public static Selector $like(String column, Object value, Match match) {

        return $like(column, value, defaultIgnoreCase, defaultIgnoreBank, match);
    }

    public static Selector $like(String column, Object value, boolean ignoreCase, boolean ignoreBank) {

        return $like(column, value, ignoreCase, ignoreBank, defaultMatch);
    }

    public static Selector $like(String column, Object value, boolean ignoreCase, boolean ignoreBank, Match match) {

        return $createSelector(Condition.LIKE, column, value, null, ignoreCase, ignoreBank, match);
    }

    public static Selector $in(String column, List<?> inData) {
        return $createSelector(Condition.IN, column, inData, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $null(String column) {
        return $createSelector(Condition.NULL, column, null, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $notNull(String column) {
        return $createSelector(Condition.NOTNULL, column, null, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $empty(String column) {
        return $createSelector(Condition.EMPTY, column, null, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $notEmpty(String column) {
        return $createSelector(Condition.NOTEMPTY, column, null, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $between(String column, Object value, Object value1) {
        return $createSelector(Condition.BETWEEN, column, value, value1, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $not(Selector selector) {
        return $createSelector(selector);
    }

    public static Selector $concat(String column, Object value) {
        return $createSelector(Condition.CONCAT, column, value, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    public static Selector $sql(String sql) {
        return $createSelector(Condition.SQL, sql, null, null, defaultIgnoreCase, defaultIgnoreBank, defaultMatch);
    }

    /**
     * ***************************************or***************************
     */
    public static Selector $or(Selector... selectors) {
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
     * ***************************************or***************************
     */
    public static Selector $or(List<Selector> relationSelectors) {
        if (relationSelectors.size() < 2) {
            return null;
        }
        return $createSelector(Relation.OR, relationSelectors);
    }

    /**
     * ***************************************and***************************
     */
    public static Selector $and(Selector... selectors) {
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
     * ***************************************alias***************************
     */
    public static Selector $alias(String column, String alias) {
        Selector selector = new Selector();
        selector.setSelectorType(SelectorType.ALIAS);
        selector.setAliasColumn(column);
        selector.setAliasAs(alias);
        return selector;
    }

    /**
     * ***************************************order***************************
     */
    public static Selector $order(String column) {
        return $order(column, true);
    }

    public static Selector $order(String column, boolean asc) {
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
     * ***************************************count***************************
     */
    public static Selector $count(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.count(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }


    /**
     * ***************************************countDistinct***************************
     */
    public static Selector $countDistinct(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.countDistinct(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    /**
     * ***************************************avg***************************
     */
    public static Selector $avg(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.avg(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    /**
     * ***************************************sum***************************
     */
    public static Selector $sum(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.sum(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    /**
     * ***************************************min***************************
     */
    public static Selector $min(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.min(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    /**
     * ***************************************max***************************
     */
    public static Selector $max(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.max(column);
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    /**
     * ***************************************distinct***************************
     */
    public static Selector $distinct(String column) {
        Selector selector = new Selector();
        Projection projection = Projections.distinct(Projections.property(column));
        selector.setSelectorType(SelectorType.PROJECTION);
        selector.setProjection(projection);
        return selector;
    }

    private static Selector $createSelector(Relation relation, List<Selector> selectors) {
        Selector selector = new Selector();
        Criterion criterion = sequenceRelation(relation, selectors, null);
        selector.setCriterion(criterion);
        return selector;
    }

    private static Selector $createSelector(Selector selectors) {
        Selector selector = new Selector();
        Criterion criterion = Restrictions.not(selectors.getCriterion());
        selector.setCriterion(criterion);
        return selector;
    }

    private static Criterion sequenceRelation(Relation relation, List<Selector> selectors, Criterion returnCriterion) {
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

    private static Selector $createSelector(Condition condition, String column, Object value, Object value1, boolean ignoreCase, boolean ignoreBank, Match match) {
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
            case BETWEEN:
                criterion = Restrictions.between(column, value, value1);
            case CONCAT:
                criterion = Restrictions.sqlRestriction(" '" + value.toString() + "' LIKE CONCAT(" + column + ",'%')");
            case SQL:
                criterion = Restrictions.sqlRestriction(column);
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

    private static Criterion $isIgnoreCase(SimpleExpression simpleExpression, boolean ignoreCase) {
        return ignoreCase ? simpleExpression.ignoreCase() : simpleExpression;
    }
}
