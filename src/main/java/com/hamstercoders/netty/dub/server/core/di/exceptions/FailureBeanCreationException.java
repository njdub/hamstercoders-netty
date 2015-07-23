package com.hamstercoders.netty.dub.server.core.di.exceptions;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class FailureBeanCreationException extends RuntimeException {
    public FailureBeanCreationException(String message) {
        super(message);
    }

    public FailureBeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailureBeanCreationException(Throwable cause) {
        super(cause);
    }
}
