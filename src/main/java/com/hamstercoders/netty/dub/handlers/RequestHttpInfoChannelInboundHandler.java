package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.dao.ServerInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@ChannelHandler.Sharable
@Component
public class RequestHttpInfoChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        ctx.channel().attr(ServerInfo.REQUEST_INFO).get().setIp(address.toString());
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            ctx.channel().attr(ServerInfo.REQUEST_INFO).get().setUri(request.getUri());
        }
        super.channelRead(ctx, msg);
    }
}
