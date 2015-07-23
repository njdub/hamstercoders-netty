package com.hamstercoders.netty.dub.server;

import com.hamstercoders.netty.dub.dao.HelloDao;
import com.hamstercoders.netty.dub.dao.SimpleHelloDao;
import com.hamstercoders.netty.dub.server.core.di.bean.Bean;
import com.hamstercoders.netty.dub.server.core.di.bean.Configuration;
import com.hamstercoders.netty.dub.dao.ServerInfoRepository;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.MessageSizeEstimator;

/**
 * Created on 22-Jul-15.
 *
 * @author Nazar Dub
 */
@Configuration
public class AppConfiguration {

    @Bean
    ServerInfoRepository defaultServerRepository() {
        return new ServerInfoRepository();
    }

    @Bean
    HelloDao defaultHelloDaoImpl() {
        return new SimpleHelloDao();
    }

    @Bean
    MessageSizeEstimator httpMessageEstimator() {
        return new DefaultMessageSizeEstimator(0);
    }
}
