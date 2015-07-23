package com.hamstercoders.netty.dub.server.core.di;

import com.hamstercoders.netty.dub.server.core.di.Component;

import java.lang.annotation.*;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface HttpHandler {
    String value();
}
