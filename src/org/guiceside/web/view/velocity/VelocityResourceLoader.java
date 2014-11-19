package org.guiceside.web.view.velocity;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.guiceside.commons.lang.ClassLoaderUtils;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-5
 * Time: 21:25:06
 * To change this template use File | Settings | File Templates.
 */
public class VelocityResourceLoader extends ClasspathResourceLoader {
    public synchronized InputStream getResourceStream(String name) throws ResourceNotFoundException {
        if ((name == null) || (name.length() == 0)) {
            throw new ResourceNotFoundException("No template name provided");
        }

        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        try {
            return ClassLoaderUtils.getResourceAsStream(name, VelocityResourceLoader.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
