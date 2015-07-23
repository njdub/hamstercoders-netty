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
        //System.out.println(":::::::::::::::::::::::::::::::::::::");
//        SocketAddress address = ctx.channel().remoteAddress();
//        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
//        System.out.println(inetSocketAddress.getHostName());
//        System.out.println(inetSocketAddress.getHostString());
//        System.out.println(inetSocketAddress.getPort());
//        System.out.println(inetSocketAddress.toString());

//        EmbeddedChannel ch = new EmbeddedChannel(new HttpRequestEncoder());
//        ch.writeOutbound(msg);
//        ByteBuf encoded = (ByteBuf) ch.readOutbound();
//        ch.close();
//
//        DefaultMessageSizeEstimator estimator = new DefaultMessageSizeEstimator(0);
//        System.out.println(estimator.newHandle().size(encoded));

        //LengthFieldBasedFrameDecoder decoder = new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,)

//        System.out.println(ctx.channel().toString());
//        System.out.println(ctx.name());
//        System.out.println(ctx.hashCode());
//        System.out.println(ctx.newPromise());
//        ctx.channel().attr(AttributeKey.valueOf("ate")).set("Nazar");
//        System.out.println(":::::::::::::::::::::::::::::::::::::");


        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            QueryStringDecoder query = new QueryStringDecoder(request.getUri());
            SimpleHttpChannelServerHandler handler = handlers.get(query.path());
            //System.out.println(query.path());
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
