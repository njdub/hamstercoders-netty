package com.hamstercoders.netty.dub.handlers;

import com.hamstercoders.netty.dub.server.core.di.Component;
import com.hamstercoders.netty.dub.server.core.di.Inject;
import com.hamstercoders.netty.dub.dao.ServerInfo;
import io.netty.channel.*;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
@ChannelHandler.Sharable
@Component
public class ResponseSizeChannelOutboundHandler extends ChannelOutboundHandlerAdapter {


    @Inject
    private MessageSizeEstimator estimator;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.channel().attr(ServerInfo.REQUEST_INFO).get().setSentBytes(estimator.newHandle().size(msg));
        super.write(ctx, msg, promise);
    }
}
