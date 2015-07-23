package com.hamstercoders.netty.dub.handlers.web;

import com.hamstercoders.netty.dub.dao.ServerStatus;
import com.hamstercoders.netty.dub.entities.RequestInfo;
import com.hamstercoders.netty.dub.server.core.di.HttpHandler;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.views.ServerStatusPage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
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

    @Inject
    ServerStatusPage page;

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        StringBuilder res = new StringBuilder();

        page.begin(res);
        page.activeConnections(res, status.getActiveConnectionCount());
        page.totalRequest(res, status.requestCount());
        page.uniqueRequestPerId(res, status.getUniqueRequestCountPerId());
        page.requestPerId(res, status.getRequestCountPerId());
        page.redirectCountPerUrl(res, status.getRedirectCountPerUrl());
        page.lastRequests(res, status.getLastRequests(16));
        page.end(res);

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
