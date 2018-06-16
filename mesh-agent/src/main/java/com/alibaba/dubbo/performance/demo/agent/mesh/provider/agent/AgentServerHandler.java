package com.alibaba.dubbo.performance.demo.agent.mesh.provider.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class AgentServerHandler extends SimpleChannelInboundHandler<ProtoRequest.Request> {
    private RpcClient rpcClient;

    public AgentServerHandler(RpcClient client) {
        this.rpcClient = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtoRequest.Request request) throws  Exception {
//        System.out.println(System.currentTimeMillis() + "hello");//////////////
        Channel channel = ChannelHolder.rpcChannels.get(request.getId() % 2);
        if(channel == null || !channel.isActive())
            ChannelHolder.rpcChannels.put(request.getId() % 2, channelHandlerContext.channel());
        rpcClient.invoke(request.getInterfaceNama(),
                request.getMethodName(),
                request.getParameterTypes(),
                request.getArguments(),
                request.getId());
    }


//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AgentRequest request) throws  Exception {
////        System.out.println(System.currentTimeMillis() + "hello");//////////////
//        long requestId = request.getId();
////        ChannelHolder.channelMap.put(String.valueOf(requestId), channel);
//        if(ChannelHolder.channel == null || !ChannelHolder.channel.isActive())
//            ChannelHolder.channel = channelHandlerContext.channel();
//        rpcClient.invoke((RpcInvocation) request.getData(), requestId);
//    }
}
