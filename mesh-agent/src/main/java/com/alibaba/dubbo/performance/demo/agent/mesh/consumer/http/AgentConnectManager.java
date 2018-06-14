package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.agent.AgentClientInitializer;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
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
    private Map<String, Channel> channels = new HashMap<>();

    private Object lock = new Object();

    public Channel getChannel(String host, int port) throws Exception {
        if (null != channels.get(host)) {
            Channel channel = channels.get(host);
            if(!channel.isOpen()) {
                channels.remove(host);
            }
            return channel;
        }

        if (channels.get(host) == null){
            synchronized (lock){
                if (null == channels.get(host)){
                    Channel c = bootstrap.connect(host, port).sync().channel();
                    channels.put(host, c);
                }
            }
        }
        return channels.get(host);
    }

    public AgentConnectManager() {
        bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new AgentClientInitializer());
    }
}
