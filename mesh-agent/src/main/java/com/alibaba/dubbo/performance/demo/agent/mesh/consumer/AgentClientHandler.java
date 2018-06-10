package com.alibaba.dubbo.performance.demo.agent.mesh.provider;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.DeferredResponseHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
        String requestId = response.getRequestId();
        DeferredResponseHolder.resMap.get(requestId).setResult(Integer.valueOf(new String(response.getBytes())));
        DeferredResponseHolder.resMap.remove(requestId);
    }
}
