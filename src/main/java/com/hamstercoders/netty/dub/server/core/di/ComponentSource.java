package com.hamstercoders.netty.dub.server.core.di;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public interface ComponentSource {
    public Object getComponent(Class<?> clazz);
}
