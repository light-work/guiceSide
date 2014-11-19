package org.guiceside.web.view.velocity;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.config.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-4
 * Time: 22:23:24
 * To change this template use File | Settings | File Templates.
 */
public class VelocityManager {

    private static final Logger log = Logger.getLogger(VelocityManager.class);
    private static VelocityManager velocityManager = null;
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_CONTENT_TYPE = "text/html";


    private VelocityEngine velocityEngine;

    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String SESSION = "session";

    public synchronized static VelocityManager getInstance() {
        if (velocityManager == null) {
            velocityManager = new VelocityManager();
        }
        return velocityManager;
    }

    public synchronized void init(ServletContext context, Configuration configuration) {
        if (velocityEngine == null) {
            velocityEngine = newVelocityEngine(context, configuration);
        }
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    protected VelocityEngine newVelocityEngine(ServletContext servletContext, Configuration configuration) {
        if (servletContext == null) {
            String gripe = "Error attempting to create a new VelocityEngine from a null ServletContext!";
            log.error(gripe);
            throw new IllegalArgumentException(gripe);
        }
        Properties p=new Properties();
        //Properties p = loadConfiguration(configuration, servletContext);
        p.setProperty(Velocity.RESOURCE_LOADER, "file, class");
        String servetPath = servletContext.getRealPath(File.separator);
        if (StringUtils.isNotBlank(servetPath)) {
            p.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            p.setProperty("file.resource.loader.path", servetPath);
            p.setProperty("file.resource.loader.modificationCheckInterval", "2");
            p.setProperty("file.resource.loader.cache", "true");
        }

        p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        p.setProperty("class.resource.loader.class", "org.guiceside.web.view.velocity.VelocityResourceLoader");
        p.setProperty("class.resource.loader.modificationCheckInterval", "2");
        p.setProperty("class.resource.loader.cache", "true");
        VelocityEngine velocityEngine = new VelocityEngine();
        String encoding=configuration.getVelocityEncoding();
        if(StringUtils.isBlank(encoding)){
            encoding=DEFAULT_ENCODING;
        }
        p.setProperty("input.encoding", encoding);
        p.setProperty("output.encoding",encoding);
        //	Set the velocity attribute for the servlet servletContext
        //  if this is not set the webapp loader WILL NOT WORK
        velocityEngine.setApplicationAttribute(ServletContext.class.getName(),
                servletContext);
        //p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH,servletContext.getRealPath("/"));
        try {
            velocityEngine.init(p);
        } catch (Exception e) {
            String gripe = "Unable to instantiate VelocityEngine!";
            log.error(gripe, e);
            throw new IllegalArgumentException(gripe);
        }

        return velocityEngine;
    }

    public Properties loadConfiguration(Configuration guiceSideCfg, ServletContext context) {
        if (context == null) {
            String gripe = "Error attempting to create a loadConfiguration from a null ServletContext!";
            log.error(gripe);
            throw new IllegalArgumentException(gripe);
        }

        Properties properties = new Properties();


        /**
         * if the user has specified an external velocity configuration file, we'll want to search for it in the
         * following order
         *
         * 1. relative to the context path
         * 2. relative to /WEB-INF
         * 3. in the class path
         */
        String configfile;
        if (StringUtils.isNotBlank(guiceSideCfg.getVelocityConfigfile())) {
            configfile = guiceSideCfg.getVelocityConfigfile();
        } else {
            configfile = "velocity.properties";
        }

        configfile = configfile.trim();

        InputStream in = null;
        String resourceLocation = null;

        try {
            if (context.getRealPath(configfile) != null) {
                // 1. relative to context path, i.e. /velocity.properties
                String filename = context.getRealPath(configfile);

                if (filename != null) {
                    File file = new File(filename);

                    if (file.isFile()) {
                        resourceLocation = file.getCanonicalPath() + " from file system";
                        in = new FileInputStream(file);
                    }

                    // 2. if nothing was found relative to the context path, search relative to the WEB-INF directory
                    if (in == null) {
                        file = new File(context.getRealPath("/WEB-INF/" + configfile));

                        if (file.isFile()) {
                            resourceLocation = file.getCanonicalPath() + " from file system";
                            in = new FileInputStream(file);
                        }
                    }
                }
            }

            // 3. finally, if there's no physical file, how about something in our classpath
            if (in == null) {
                in = VelocityManager.class.getClassLoader().getResourceAsStream(configfile);
                if (in != null) {
                    resourceLocation = configfile + " from classloader";
                }
            }

            // if we've got something, load 'er up
            if (in != null) {
                log.info("Initializing velocity using " + resourceLocation);
                properties.load(in);
            }
        } catch (IOException e) {
            log.warn("Unable to load velocity configuration " + resourceLocation, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        // overide with programmatically set properties
        
        // for debugging purposes, allows users to dump out the properties that have been configured
        if (log.isDebugEnabled()) {
            log.debug("Initializing Velocity with the following properties ...");

            for (Iterator iter = properties.keySet().iterator();
                 iter.hasNext();) {
                String key = (String) iter.next();
                String value = properties.getProperty(key);

                if (log.isDebugEnabled()) {
                    log.debug("    '" + key + "' = '" + value + "'");
                }
            }
        }

        return properties;
    }


    public Context createContext(HttpServletRequest req, HttpServletResponse res, HttpSession httpSession) {
        VelocityContext context = new VelocityContext();
        String key;
        for (Enumeration e = req.getAttributeNames(); e.hasMoreElements(); context.put(key, req.getAttribute(key))) {
            key = (String) e.nextElement();
        }
        context.put(REQUEST, req);
        context.put(RESPONSE, res);
        context.put(SESSION, httpSession);
        return context;
    }

}
