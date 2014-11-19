package la.mingdao.common;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.web.annotation.ActionInterceptor;
import org.guiceside.web.annotation.BindingGuice;

import java.lang.reflect.Method;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-11-6
 * @since JDK1.5
 */
public class ActionInterceptorModel extends AbstractInterceptorStrategy {

    public Matcher<? super Class<?>> getClassMatcher() {
        // TODO Auto-generated method stub
        return Matchers.annotatedWith(BindingGuice.class);
    }

    public MethodInterceptor getMethodInterceptor() {
        // TODO Auto-generated method stub
        return new la.mingdao.common.ActionInterceptor();
    }

    public Matcher<? super Method> getMethodMatcher() {
        // TODO Auto-generated method stub
        return Matchers.annotatedWith(ActionInterceptor.class);
    }

}