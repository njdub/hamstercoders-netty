package com.hamstercoders.netty.dub.server.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
public interface SimpleHttpChannelServerHandler {
    public void channelRead(ChannelHandlerContext ctx, HttpRequest req) throws Exception;
}
