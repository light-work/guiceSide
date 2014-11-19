package org.guiceside.config;

import org.apache.commons.lang.ClassUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.guiceside.commons.GlobalExceptionMapping;
import org.guiceside.commons.GlobalResult;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.persistence.PersistenceFlavor;
import org.guiceside.web.annotation.Dispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 通过dom4j读取GuiceSideConfiguration.xml文件
 * </p>
 *
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class ConfigurationManager {


    private static ConfigurationManager configurationManager;

    private SAXReader saxReader;

    private Document document;

    public static void initialize(String fileName) {
        synchronized (ConfigurationManager.class) {
            if (configurationManager == null) {
                try {
                    configurationManager = new ConfigurationManager(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ConfigurationManager getInstance() {
        return configurationManager;
    }

    private ConfigurationManager() throws Exception {
        saxReader = new SAXReader();
        InputStream is = this.getClass().getResourceAsStream("/guiceSide.xml");
        try {
            document = saxReader.read(is);
        } catch (DocumentException e) {
            throw new IOException("guiceSide.xml 没有正确加载,请检查文件位置是否正确");
        }
    }

    private ConfigurationManager(String fileName) throws IOException {
        saxReader = new SAXReader();
        InputStream is = this.getClass().getResourceAsStream("/" + fileName);
        try {
            document = saxReader.read(is);
        } catch (DocumentException e) {
            throw new IOException(fileName + " 没有正确加载,请检查文件位置是否正确");
        }
    }

    public Configuration loadConfig() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setPersistenceFlavor(getPersistence());
        configuration.setActionPackeages(getActionPackages());
        configuration.setHibernatePackeages(getHibernatePackages());
        configuration.setInterceptors(getInterceptors());
        configuration.setExtension(getActionExtension());
        configuration.setGlobalResults(getGlobalResults());
        configuration.setGlobalExceptionMappings(getGlobalExceptionMapping());
        configuration.setIgnoreParams(getIgnoreParams());
        configuration.setIgnoreParamsKey(getIgnoreParamsKey());
        /* view Templete*/
        configuration.setTemplete(getTemplete());
        if (StringUtils.isNotBlank(configuration.getTemplete())) {
            if (configuration.getTemplete().toLowerCase().equals("true")) {
                configuration.setTempleteMode(getTempleteMode());
                if (StringUtils.isNotBlank(configuration.getTempleteMode())) {
                    if (configuration.getTempleteMode().toLowerCase().equals("freemarker")) {
                        configuration.setFreemarkerContentType(getFreeMarkerContentType());
                        configuration.setFreemarkerEncoding(getFreeMarkerEncoding());
                        configuration.setFreemarkerLoadPath(getFreeMarkerLoadPath());
                        configuration.setFreemarkerLocale(getFreeMarkerLocale());
                        configuration.setFreemarkerNoCache(getFreeMarkerNoCache());
                        configuration.setFreemarkerUpdateDelay(getFreeMarkerUpdateDelay());
                        configuration.setFreemarkerWrapper(getFreeMarkerWrapper());
                        configuration.setFreemarkerExceptionHandler(getFreeMarkerExceptionHandler());
                    } else if (configuration.getTempleteMode().toLowerCase().equals("velocity")) {
                        configuration.setVelocityConfigfile(getVelocityConfigFile());
                        configuration.setVelocityContentType(getVelocityContentType());
                        configuration.setVelocityEncoding(getVelocityEncoding());
                        configuration.setVelocityLoaderPath(getVelocityLoaderPath());
                    }
                }
            }
        }
        return configuration;
    }


    private String getActionExtension() {
        Element actionExtension = (Element) document
                .selectObject("/guiceSide-configuration/action/extension");
        if (actionExtension != null) {
            if (StringUtils.isNotBlank(actionExtension.getTextTrim())) {
                return actionExtension.getTextTrim();
            }
        }

        return "action";
    }


    private PersistenceFlavor getPersistence() {
        Element provider = (Element) document
                .selectObject("/guiceSide-configuration/persistence/provider");
        if (provider != null) {
            if (StringUtils.isNotBlank(provider.getTextTrim())) {
                if (provider.getTextTrim().toLowerCase().equals("hibernate")) {
                    return PersistenceFlavor.HIBERNATE;
                } else if (provider.getTextTrim().toLowerCase()
                        .equals("jpa")) {
                    return PersistenceFlavor.JPA;
                } else if (provider.getTextTrim().toLowerCase().equals(
                        "ibatis")) {
                    return PersistenceFlavor.IBATIS;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<AbstractInterceptorStrategy> getInterceptors() throws Exception {
        List<Element> interceptorList = document
                .selectNodes("/guiceSide-configuration/action/interceptor/module/value");
        List<AbstractInterceptorStrategy> interceptors = null;
        if (interceptorList != null && !interceptorList.isEmpty()) {
            interceptors = new ArrayList<AbstractInterceptorStrategy>();
            for (Element el : interceptorList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    Class iclass = ClassUtils.getClass(el.getTextTrim());
                    AbstractInterceptorStrategy iObj = (AbstractInterceptorStrategy) iclass.newInstance();
                    interceptors.add(iObj);
                }
            }
        }
        return interceptors;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getHibernatePackages() throws Exception {
        List<Element> hibernatePackeageList = document
                .selectNodes("/guiceSide-configuration/persistence/hibernate/packages/value");
        Set<String> hibernatePackages = null;
        if (hibernatePackeageList != null && !hibernatePackeageList.isEmpty()) {
            hibernatePackages = new HashSet<String>();
            for (Element el : hibernatePackeageList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    hibernatePackages.add(el.getTextTrim());
                }
            }
        }
        return hibernatePackages;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getActionPackages() throws Exception {
        List<Element> actionPackeageList = document
                .selectNodes("/guiceSide-configuration/action/packages/value");
        Set<String> actionPackages = null;
        if (actionPackeageList != null && actionPackeageList.size() > 0) {
            actionPackages = new HashSet<String>();
            for (Element el : actionPackeageList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    actionPackages.add(el.getTextTrim());
                }
            }
        }
        return actionPackages;
    }


    @SuppressWarnings("unchecked")
    private List<String> getGlobalResultsName() throws Exception {
        List<Attribute> globalResultNameList = document
                .selectNodes("/guiceSide-configuration/action/global-results/result/@name");
        List<String> globalResultNames = null;
        if (globalResultNameList != null && globalResultNameList.size() > 0) {
            globalResultNames = new ArrayList<String>();
            for (Attribute att : globalResultNameList) {
                globalResultNames.add(att.getValue());
            }
        }
        return globalResultNames;
    }

    private Dispatcher getDispatcher(String key) {
        if (StringUtils.isBlank(key)) {
            return Dispatcher.Forward;
        }
        if (key.toLowerCase().equals(
                Dispatcher.Forward.toString().toLowerCase())) {
            return Dispatcher.Forward;
        } else if (key.toLowerCase().equals(
                Dispatcher.Redirect.toString().toLowerCase())) {
            return Dispatcher.Redirect;
        } else if (key.toLowerCase().equals(
                Dispatcher.FreeMarker.toString().toLowerCase())) {
            return Dispatcher.FreeMarker;
        }
        return Dispatcher.Forward;

    }

    @SuppressWarnings("unchecked")
    private List<Dispatcher> getGlobalResultsType() throws Exception {
        List<Attribute> globalResultTypeList = document
                .selectNodes("/guiceSide-configuration/action/global-results/result/@type");
        List<Dispatcher> globalResultType = null;
        if (globalResultTypeList != null && globalResultTypeList.size() > 0) {
            globalResultType = new ArrayList<Dispatcher>();
            for (Attribute att : globalResultTypeList) {
                globalResultType.add(getDispatcher(att.getValue()));
            }
        }
        return globalResultType;
    }

    @SuppressWarnings("unchecked")
    private List<String> getGlobalResultsPath() throws Exception {
        List<Element> globalResultPathList = document
                .selectNodes("/guiceSide-configuration/action/global-results/result");
        List<String> globalResultPaths = null;
        if (globalResultPathList != null && globalResultPathList.size() > 0) {
            globalResultPaths = new ArrayList<String>();
            for (Element el : globalResultPathList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    globalResultPaths.add(el.getTextTrim());
                }
            }
        }
        return globalResultPaths;
    }

    private List<GlobalResult> getGlobalResults() throws Exception {
        List<String> globalResultNames = getGlobalResultsName();
        List<Dispatcher> globalResultTypes = getGlobalResultsType();
        List<String> globalResultPaths = getGlobalResultsPath();
        List<GlobalResult> globalResults = null;
        GlobalResult globalResult = null;
        if (globalResultNames != null && globalResultTypes != null
                && globalResultPaths != null) {
            if (globalResultNames.size() == globalResultTypes.size()
                    && globalResultTypes.size() == globalResultPaths.size()) {
                globalResults = new ArrayList<GlobalResult>();
                for (int i = 0; i < globalResultPaths.size(); i++) {
                    globalResult = new GlobalResult();
                    globalResult.setName(globalResultNames.get(i));
                    globalResult.setType(globalResultTypes.get(i));
                    globalResult.setPath(globalResultPaths.get(i));
                    globalResults.add(globalResult);
                }
            }
        }
        return globalResults;
    }

    private List<GlobalExceptionMapping> getGlobalExceptionMapping()
            throws Exception {
        List<Class<? super Exception>> exceptions = getGlobalException();
        List<String> exceptionResults = getGlobalExceptionResult();
        List<GlobalExceptionMapping> globalExceptionMappings = null;
        GlobalExceptionMapping globalExceptionMapping = null;
        if (exceptions != null && exceptionResults != null) {
            if (exceptions.size() == exceptionResults.size()) {
                globalExceptionMappings = new ArrayList<GlobalExceptionMapping>();
                for (int i = 0; i < exceptionResults.size(); i++) {
                    globalExceptionMapping = new GlobalExceptionMapping();
                    globalExceptionMapping.setException(exceptions.get(i));
                    globalExceptionMapping.setResult(exceptionResults.get(i));
                    globalExceptionMappings.add(globalExceptionMapping);
                }
            }
        }
        return globalExceptionMappings;
    }

    @SuppressWarnings("unchecked")
    private List<Class<? super Exception>> getGlobalException()
            throws Exception {
        List<Attribute> globalExceptionList = document
                .selectNodes("/guiceSide-configuration/action/global-exception-mappings/exception-mapping/@exception");
        List<Class<? super Exception>> globalExceptions = null;
        if (globalExceptionList != null && globalExceptionList.size() > 0) {
            globalExceptions = new ArrayList<Class<? super Exception>>();
            for (Attribute att : globalExceptionList) {
                if (StringUtils.isNotBlank(att.getValue())) {
                    globalExceptions.add((Class<? super Exception>) Class
                            .forName(att.getValue()));
                }
            }
        }
        return globalExceptions;
    }

    @SuppressWarnings("unchecked")
    private List<String> getGlobalExceptionResult() throws Exception {
        List<Attribute> globalExceptionResultList = document
                .selectNodes("/guiceSide-configuration/action/global-exception-mappings/exception-mapping/@result");
        List<String> globalExceptionResults = null;
        if (globalExceptionResultList != null
                && globalExceptionResultList.size() > 0) {
            globalExceptionResults = new ArrayList<String>();
            for (Attribute att : globalExceptionResultList) {
                if (StringUtils.isNotBlank(att.getValue())) {
                    globalExceptionResults.add(att.getValue());
                }
            }
        }
        return globalExceptionResults;
    }


    private String getTemplete() throws Exception {
        Element templete = (Element) document
                .selectObject("/guiceSide-configuration/action/view/templete");
        String templeteYn = null;
        if (templete != null) {
            if (StringUtils.isNotBlank(templete.getTextTrim())) {
                templeteYn = templete.getTextTrim();
            }
        }
        return templeteYn;
    }

    private String getTempleteMode() throws Exception {
        Element templeteMode = (Element) document
                .selectObject("/guiceSide-configuration/action/view/templete-mode");
        String mode = null;
        if (templeteMode != null) {
            if (StringUtils.isNotBlank(templeteMode.getTextTrim())) {
                mode = templeteMode.getTextTrim();
            }
        }
        return mode;
    }

    private String getFreeMarkerLoadPath() throws Exception {
        Element freemarkerLoadPath = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-loadPath");
        String loadPath = null;
        if (freemarkerLoadPath != null) {
            if (StringUtils.isNotBlank(freemarkerLoadPath.getTextTrim())) {
                loadPath = freemarkerLoadPath.getTextTrim();
            }
        }
        return loadPath;
    }

    private String getFreeMarkerUpdateDelay() throws Exception {
        Element freemarkerUpdateDelay = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-update-delay");
        String updateDelay = null;
        if (freemarkerUpdateDelay != null) {
            if (StringUtils.isNotBlank(freemarkerUpdateDelay.getTextTrim())) {
                updateDelay = freemarkerUpdateDelay.getTextTrim();
            }
        }
        return updateDelay;
    }

    private String getFreeMarkerEncoding() throws Exception {
        Element freemarkerEncoding = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-encoding");
        String encoding = null;
        if (freemarkerEncoding != null) {
            if (StringUtils.isNotBlank(freemarkerEncoding.getTextTrim())) {
                encoding = freemarkerEncoding.getTextTrim();
            }
        }
        return encoding;
    }

    private String getFreeMarkerLocale() throws Exception {
        Element freemarkerLocale = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-locale");
        String locale = null;
        if (freemarkerLocale != null) {
            if (StringUtils.isNotBlank(freemarkerLocale.getTextTrim())) {
                locale = freemarkerLocale.getTextTrim();
            }
        }
        return locale;
    }

    private String getFreeMarkerNoCache() throws Exception {
        Element freemarkerNoCache = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-nocache");
        String nocache = null;
        if (freemarkerNoCache != null) {
            if (StringUtils.isNotBlank(freemarkerNoCache.getTextTrim())) {
                nocache = freemarkerNoCache.getTextTrim();
            }
        }
        return nocache;
    }

    private String getFreeMarkerContentType() throws Exception {
        Element freemarkerContentType = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-contentType");
        String contentType = null;
        if (freemarkerContentType != null) {
            if (StringUtils.isNotBlank(freemarkerContentType.getTextTrim())) {
                contentType = freemarkerContentType.getTextTrim();
            }
        }
        return contentType;
    }

    private String getFreeMarkerWrapper() throws Exception {
        Element freemarkerWrapper = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-wrapper");
        String wrapper = null;
        if (freemarkerWrapper != null) {
            if (StringUtils.isNotBlank(freemarkerWrapper.getTextTrim())) {
                wrapper = freemarkerWrapper.getTextTrim();
            }
        }
        return wrapper;
    }

    private String getFreeMarkerExceptionHandler() throws Exception {
        Element freemarkerExceptionHandler = (Element) document
                .selectObject("/guiceSide-configuration/action/view/freemarker/freemarker-exceptionHandler");
        String exceptionHandler = null;
        if (freemarkerExceptionHandler != null) {
            if (StringUtils.isNotBlank(freemarkerExceptionHandler.getTextTrim())) {
                exceptionHandler = freemarkerExceptionHandler.getTextTrim();
            }
        }
        return exceptionHandler;
    }

    private String getVelocityConfigFile() throws Exception {
        Element velocityConfigFile = (Element) document
                .selectObject("/guiceSide-configuration/action/view/velocity/velocity-configfile");
        String configFile = null;
        if (velocityConfigFile != null) {
            if (StringUtils.isNotBlank(velocityConfigFile.getTextTrim())) {
                configFile = velocityConfigFile.getTextTrim();
            }
        }
        return configFile;
    }

    private String getVelocityEncoding() throws Exception {
        Element velocityEncoding = (Element) document
                .selectObject("/guiceSide-configuration/action/view/velocity/velocity-encoding");
        String encoding = null;
        if (velocityEncoding != null) {
            if (StringUtils.isNotBlank(velocityEncoding.getTextTrim())) {
                encoding = velocityEncoding.getTextTrim();
            }
        }
        return encoding;
    }

    private String getVelocityContentType() throws Exception {
        Element velocityContentType = (Element) document
                .selectObject("/guiceSide-configuration/action/view/velocity/velocity-contentType");
        String contentType = null;
        if (velocityContentType != null) {
            if (StringUtils.isNotBlank(velocityContentType.getTextTrim())) {
                contentType = velocityContentType.getTextTrim();
            }
        }
        return contentType;
    }

    private String getVelocityLoaderPath() throws Exception {
        Element velocityLoaderPathType = (Element) document
                .selectObject("/guiceSide-configuration/action/view/velocity/velocity-loaderPath");
        String loaderPathType = null;
        if (velocityLoaderPathType != null) {
            if (StringUtils.isNotBlank(velocityLoaderPathType.getTextTrim())) {
                loaderPathType = velocityLoaderPathType.getTextTrim();
            }
        }
        return loaderPathType;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getIgnoreParams() throws Exception {
        List<Element> ignoreParamList = document
                .selectNodes("/guiceSide-configuration/action/url/ignore-params/pattern");
        Set<String> ignoreParams = null;
        if (ignoreParamList != null && ignoreParamList.size() > 0) {
            ignoreParams = new HashSet<String>();
            for (Element el : ignoreParamList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    ignoreParams.add(el.getTextTrim());
                }
            }
        }
        return ignoreParams;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getIgnoreParamsKey() throws Exception {
        List<Element> ignoreParamKeyList = document
                .selectNodes("/guiceSide-configuration/action/url/ignore-params-key/pattern");
        Set<String> ignoreParamKeys = null;
        if (ignoreParamKeyList != null && ignoreParamKeyList.size() > 0) {
            ignoreParamKeys = new HashSet<String>();
            for (Element el : ignoreParamKeyList) {
                if (StringUtils.isNotBlank(el.getTextTrim())) {
                    ignoreParamKeys.add(el.getTextTrim());
                }
            }
        }
        return ignoreParamKeys;
    }
}
