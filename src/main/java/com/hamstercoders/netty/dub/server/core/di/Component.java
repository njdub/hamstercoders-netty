package com.hamstercoders.netty.dub.server.core.di;

import java.lang.annotation.*;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * @return is component singleton
     */
    boolean value() default true;
}
