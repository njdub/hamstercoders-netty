package com.hamstercoders.netty.dub.server.core.di.bean;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class SingletonBeanDefinition implements BeanDefinition {
    private Class<?> clazz;
    private Object instance;

    public SingletonBeanDefinition(Class<?> clazz, Object instance) {
        this.clazz = clazz;
        this.instance = instance;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}
