package com.alibaba.dubbo.performance.demo.agent.mesh;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgentConnectManager {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(8);

    private Bootstrap bootstrap;

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    private Object lock = new Object();

    public Channel getChannel(String host, int port) throws Exception {
        if (null != channelMap.get(host)) {
            Channel channel = channelMap.get(host);
            if(!channel.isOpen()) {
                channelMap.remove(host);
            }
//            if(!channel.isActive()) {
//                channelMap.remove(host);
//            }
        }

        if (null == bootstrap) {
            synchronized (lock) {
                if (null == bootstrap) {
                    initBootstrap();
                }
            }
        }

        if (null == channelMap.get(host)) {
            synchronized (lock){
                if (null == channelMap.get(host)){
                    channelMap.put(host, bootstrap.connect(host, port).sync().channel());
                }
            }
        }

        return channelMap.get(host);
    }

    private void initBootstrap() {
        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new AgentClientInitializer());
    }
}
