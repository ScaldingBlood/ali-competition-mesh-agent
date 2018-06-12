package com.alibaba.dubbo.performance.demo.agent.mesh.consumer;

import com.alibaba.dubbo.performance.demo.agent.mesh.provider.AgentConnectManager;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;

import java.util.List;
import java.util.Random;

public class ConsumerAgentServer {
    private static final int port = Integer.valueOf(System.getProperty("server.port"));
    private IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
    private List<Endpoint> endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
    private Random random = new Random();

    private ConsumerAgentClient consumerAgentClient = new ConsumerAgentClient();

    public ConsumerAgentServer() throws Exception {}

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
//                            pipeline.addLast(new HttpServerKeepAliveHandler());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 4));
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpConsumerHandler(endpoints.get(random.nextInt(endpoints.size())), consumerAgentClient));
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
