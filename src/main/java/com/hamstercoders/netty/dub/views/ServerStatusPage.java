package com.hamstercoders.netty.dub.views;

import com.hamstercoders.netty.dub.entities.RequestInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class ServerStatusPage {

    public StringBuilder activeConnections(StringBuilder builder, long connectionCount) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<p  align=\"center\" >Active connections: ").append(connectionCount).append("</p>");
        return builder;
    }

    public StringBuilder totalRequest(StringBuilder builder, long requestCount) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<p  align=\"center\" >Total request number: ").append(requestCount).append("</p>");
        return builder;
    }

    public StringBuilder uniqueRequestPerId(StringBuilder builder, Map<String, Long> requests) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<p>Unique request per id:</p>");
        builder.append("<ul>");
        for (String ip : requests.keySet()) {
            builder.append("<li>").append(ip).append(" -> ").append(requests.get(ip)).append("</li>");
        }
        builder.append("</ul>");
        return builder;
    }

    public StringBuilder requestPerId(StringBuilder builder, Map<String, Map<Long, Date>> requestCount) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<table border=\"1\" align=\"center\">");
        builder.append("<caption>Request per id</caption>");
        builder.append("<tr><th>IP</th><th>COUNT</th><th>LAST_DATE</th></tr>");
        for (String ip : requestCount.keySet()) {
            builder.append("<tr>");

            builder.append("<td>").append(ip).append("</td>");
            builder.append("<td>");
            requestCount.get(ip).keySet().forEach(builder::append);
            builder.append("</td>");
            builder.append("<td>");
            requestCount.get(ip).values().forEach(builder::append);
            builder.append("</td>");

            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder;
    }

    public StringBuilder redirectCountPerUrl(StringBuilder builder, Map<String, Long> redirectPerUrl) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<hr/><br/>");
        builder.append("<table border=\"1\" align=\"center\">");
        builder.append("<caption>Redirect per url</caption>");
        builder.append("<tr><th>URL</th><th>COUNT</th></tr>");
        for (String url : redirectPerUrl.keySet()) {
            builder.append("<tr>");
            builder.append("<td>").append(url).append("</td>");
            builder.append("<td>").append(redirectPerUrl.get(url)).append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder;
    }


    public StringBuilder lastRequests(StringBuilder builder, List<RequestInfo> requests) {
        if (builder == null) {
            builder = new StringBuilder();
        }

        builder.append("<hr/><br/>");
        builder.append("<table border=\"1\" align=\"center\">");
        builder.append("<caption>Last requests</caption>");
        builder.append("<tr><th>IP</th><th>URL</th><th>Date</th><th>Sent bytes</th><th>Received bytes</th><th>Speed (bytes/sec)</th></tr>");
        for (RequestInfo r : requests) {
            builder.append("<tr>");

            builder.append("<td>").append(r.getIp()).append("</td>");
            builder.append("<td>").append(r.getUri()).append("</td>");
            builder.append("<td>").append(r.getRequestDate()).append("</td>");
            builder.append("<td>").append(r.getSentBytes()).append("</td>");
            builder.append("<td>").append(r.getReceivedBytes()).append("</td>");
            long bytes = r.getReceivedBytes() + r.getSentBytes();
            long time = (r.getEndTime() - r.getStartTime() != 0 ? r.getEndTime() - r.getStartTime() : 1000000000);
            double speed = ((double) bytes / (double) (time / 1000000000));
            if (speed == Double.POSITIVE_INFINITY || speed == Double.NEGATIVE_INFINITY) {
                speed = bytes;
            }
            builder.append("<td>").append(speed).append("</td>");

            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder;
    }

    public StringBuilder begin(StringBuilder builder) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("<body><h1  align=\"center\">STATUS</h1>");
        return builder;
    }

    public StringBuilder end(StringBuilder builder) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        builder.append("</body>");
        return builder;
    }

}
