package com.hamstercoders.netty.dub.server.core;

import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
public class ChannelInboundMediator extends ChannelInboundHandlerAdapter {
    private final Map<String, SimpleHttpChannelServerHandler> handlers;


    public ChannelInboundMediator(Map<String, SimpleHttpChannelServerHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            QueryStringDecoder query = new QueryStringDecoder(request.getUri());
            SimpleHttpChannelServerHandler handler = handlers.get(query.path());
            if (handler == null) {
                handler = handlers.get("/NOT_FOUND");
                super.channelRead(ctx, request);
            }
            if (handler != null) {
                handler.channelRead(ctx, request);
            }
        } else {
            super.channelRead(ctx, msg);
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
