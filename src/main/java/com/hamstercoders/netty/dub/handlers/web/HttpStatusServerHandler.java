package com.hamstercoders.netty.dub.handlers.web;

import com.hamstercoders.netty.dub.dao.ServerStatus;
import com.hamstercoders.netty.dub.entities.RequestInfo;
import com.hamstercoders.netty.dub.server.core.di.HttpHandler;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@HttpHandler("/status")
public class HttpStatusServerHandler implements SimpleHttpChannelServerHandler {

    @Inject
    ServerStatus status;

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        StringBuilder res = new StringBuilder("<body><h1  align=\"center\">STATUS</h1>");
        res.append("<p  align=\"center\" >Active connections: ").append(status.getActiveConnectionCount()).append("</p>");
        res.append("<p  align=\"center\" >Total request number: ").append(status.requestCount()).append("</p>");
        res.append("<p>Unique request per id:</p>");
        res.append("<ul>");
        Map<String, Long> uniqueRequestPerId = status.getUniqueRequestCountPerId();
        for (String ip : uniqueRequestPerId.keySet()) {
            res.append("<li>").append(ip).append(" -> ").append(uniqueRequestPerId.get(ip)).append("</li>");
        }
        res.append("</ul>");

        res.append("<table border=\"1\" align=\"center\">");
        res.append("<caption>Request per id</caption>");
        res.append("<tr><th>IP</th><th>COUNT</th><th>LAST_DATE</th></tr>");
        Map<String, Map<Long, Date>> requestPerId = status.getRequestCountPerId();
        for (String ip : requestPerId.keySet()) {
            res.append("<tr>");

            res.append("<td>").append(ip).append("</td>");
            res.append("<td>");
            requestPerId.get(ip).keySet().forEach(res::append);
            res.append("</td>");
            res.append("<td>");
            requestPerId.get(ip).values().forEach(res::append);
            res.append("</td>");

            res.append("</tr>");
        }
        res.append("</table>");

        Map<String, Long> redirectPerUrl = status.getRedirectCountPerUrl();
        res.append("<hr/><br/>");
        res.append("<table border=\"1\" align=\"center\">");
        res.append("<caption>Redirect per url</caption>");
        res.append("<tr><th>URL</th><th>COUNT</th></tr>");
        for (String url : redirectPerUrl.keySet()) {
            res.append("<tr>");
            res.append("<td>").append(url).append("</td>");
            res.append("<td>").append(redirectPerUrl.get(url)).append("</td>");
            res.append("</tr>");
        }
        res.append("</table>");

        List<RequestInfo> lastRequest = status.getLastRequests(16);

        res.append("<hr/><br/>");
        res.append("<table border=\"1\" align=\"center\">");
        res.append("<caption>Last requests</caption>");
        res.append("<tr><th>IP</th><th>URL</th><th>Date</th><th>Sent bytes</th><th>Received bytes</th><th>Speed (bytes/sec)</th></tr>");
        for (RequestInfo r : lastRequest) {
            res.append("<tr>");

            res.append("<td>").append(r.getIp()).append("</td>");
            res.append("<td>").append(r.getUri()).append("</td>");
            res.append("<td>").append(r.getRequestDate()).append("</td>");
            res.append("<td>").append(r.getSentBytes()).append("</td>");
            res.append("<td>").append(r.getReceivedBytes()).append("</td>");
            long bytes = r.getReceivedBytes() + r.getSentBytes();
            long time = (r.getEndTime() - r.getStartTime() != 0 ? r.getEndTime() - r.getStartTime() : 1000000000);
            double speed = ((double) bytes / (double) (time / 1000000000));
            if (speed == Double.POSITIVE_INFINITY || speed == Double.NEGATIVE_INFINITY) {
                speed = bytes;
            }
            res.append("<td>").append(speed).append("</td>");

            res.append("</tr>");
        }
        res.append("</table>");


        res.append("</body>");
        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.wrappedBuffer(res.toString().getBytes()));
        httpResponse.headers().set(CONTENT_TYPE, "text/html");
        httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());

        if (!HttpHeaders.isKeepAlive(req)) {
            ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
        } else {
            httpResponse.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(httpResponse);
        }
        ctx.flush();
    }
}
