package com.hamstercoders.netty.dub.server.core.di;

import com.hamstercoders.netty.dub.Application;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.ServerContext;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.utils.ReflectionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class WebHandlersFinder {

    public WebHandlersFinder(ComponentSource context) {
        this.context = context;
    }

    private ComponentSource context;

    public Map<String, SimpleHttpChannelServerHandler> findHandlers() {
        Map<String, SimpleHttpChannelServerHandler> handlers = new HashMap<>();
        List<Class<?>> handlerClasses = ReflectionUtil.find(Application.PROJECT_PACKAGE,
                clazz -> Arrays.asList(clazz.getInterfaces()).contains(SimpleHttpChannelServerHandler.class) &&
                        clazz.isAnnotationPresent(HttpHandler.class));
        for (Class<?> c : handlerClasses) {
            handlers.put(c.getAnnotation(HttpHandler.class).value(), (SimpleHttpChannelServerHandler) context.getComponent(c));
        }
        return handlers;
    }
}
