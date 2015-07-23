package com.hamstercoders.netty.dub.server.core.di.exceptions;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class FailureBeanInjectionException extends RuntimeException {
    public FailureBeanInjectionException(String message) {
        super(message);
    }
}
