package com.alibaba.dubbo.performance.demo.agent.mesh.provider;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.ProtoRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.provider.agent.AgentServerHandler;
import com.alibaba.dubbo.performance.demo.agent.mesh.provider.agent.ProviderAgentDecoder;
import com.alibaba.dubbo.performance.demo.agent.mesh.provider.agent.ProviderAgentEncoder;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;


public class ProviderAgentServer {
    private IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));

    private final int port = Integer.valueOf(System.getProperty("server.port"));
//    private static final int MAX_FRAME_LENGTH = 1024 * 4;
//    private static final int LENGTH_FIELD_LENGTH = 4;
//    private static final int LENGTH_FIELD_OFFSET = 0;
//    private static final int LENGTH_ADJUSTMENT = 8;
//    private static final int INITIAL_BYTES_TO_STRIP = 4;

    private RpcClient rpcClient = new RpcClient();

    public ProviderAgentServer() throws Exception {}

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(7);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
//                            pipeline.addLast(new ProviderAgentDecoder());
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(ProtoRequest.Request.getDefaultInstance()));
                            pipeline.addLast(new ProviderAgentEncoder());
                            pipeline.addLast(new AgentServerHandler(rpcClient));
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
