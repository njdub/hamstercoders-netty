package com.hamstercoders.netty.dub.dao;

import com.hamstercoders.netty.dub.entities.RequestInfo;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.util.Collections.replaceAll;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

/**
 * Created on 21-Jul-15.
 *
 * @author Nazar Dub
 */
public class ServerInfoRepository implements ServerInfo, ServerStatus {
    private final Map<String, List<RequestInfo>> requests = synchronizedMap(new HashMap<>());

    private final AtomicLong activeConnectionCount = new AtomicLong(0);

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
    public void incrementActiveConnection() {
        this.activeConnectionCount.incrementAndGet();
    }

    @Override
    public void decrementActiveConnections() {
        this.activeConnectionCount.decrementAndGet();
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
                final Set<String> uniqueUri = new HashSet<>();
                requests.get(ip).forEach(new Consumer<RequestInfo>() {
                    @Override
                    public void accept(RequestInfo requestInfo) {
                        uniqueUri.add(requestInfo.getUri());
                    }
                });
                result.put(ip, (long) uniqueUri.size());
            }
        });
        return result;
    }

    @Override
    public long getActiveConnectionCount() {
        return this.activeConnectionCount.get();
    }

    @Override
    public Map<String, Map<Long, Date>> getRequestCountPerId() {
        Map<String, Map<Long, Date>> result = new HashMap<>();
        for (String ip : requests.keySet()) {
            Long count = 0L;
            Date lastDate = null;
            Iterator<RequestInfo> requestInfoIterator = requests.get(ip).iterator();
            while (requestInfoIterator.hasNext()) {
                RequestInfo info = requestInfoIterator.next();
                count++;
                if (lastDate == null) {
                    lastDate = info.getRequestDate();
                } else {
                    if (info.getRequestDate().after(lastDate)) {
                        lastDate = info.getRequestDate();
                    }
                }
            }
//            for (RequestInfo info : requests.get(ip)) {
//                count++;
//                if (lastDate == null) {
//                    lastDate = info.getRequestDate();
//                } else {
//                    if (info.getRequestDate().after(lastDate)) {
//                        lastDate = info.getRequestDate();
//                    }
//                }
//
//            }
            Map<Long, Date> ipResult = new HashMap<>();
            ipResult.put(count, lastDate);
            result.put(ip, ipResult);
        }
        return result;

    }

    @Override
    public Map<String, Long> getRedirectCountPerUrl() {
        final String url = "/redirect";
        final Map<String, Long> result = new HashMap<>();
        requests.keySet().forEach(new Consumer<String>() {
            @Override
            public void accept(String ip) {
                requests.get(ip).forEach(new Consumer<RequestInfo>() {
                    @Override
                    public void accept(RequestInfo requestInfo) {
                        QueryStringDecoder query = new QueryStringDecoder(requestInfo.getUri());
                        if (query.path().equals(url)) {
                            String urlParam = query.parameters().get("url").get(0);
                            if (urlParam != null) {
                                if (result.get(urlParam) == null) {
                                    result.put(urlParam, 1L);
                                } else {
                                    result.put(urlParam, (result.get(urlParam) + 1L));
                                }
                            }
                        }
                    }
                });
            }
        });
        return result;
    }

    @Override
    public List<RequestInfo> getLastRequests(int number) {
        List<RequestInfo> result = new ArrayList<>();
        requests.keySet().forEach(new Consumer<String>() {
            @Override
            public void accept(String id) {
                requests.get(id).forEach(new Consumer<RequestInfo>() {
                    @Override
                    public void accept(RequestInfo requestInfo) {
                        if (result.size() < number) {
                            result.add(requestInfo);
                        } else {
                            for (int i = 0; i < result.size(); i++) {
                                if (result.get(i).getRequestDate().before(requestInfo.getRequestDate())) {
                                    requestInfo = result.set(i, requestInfo);
                                }
                            }
                        }
                    }
                });
            }
        });
        return result;
    }


}
