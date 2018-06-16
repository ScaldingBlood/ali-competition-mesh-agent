package com.alibaba.dubbo.performance.demo.agent.dubbo;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;

import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
//        ChannelHolder.channelMap.get(response.getRequestId()).writeAndFlush(response);
//        ChannelHolder.channelMap.remove(response.getRequestId());
        ChannelHolder.rpcChannels.get(Long.valueOf(response.getRequestId()) % 2).writeAndFlush(response);
    }
}
