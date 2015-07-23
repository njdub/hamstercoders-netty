package com.hamstercoders.netty.dub.server.core.di.exceptions;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class FailureComponentCreationException extends RuntimeException {
    public FailureComponentCreationException(String message) {
        super(message);
    }

    public FailureComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailureComponentCreationException(Throwable cause) {
        super(cause);
    }
}
