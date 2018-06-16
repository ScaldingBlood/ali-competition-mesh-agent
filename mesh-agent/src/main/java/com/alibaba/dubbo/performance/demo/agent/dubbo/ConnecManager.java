package com.alibaba.dubbo.performance.demo.agent.dubbo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class ConnecManager {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(8);

    private Bootstrap bootstrap;

    private ConcurrentHashMap<Integer,Channel> channels = new ConcurrentHashMap<>();
    private Object lock = new Object();

    public ConnecManager() {
    }

    public Channel getChannel(Long requestId) throws Exception {
        int flag = (int)(requestId % 4);
        if (null != channels.get(flag)) {
            return channels.get(flag);
        }

        if (null == bootstrap) {
            synchronized (lock) {
                if (null == bootstrap) {
                    initBootstrap();
                }
            }
        }

        if (null == channels.get(flag)) {
            synchronized (lock){
                if (null == channels.get(flag)){
                    int port = Integer.valueOf(System.getProperty("dubbo.protocol.port"));
                    channels.put(flag, bootstrap.connect("127.0.0.1", port).sync().channel());
                }
            }
        }

        return channels.get(flag);
    }

    public void initBootstrap() {

        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }
}
