package com.hamstercoders.netty.dub.server;

import com.hamstercoders.netty.dub.Application;
import com.hamstercoders.netty.dub.handlers.RequestSizeChannelInboundHandler;
import com.hamstercoders.netty.dub.handlers.RequestHttpInfoChannelInboundHandler;
import com.hamstercoders.netty.dub.handlers.ResponseSizeChannelOutboundHandler;
import com.hamstercoders.netty.dub.server.core.ChannelInboundMediator;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.utils.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.*;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    //    final Map<String, SimpleHttpChannelServerHandler> handlers = new HashMap<String, SimpleHttpChannelServerHandler>() {{
//        put("/hello", new HttpHelloServerHandler());
//        put("/redirect", new HttpRedirectServerHandler());
//        put("/NOT_FOUND", new HttpNotFoundServerHandler());
//    }};
    private final Map<String, SimpleHttpChannelServerHandler> handlers;

    public ServerChannelInitializer() {
        this.handlers = findHandlers();
    }

    private Map<String, SimpleHttpChannelServerHandler> findHandlers() {
        Map<String, SimpleHttpChannelServerHandler> handlers = new HashMap<>();
        List<Class<?>> handlerClasses = ReflectionUtil.find(Application.PROJECT_PACKAGE,
                clazz -> Arrays.asList(clazz.getInterfaces()).contains(SimpleHttpChannelServerHandler.class) &&
                        clazz.isAnnotationPresent(HttpHandler.class));
        for (Class<?> c : handlerClasses) {
            try {
                handlers.put(c.getAnnotation(HttpHandler.class).value(), (SimpleHttpChannelServerHandler) c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return handlers;
    }

    public static void main(String[] args) {
        //System.out.println(new ServerChannelInitializer().findHandlers());
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new RequestSizeChannelInboundHandler());
        //ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new HttpRequestDecoder());
        ch.pipeline().addLast(new RequestHttpInfoChannelInboundHandler());
        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast(new ResponseSizeChannelOutboundHandler());
        ch.pipeline().addLast(new ChannelInboundMediator(this.handlers));
    }
}
