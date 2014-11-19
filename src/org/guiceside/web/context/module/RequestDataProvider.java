package org.guiceside.web.context.module;

import com.google.inject.Provider;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.dispatcher.FilterDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 16:04:14
 * To change this template use File | Settings | File Templates.
 */
public class RequestDataProvider implements Provider<RequestData>{
    public RequestData get() {
        return FilterDispatcher.getRequestData();
    }
}
