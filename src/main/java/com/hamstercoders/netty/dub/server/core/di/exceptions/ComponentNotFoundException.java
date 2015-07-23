package com.hamstercoders.netty.dub.server.core.di.exceptions;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException() {
        super();
    }

    public ComponentNotFoundException(String message) {
        super(message);
    }
}
