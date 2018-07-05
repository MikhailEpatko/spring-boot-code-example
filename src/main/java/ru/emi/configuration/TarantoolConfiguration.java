package ru.emi.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tarantool.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

@Slf4j
@Configuration
public class TarantoolConfiguration {

    @Value("${spring.data.tarantool.username}")
    private String username;
    @Value("${spring.data.tarantool.password}")
    private String password;
    @Value("${spring.data.tarantool.host}")
    private String host;
    @Value("${spring.data.tarantool.port}")
    private int port;

    @Bean
    public TarantoolClientConfig tarantoolClientConfig() {
        final TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = this.username;
        config.password = this.password;
        config.useNewCall = true;
        return config;
    }

    @Bean
    public SocketChannelProvider socketChannelProvider() {
        return (retryNumber, lastError) -> {
            if (lastError != null) {
                log.error(lastError.toString());
            }
            try {
                return SocketChannel.open(new InetSocketAddress(host, port));
            } catch (IOException e) {
                log.error(e.toString());
                throw new IllegalStateException(e);
            }
        };
    }

    @Bean(destroyMethod = "close")
    @Autowired
    public TarantoolClient tarantoolClient(SocketChannelProvider socketChannelProvider, TarantoolClientConfig tarantoolClientConfig) {
        return new TarantoolClientImpl(socketChannelProvider, tarantoolClientConfig);
    }

    @Bean(destroyMethod = "close")
    @Autowired
    public TarantoolClientOps<Integer, List<?>, Object, List<?>> tarantoolSyncOps(TarantoolClient tarantoolClient) {
        return tarantoolClient.syncOps();
    }
}
