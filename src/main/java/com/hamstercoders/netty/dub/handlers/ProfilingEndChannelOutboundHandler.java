package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.dao.ServerInfo;
import com.hamstercoders.netty.dub.server.AppConfiguration;
import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
@ChannelHandler.Sharable
@Component
public class ProfilingEndChannelOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Inject
    private ServerInfo info;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //System.out.println(ctx.channel().attr(ServerInfo.REQUEST_INFO).get());
        info.addRequest(ctx.channel().attr(ServerInfo.REQUEST_INFO).get());
        super.write(ctx, msg, promise);
    }
}
