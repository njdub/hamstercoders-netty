package com.hamstercoders.netty.dub;

import com.hamstercoders.netty.dub.server.Server;

/**
 * Created on 19-Jul-15.
 *
 * @author Nazar Dub
 */
public class Application {
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = Server.DEFAULT_PORT;
        }
        new Server(port).run();
    }
}
