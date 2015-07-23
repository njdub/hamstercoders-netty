package com.hamstercoders.netty.dub.dao;

import com.hamstercoders.netty.dub.entities.RequestInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

/**
 * Created on 21-Jul-15.
 *
 * @author Nazar Dub
 */
public class ServerInfoRepository implements ServerInfo, ServerStatusDao {
    private final Map<String, List<RequestInfo>> requests = synchronizedMap(new HashMap<>());

    @Override
    public Map<String, List<RequestInfo>> getRequests() {
        return requests;
    }

    @Override
    public void addRequest(RequestInfo request) {
        if (request != null) {
            if (!requests.containsKey(request.getIp())) {
                requests.put(request.getIp(), synchronizedList(new ArrayList<RequestInfo>() {{
                    add(request);
                }}));
            } else {
                requests.get(request.getIp()).add(request);
            }
        }
    }

    @Override
    public long requestCount() {
        final AtomicLong count = new AtomicLong(0);
        requests.values().forEach(requestInfo -> {
            requestInfo.forEach(requestInfo1 -> count.incrementAndGet());
        });
        return count.get();
    }

    @Override
    public Map<String, Long> getUniqueRequestCountPerId() {
        Map<String, Long> result = new HashMap<>();
        requests.keySet().forEach(new Consumer<String>() {
            @Override
            public void accept(String ip) {
                final AtomicLong count = new AtomicLong(0);
                requests.get(ip).forEach(requestInfo -> count.incrementAndGet());
                result.put(ip, count.get());
            }
        });
        return result;
    }

    @Override
    public long getActiveConnectionCount() {
        throw new UnsupportedOperationException("This method hasn't implemented, yet");
    }
}
