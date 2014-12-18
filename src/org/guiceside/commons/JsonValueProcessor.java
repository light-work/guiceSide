package org.guiceside.commons;

/**
 * Created by admin on 2014/12/13.
 */
public interface JsonValueProcessor {
    <T> T process(String name, Object value,T t);

}
