package com.alibaba.dubbo.performance.demo.agent.mesh.consumer;

import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http.ConsumerAgentClient;
import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http.HttpConsumerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class ConsumerAgentServer {
    private static final int port = Integer.valueOf(System.getProperty("server.port"));

    private ConsumerAgentClient consumerAgentClient = new ConsumerAgentClient();

    public ConsumerAgentServer() throws Exception {}

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(7);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
//                            pipeline.addLast(new HttpServerKeepAliveHandler());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 4));
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpConsumerHandler(consumerAgentClient));
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
