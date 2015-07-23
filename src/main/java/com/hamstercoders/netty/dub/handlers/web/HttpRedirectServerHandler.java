package com.hamstercoders.netty.dub.handlers.web;

import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.server.core.di.Component;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@HttpHandler("/redirect")
public class HttpRedirectServerHandler implements SimpleHttpChannelServerHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

//        System.out.println("::::::::::::::::::::::::::::::::::::::::");
//        System.out.println(req.getMethod().toString());
//        System.out.println(req.getUri());

        QueryStringDecoder query = new QueryStringDecoder(req.getUri());

//        System.out.println(query.path());
//        System.out.println(query.parameters().get("url"));
//        System.out.println(req.getProtocolVersion());
//        System.out.println(HttpHeaders.getHost(req, "none1"));


        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, MOVED_PERMANENTLY);
        res.headers().set(LOCATION, query.parameters().get("url"));

        if (!HttpHeaders.isKeepAlive(req)) {
            ctx.write(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            res.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(res);
        }
    }
}
