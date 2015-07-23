package com.hamstercoders.netty.dub.server.core.di.bean;

import java.lang.annotation.*;

/**
 * Created on 22-Jul-15.
 *
 * @author Nazar Dub
 *
 * <p>
 *     Configuration class must have default constructor
 * </p>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
}
