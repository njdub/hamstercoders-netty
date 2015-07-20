package com.hamstercoders.netty.dub.server.core;

import java.lang.annotation.*;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpHandler {
    String value();
}
