package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import com.hamstercoders.netty.dub.dao.ServerInfo;
import io.netty.channel.*;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
@ChannelHandler.Sharable
@Component
public class RequestSizeChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Inject
    private MessageSizeEstimator estimator;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().attr(ServerInfo.REQUEST_INFO).get().setReceivedBytes(estimator.newHandle().size(msg));
        super.channelRead(ctx, msg);
    }
}
