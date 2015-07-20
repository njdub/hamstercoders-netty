package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.dao.HelloDao;
import com.hamstercoders.netty.dub.dao.SimpleHelloDao;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

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

    private HelloDao dao = new SimpleHelloDao();

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

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
        Thread.sleep(10000);    //Blocking thread here TODO: Try to find better solution
    }
}
