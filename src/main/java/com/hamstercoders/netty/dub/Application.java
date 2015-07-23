package com.hamstercoders.netty.dub;

import com.hamstercoders.netty.dub.handlers.*;
import com.hamstercoders.netty.dub.server.DefaultServer;
import com.hamstercoders.netty.dub.server.core.ServerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
public class Application {
    public static final String PROJECT_PACKAGE = "com.hamstercoders";

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = DefaultServer.DEFAULT_PORT;
        }

        ServerContext context = new ServerContext(PROJECT_PACKAGE);


        context.getHttpChannelHandler();

        DefaultServer server = new DefaultServer(port);

        server.setChannelInitializer(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast((ChannelHandler) context.getComponent(ProfilingStartChannelInboundHandler.class));
                ch.pipeline().addLast((ChannelHandler) context.getComponent(RequestSizeChannelInboundHandler.class));
                ch.pipeline().addLast((ChannelHandler) context.getComponent(RequestTimeChannelInboundHandler.class));
                ch.pipeline().addLast(new HttpRequestDecoder());
                ch.pipeline().addLast((ChannelHandler) context.getComponent(RequestHttpInfoChannelInboundHandler.class));
                ch.pipeline().addLast(new HttpResponseEncoder());
                ch.pipeline().addLast((ChannelHandler) context.getComponent(ProfilingEndChannelOutboundHandler.class));
                ch.pipeline().addLast((ChannelHandler) context.getComponent(ResponseTimeChannelOutboundHandler.class));
                ch.pipeline().addLast((ChannelHandler) context.getComponent(ResponseSizeChannelOutboundHandler.class));
                ch.pipeline().addLast(context.getHttpChannelHandler());
            }
        });

        context.setServer(server);

        context.runServer();

    }
}
