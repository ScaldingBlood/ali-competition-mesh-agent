package com.alibaba.dubbo.performance.demo.agent.mesh;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AgentConnectManager {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private Bootstrap bootstrap;
//    private Bootstrap bootstrap;
//    private Bootstrap bootstrap;


    private Channel channel;
    private Object lock = new Object();

    public Channel getChannel(String host, int port) throws Exception {
        if (null != channel) {
            return channel;
        }

        if (null == bootstrap) {
            synchronized (lock) {
                if (null == bootstrap) {
                    initBootstrap();
                }
            }
        }

        if (null == channel) {
            synchronized (lock){
                if (null == channel){
                    channel = bootstrap.connect(host, port).sync().channel();
                    //
                }
            }
        }

        return channel;
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
