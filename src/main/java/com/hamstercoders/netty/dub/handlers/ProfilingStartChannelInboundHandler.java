package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.entities.RequestInfo;
import com.hamstercoders.netty.dub.dao.ServerInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@ChannelHandler.Sharable
@Component
public class ProfilingStartChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().attr(ServerInfo.REQUEST_INFO).set(new RequestInfo());
        super.channelRead(ctx, msg);
    }
}
