package org.guiceside.web.view.freemarker;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-27
 * Time: 23:53:04
 * To change this template use File | Settings | File Templates.
 */
public class ScopesHashModel extends SimpleHash{
    private HttpServletRequest request;
    private ServletContext servletContext;
    public ScopesHashModel(ObjectWrapper objectWrapper, ServletContext context, HttpServletRequest request) {
        super(objectWrapper);
        this.servletContext = context;
        this.request = request;
    }

    public TemplateModel get(String key) throws TemplateModelException {
        // Lookup in default scope
        TemplateModel model = super.get(key);

        if (model != null) {
            return model;
        }

        if (request != null) {
            // Lookup in request scope
            Object obj = request.getAttribute(key);

            if (obj != null) {
                return wrap(obj);
            }

            // Lookup in session scope
            HttpSession session = request.getSession(false);

            if (session != null) {
                obj = session.getAttribute(key);

                if (obj != null) {
                    return wrap(obj);
                }
            }
        }

        if (servletContext != null) {
            // Lookup in application scope
            Object obj = servletContext.getAttribute(key);

            if (obj != null) {
                return wrap(obj);
            }
        }

        return null;
    }

    public void put(String string, boolean b) {
        super.put(string, b);
    }

    public void put(String string, Object object) {
        super.put(string, object);
    }
}
