package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.agent.AgentClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

public class AgentConnectManager {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(8);

    private Bootstrap bootstrap;
    private Map<String, Channel> channels = new HashMap<>();

    private Object lock = new Object();

    public Channel getChannel(String host, int port, long requestId) throws Exception {
        String key = host + (requestId % 2);
        if (null != channels.get(key)) {
            Channel channel = channels.get(key);
            if(!channel.isOpen()) {
                channels.remove(key);
            }
        }

        if (channels.get(key) == null){
            synchronized (lock){
                if (null == channels.get(key)){
                    Channel c = bootstrap.connect(host, port).sync().channel();
                    channels.put(key, c);
                }
            }
        }
        return channels.get(key);
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
