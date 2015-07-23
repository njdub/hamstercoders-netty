package com.hamstercoders.netty.dub.dao;

import com.hamstercoders.netty.dub.entities.RequestInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on 21-Jul-15.
 *
 * @author Nazar Dub
 */
public interface ServerStatus {
    public long requestCount();

    public Map<String, Long> getUniqueRequestCountPerId();

    public long getActiveConnectionCount();

    public Map<String, Map<Long, Date>> getRequestCountPerId();

    public Map<String, Long> getRedirectCountPerUrl();

    public List<RequestInfo> getLastRequests(int number);

    public Map<String, List<RequestInfo>> getRequests();

}
