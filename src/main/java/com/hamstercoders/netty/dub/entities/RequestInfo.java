package com.hamstercoders.netty.dub.entities;

import java.util.Date;

/**
 * Created on 21-Jul-15.
 *
 * @author Nazar Dub
 */
public class RequestInfo {
    private String ip;
    private String uri;
    private long startTime;
    private long endTime;
    private long sentBytes;
    private long receivedBytes;
    private Date requestDate;

    public RequestInfo() {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "ip='" + ip + '\'' +
                ", uri='" + uri + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sentBytes=" + sentBytes +
                ", receivedBytes=" + receivedBytes +
                ", requestDate=" + requestDate +
                '}';
    }
}


