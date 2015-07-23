package com.hamstercoders.netty.dub.handlers.web;

import com.hamstercoders.netty.dub.dao.ServerStatusDao;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@HttpHandler("/status")
public class HttpStatusServerHandler implements SimpleHttpChannelServerHandler {

    @Inject
    ServerStatusDao status;

    public static final String NOT_FOUND_MESSAGE =
            "<h1 align=\"center\">Page Not Found</h1>" +
                    "<h2 align=\"center\">404<h2>";

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

        System.out.println("::::::::STATUS::::::::");
        System.out.println("Req count: " + status.requestCount());
        System.out.println("Req unique count per id: " + status.getUniqueRequestCountPerId());

        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        FullHttpResponse res = new DefaultFullHttpResponse(
                HTTP_1_1,
                NOT_FOUND,
                Unpooled.wrappedBuffer(NOT_FOUND_MESSAGE.getBytes()));
        ctx.write(res).addListener(ChannelFutureListener.CLOSE);

        res.headers().set(CONTENT_TYPE, "text/plain");
        res.headers().set(CONTENT_LENGTH, res.content().readableBytes());
    }
}
