package com.alibaba.dubbo.performance.demo.agent.mesh;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class ProviderAgentServer {
    private final int port = Integer.valueOf(System.getProperty("agent.port"));
    private static final int MAX_FRAME_LENGTH = 4 * 1027;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new ProviderAgentDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP,false));
                            pipeline.addLast(new ProviderAgentEncoder());
                            pipeline.addLast(new AgentServerHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
