package fund.mingdao.web.support;


import fund.mingdao.common.UserInfo;
import fund.mingdao.common.UserSession;
import ognl.NoSuchPropertyException;
import ognl.OgnlException;
import org.guiceside.commons.Page;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.Tracker;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.lang.reflect.Field;
import java.util.*;


public abstract class ActionSupport<T> extends BaseAction {
    @ReqSet
    protected Page<T> pageObj;

    @ReqGet
    protected boolean _search;

    @ReqGet
    @ReqSet
    protected String attToken;


    protected Map<String, Class> searchTypeMapping;

    @ReqGet
    @ReqSet
    protected int rows = 10;

    @ReqGet
    @ReqSet
    protected int start = 0;

    @ReqGet
    @ReqSet
    protected int page = 0;

    @ReqGet
    protected boolean ignoreUse;

    @ReqGet
    protected String filters;

    @ReqGet
    protected String sort;

    @ReqGet
    protected String sidx;

    @ReqGet
    @ReqSet
    protected String sord;

    protected Set<String> aliasSet = new HashSet<String>();

    protected Set<String> opSet = new HashSet<String>();
    @ReqSet
    protected String script = "parent.reload();";

    @ReqSet
    protected String alertMessage;

    @ReqSet
    protected String alertTitle;

    protected String mdURI = "https://api.mingdao.com/auth2/authorize";

    private  final String CORP_ID="wx853769e334ead822";

    private  final String CORP_SECRET="17ab4955fe78a806a1a778c7adbd93d8";

    protected boolean isAsc() {
        if (StringUtils.isNotBlank(sord)) {
            sord = sord.toUpperCase();
            if (sord.equals("ASC")) {
                return true;
            } else if (sord.equals("DESC")) {
                return false;
            }
        }
        return false;
    }




    protected int getStart() {
        if (page > 0) {
            page = page - 1;
            start = page * rows;
        }
        return start;
    }

    protected String getOrderKey() {
        if (StringUtils.isNotBlank(sidx)) {
            if (sidx.indexOf(".") != -1) {
                String[] sidxs = sidx.split("\\.");
                if (sidxs.length == 2) {
                    return sidx;
                } else if (sidxs.length == 3) {
                    return sidxs[1] + "." + sidxs[2];
                }
            } else {
                return sidx;
            }
        }
        return null;
    }

    protected T copy(T fromEntity, T entity) throws Exception {
        BeanUtils.copyProperties(entity, fromEntity);
        fromEntity = null;
        return entity;
    }

    public static Object staticCopy(Object fromEntity, Object entity) throws Exception {
        BeanUtils.copyProperties(entity, fromEntity);
        fromEntity = null;
        return entity;
    }

    public static void staticBind(Object entity) throws Exception {
        if (entity instanceof Tracker) {
            BeanUtils.setValue(entity, "created", DateFormatUtil.getCurrentDate(true));
            BeanUtils.setValue(entity, "updated", DateFormatUtil.getCurrentDate(true));
        }
        try {
            String useYn = BeanUtils.getValue(entity, "useYn", String.class);
            if (StringUtils.isBlank(useYn)) {
                BeanUtils.setValue(entity, "useYn", "N");
            }
        } catch (NoSuchPropertyException e) {
            BeanUtils.setValue(entity, "useYn", "N");
        }
    }

    protected void bind(Object entity) throws Exception {
        if (entity instanceof Tracker) {
            if (BeanUtils.getValue(entity, "id") == null) {
                BeanUtils.setValue(entity, "created", DateFormatUtil.getCurrentDate(true));
            }
            UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
            BeanUtils.setValue(entity, "updated", DateFormatUtil.getCurrentDate(true));
            if (userInfo != null) {
                BeanUtils.setValue(entity, "createdBy", userInfo.getUserId());
                BeanUtils.setValue(entity, "updatedBy", userInfo.getUserId());
            }
        }
        try {
            String useYn = BeanUtils.getValue(entity, "useYn", String.class);
            if (StringUtils.isBlank(useYn)) {
                BeanUtils.setValue(entity, "useYn", "N");
            }
        } catch (NoSuchPropertyException e) {
            BeanUtils.setValue(entity, "useYn", "N");
        }
    }



    protected String get(Object entity, String property) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmpty(result);
    }

    protected String getDate(Object entity, String property, String f) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmptyByDate((Date) result, f);
    }

    protected <T> T get(Object entity, String property, Class<T> type) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        result = StringUtils.defaultIfEmpty(result);
        result = BeanUtils.convertValue(result, type);
        return (T) result;
    }


    public String getDate(Date blogDate) throws Exception {
        Date cud = DateFormatUtil.getCurrentDate(true);
        long diff = DateFormatUtil.calendarSecondPlus(blogDate, cud);
        String unit = "秒";
        if (diff > 60) {
            diff = DateFormatUtil.calendarMinutePlus(blogDate, cud);
            unit = "分";
            if (diff > 60) {
                diff = DateFormatUtil.calendarHourPlus(blogDate, cud);
                unit = "小时";
                if (diff > 24) {
                    diff = DateFormatUtil.calendarSecondPlus(blogDate, cud);
                    if (diff == 1) {
                        return "昨天" + DateFormatUtil.format(blogDate, DateFormatUtil.HOUR_MINUTE_SECOND_PATTERN);
                    } else if (diff == 2) {
                        return "前天" + DateFormatUtil.format(blogDate, DateFormatUtil.MDHM_PATTERN);
                    } else {
                        return DateFormatUtil.format(blogDate, DateFormatUtil.MDHM_PATTERN);
                    }

                }
            }
        } else {
            return "刚刚";
        }
        return diff + "" + unit + "以前";
    }

    protected   String[] filterIgnoreField(Object obj, String[] ignoresArray) throws Exception {
        if (obj == null) {
            return ignoresArray;
        }
        Set<String> ignoreSet = new HashSet<String>();
        if (ignoresArray != null && ignoresArray.length > 0) {
            ignoreSet.addAll(Arrays.asList(ignoresArray));
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("serialVersionUID")) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                ignoreSet.add(field.getName());
            }
        }
        String[] temp = new String[ignoreSet.size()];
        int index = 0;
        for (String field : ignoreSet) {
            temp[index++] = field;
        }
        return temp;
    }
}