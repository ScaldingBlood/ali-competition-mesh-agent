package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class AgentClientInitializer extends ChannelInitializer<SocketChannel> {
    private static final int MAX_FRAME_LENGTH = 4 * 1026;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new CustomerAgentDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP,false));
        pipeline.addLast(new CustomerAgentEncoder());
        pipeline.addLast(new RpcClientHandler());
    }
}
