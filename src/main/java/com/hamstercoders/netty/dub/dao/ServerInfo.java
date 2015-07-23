package com.hamstercoders.netty.dub.dao;

import com.hamstercoders.netty.dub.entities.RequestInfo;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.Map;

/**
 * Created on 22-Jul-15.
 *
 * @author Nazar Dub
 */
public interface ServerInfo {
    public static final AttributeKey<RequestInfo> REQUEST_INFO = AttributeKey.valueOf("request.info");

    public void addRequest(RequestInfo request);

    public void incrementActiveConnection();

    public void decrementActiveConnections();

}
