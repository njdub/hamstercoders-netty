package com.hamstercoders.netty.dub.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Objects;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
public class DefaultServer implements NettyServer {
    public static final int DEFAULT_PORT = 9090;

    private int port;

    private ChannelHandler channelInitializer;

    public DefaultServer(int port) {
        this.port = port;
    }

    public DefaultServer(int port, ChannelHandler channelInitializer) {
        this(port);
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void run() throws Exception {
        Objects.requireNonNull(this.channelInitializer, "Channel initializer can't be null");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(this.channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public ChannelHandler getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelHandler channelInitializer) {
        this.channelInitializer = channelInitializer;
    }
}
