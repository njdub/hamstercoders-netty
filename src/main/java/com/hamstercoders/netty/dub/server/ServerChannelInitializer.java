package com.hamstercoders.netty.dub.server;

import com.hamstercoders.netty.dub.server.core.ChannelInboundMediator;
import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.SimpleHttpChannelServerHandler;
import com.hamstercoders.netty.dub.utils.ClassFinderUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

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
        List<Class<?>> handlerClasses = ClassFinderUtil.find("com.hamstercoders",
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
        System.out.println(new ServerChannelInitializer().findHandlers());
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new ChannelInboundMediator(this.handlers));
    }
}
