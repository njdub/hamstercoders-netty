package com.hamstercoders.netty.dub.server.core.di.bean;

import com.hamstercoders.netty.dub.server.core.di.exceptions.FailureBeanCreationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class PrototypeBeanDefinition implements BeanDefinition {
    private Class<?> clazz;
    private Object configuration;
    private Method method;

    public PrototypeBeanDefinition(Class<?> clazz, Object configuration, Method method) {
        this.clazz = clazz;
        this.configuration = configuration;
        this.method = method;
    }

    @Override
    public Object getInstance() {
        try {
            return method.invoke(configuration);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FailureBeanCreationException(e);
        }
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}
