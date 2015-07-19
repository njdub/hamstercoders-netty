package com.hamstercoders.netty.dub.dao;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
public class SimpleHelloDao implements HelloDao {
    public static final String MESSAGE = "Hello World";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
