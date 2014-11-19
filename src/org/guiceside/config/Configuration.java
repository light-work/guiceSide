package org.guiceside.config;

import org.guiceside.commons.GlobalExceptionMapping;
import org.guiceside.commons.GlobalResult;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.persistence.PersistenceFlavor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 保存guiceSide.xml信息 JavaBean
 * </p>
 *
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class Configuration implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PersistenceFlavor persistenceFlavor;

    private String templete;

    private String templeteMode;

    private String freemarkerWrapper;

    private String freemarkerLoadPath;

    private String freemarkerUpdateDelay;

    private String freemarkerEncoding;

    private String freemarkerLocale;

    private String freemarkerNoCache;

    private String freemarkerContentType;

    private String freemarkerExceptionHandler;

    private String velocityConfigfile;

    private String velocityEncoding;

    private String velocityContentType;

    private String velocityLoaderPath;
    private String extension;

    private Set<String> ignoreParams;

    private Set<String> ignoreParamsKey;

    private Set<String> actionPackeages;

    private Set<String> hibernatePackeages;

    private List<GlobalResult> globalResults;

    private List<GlobalExceptionMapping> globalExceptionMappings;

    private List<AbstractInterceptorStrategy> interceptors;

    public PersistenceFlavor getPersistenceFlavor() {
        return persistenceFlavor;
    }

    public void setPersistenceFlavor(PersistenceFlavor persistenceFlavor) {
        this.persistenceFlavor = persistenceFlavor;
    }

    public Set<String> getActionPackeages() {
        return actionPackeages;
    }

    public void setActionPackeages(Set<String> actionPackeages) {
        this.actionPackeages = actionPackeages;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public List<GlobalResult> getGlobalResults() {
        return globalResults;
    }

    public void setGlobalResults(List<GlobalResult> globalResults) {
        this.globalResults = globalResults;
    }

    public List<GlobalExceptionMapping> getGlobalExceptionMappings() {
        return globalExceptionMappings;
    }

    public void setGlobalExceptionMappings(
            List<GlobalExceptionMapping> globalExceptionMappings) {
        this.globalExceptionMappings = globalExceptionMappings;
    }


    public Set<String> getHibernatePackeages() {
        return hibernatePackeages;
    }

    public void setHibernatePackeages(Set<String> hibernatePackeages) {
        this.hibernatePackeages = hibernatePackeages;
    }


    public List<AbstractInterceptorStrategy> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<AbstractInterceptorStrategy> interceptors) {
        this.interceptors = interceptors;
    }

    public String getTemplete() {
        return templete;
    }

    public void setTemplete(String templete) {
        this.templete = templete;
    }

    public String getTempleteMode() {
        return templeteMode;
    }

    public void setTempleteMode(String templeteMode) {
        this.templeteMode = templeteMode;
    }

    public String getFreemarkerLoadPath() {
        return freemarkerLoadPath;
    }

    public void setFreemarkerLoadPath(String freemarkerLoadPath) {
        this.freemarkerLoadPath = freemarkerLoadPath;
    }

    public String getFreemarkerUpdateDelay() {
        return freemarkerUpdateDelay;
    }

    public void setFreemarkerUpdateDelay(String freemarkerUpdateDelay) {
        this.freemarkerUpdateDelay = freemarkerUpdateDelay;
    }

    public String getFreemarkerEncoding() {
        return freemarkerEncoding;
    }

    public void setFreemarkerEncoding(String freemarkerEncoding) {
        this.freemarkerEncoding = freemarkerEncoding;
    }

    public String getFreemarkerLocale() {
        return freemarkerLocale;
    }

    public void setFreemarkerLocale(String freemarkerLocale) {
        this.freemarkerLocale = freemarkerLocale;
    }

    public String getFreemarkerNoCache() {
        return freemarkerNoCache;
    }

    public void setFreemarkerNoCache(String freemarkerNoCache) {
        this.freemarkerNoCache = freemarkerNoCache;
    }

    public String getFreemarkerContentType() {
        return freemarkerContentType;
    }

    public void setFreemarkerContentType(String freemarkerContentType) {
        this.freemarkerContentType = freemarkerContentType;
    }

    public String getFreemarkerWrapper() {
        return freemarkerWrapper;
    }

    public void setFreemarkerWrapper(String freemarkerWrapper) {
        this.freemarkerWrapper = freemarkerWrapper;
    }

    public String getVelocityConfigfile() {
        return velocityConfigfile;
    }

    public void setVelocityConfigfile(String velocityConfigfile) {
        this.velocityConfigfile = velocityConfigfile;
    }

    public String getVelocityEncoding() {
        return velocityEncoding;
    }

    public void setVelocityEncoding(String velocityEncoding) {
        this.velocityEncoding = velocityEncoding;
    }

    public String getVelocityContentType() {
        return velocityContentType;
    }

    public void setVelocityContentType(String velocityContentType) {
        this.velocityContentType = velocityContentType;
    }

    public String getVelocityLoaderPath() {
        return velocityLoaderPath;
    }

    public void setVelocityLoaderPath(String velocityLoaderPath) {
        this.velocityLoaderPath = velocityLoaderPath;
    }

    public String getFreemarkerExceptionHandler() {
        return freemarkerExceptionHandler;
    }

    public void setFreemarkerExceptionHandler(String freemarkerExceptionHandler) {
        this.freemarkerExceptionHandler = freemarkerExceptionHandler;
    }

    public Set<String> getIgnoreParams() {
        return ignoreParams;
    }

    public void setIgnoreParams(Set<String> ignoreParams) {
        this.ignoreParams = ignoreParams;
    }

    public Set<String> getIgnoreParamsKey() {
        return ignoreParamsKey;
    }

    public void setIgnoreParamsKey(Set<String> ignoreParamsKey) {
        this.ignoreParamsKey = ignoreParamsKey;
    }
}
