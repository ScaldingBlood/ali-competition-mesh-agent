package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentServerHandler extends SimpleChannelInboundHandler<AgentRequest> {
    private RpcClient rpcClient = new RpcClient();

//    @Override
//    public void channelActive(final ChannelHandlerContext ctx) {
//
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AgentRequest request) {
        String requestId = String.valueOf(request.getId());
        RpcFuture future = new RpcFuture();
        RpcRequestHolder.put(requestId, future);
        byte[] res = null;
        try {
            res = (byte[])rpcClient.invoke((RpcInvocation) request.getData());
        } catch(Exception e) {
            e.printStackTrace();
        }
        RpcResponse response = new RpcResponse();
        response.setBytes(res);
        response.setRequestId(String.valueOf(request.getId()));

        channelHandlerContext.writeAndFlush(response);
    }
}
