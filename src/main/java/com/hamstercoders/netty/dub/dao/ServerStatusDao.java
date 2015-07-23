package com.hamstercoders.netty.dub.dao;

import java.util.Map;

/**
 * Created on 21-Jul-15.
 *
 * @author Nazar Dub
 */
public interface ServerStatusDao {
    public long requestCount();

    public Map<String, Long> getUniqueRequestCountPerId();

    public long getActiveConnectionCount();

}
