package org.guiceside.web.context.module;

import com.google.inject.AbstractModule;
import org.guiceside.commons.collection.RequestData;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 16:11:51
 * To change this template use File | Settings | File Templates.
 */
public class RequestDataModule extends AbstractModule{
    protected void configure() {
        bind(RequestData.class).toProvider(RequestDataProvider.class);
    }
}
