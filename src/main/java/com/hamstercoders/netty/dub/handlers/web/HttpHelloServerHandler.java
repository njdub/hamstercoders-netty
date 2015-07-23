package com.hamstercoders.netty.dub.handlers.web;

import com.hamstercoders.netty.dub.dao.HelloDao;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@HttpHandler("/hello")
public class HttpHelloServerHandler implements SimpleHttpChannelServerHandler {

    private static final int DELAY = 10;

    @Inject
    private HelloDao dao;

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        ctx.executor().schedule(new WriteHelloTask(ctx, req), DELAY, TimeUnit.SECONDS);
    }

    private class WriteHelloTask implements Runnable {
        private final ChannelHandlerContext ctx;
        private final HttpRequest req;

        private WriteHelloTask(ChannelHandlerContext ctx, HttpRequest req) {
            this.ctx = ctx;
            this.req = req;
        }

        @Override
        public void run() {
            FullHttpResponse res = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    OK,
                    Unpooled.wrappedBuffer(dao.getMessage().getBytes()));
            res.headers().set(CONTENT_TYPE, "text/plain");
            res.headers().set(CONTENT_LENGTH, res.content().readableBytes());

            if (!HttpHeaders.isKeepAlive(req)) {
                ctx.write(res).addListener(ChannelFutureListener.CLOSE);
            } else {
                res.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(res);
            }
            ctx.flush();
        }
    }
}
