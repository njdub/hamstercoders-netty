package com.hamstercoders.netty.dub.server.core.di.bean;

import java.lang.annotation.*;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 *
 *<p>
 *  Work only in class declared with <code>@Configuration</code> annotaion
 *  Method must hasn't any args
 *</p>
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

    /**
     * @return is bean singleton
     */
    boolean value() default true;
}
