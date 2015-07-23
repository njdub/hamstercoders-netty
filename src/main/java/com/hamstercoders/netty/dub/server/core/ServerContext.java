package com.hamstercoders.netty.dub.server.core;

import com.hamstercoders.netty.dub.server.DefaultServer;
import com.hamstercoders.netty.dub.server.NettyServer;
import com.hamstercoders.netty.dub.server.core.di.ComponentSource;
import com.hamstercoders.netty.dub.server.core.di.DependencyInjectionCore;
import com.hamstercoders.netty.dub.server.core.di.WebHandlersFinder;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Objects;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class ServerContext implements ComponentSource {
    private final String rootPackage;

    private DependencyInjectionCore di;
    private WebHandlersFinder webFinder;

    private NettyServer server;

    public ServerContext(String rootPackage) {
        Objects.requireNonNull(rootPackage);
        this.rootPackage = rootPackage;
        di = new DependencyInjectionCore(this.rootPackage);
        initializeDi();
        webFinder = new WebHandlersFinder(this);
    }

    private void initializeDi() {
        di.initialization();
    }

    @Override
    public Object getComponent(Class<?> clazz) {
        return di.getComponent(clazz);
    }

    public ChannelInboundHandlerAdapter getHttpChannelHandler() {
        return new ChannelInboundMediator(webFinder.findHandlers());
    }

    public void setServer(NettyServer server) {
        this.server = server;
    }


    public void runServer() throws Exception {
        server.run();
    }
}
