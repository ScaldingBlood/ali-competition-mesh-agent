package com.alibaba.dubbo.performance.demo.agent.mesh.consumer;

import com.alibaba.dubbo.performance.demo.agent.dubbo.DubboRespDecoder;
import com.alibaba.dubbo.performance.demo.agent.dubbo.DubboRpcDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class AgentClientInitializer extends ChannelInitializer<SocketChannel> {
    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_OFFSET = 12;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 4;

//    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
//    private static final int LENGTH_FIELD_OFFSET = 0;
//    private static final int LENGTH_FIELD_LENGTH = 4;
//    private static final int LENGTH_ADJUSTMENT = 8;
//    private static final int INITIAL_BYTES_TO_STRIP = 4;
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
//        pipeline.addLast(new CustomerAgentDecoder());
        pipeline.addLast(new DubboRespDecoder());
//        pipeline.addLast(new DubboRpcDecoder());
        pipeline.addLast(new CustomerAgentEncoder());
        pipeline.addLast(new ReadTimeoutHandler(6));
        pipeline.addLast(new AgentClientHandler());
    }
}
