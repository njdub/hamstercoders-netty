package com.hamstercoders.netty.dub.server.core.di.bean;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public interface BeanDefinition {
    public Object getInstance();

    public Class<?> getClazz();
}
