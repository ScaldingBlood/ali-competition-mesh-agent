package com.alibaba.dubbo.performance.demo.agent.dubbo;


import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object dubboResp) {
        ChannelHolder.channel.writeAndFlush(dubboResp);
    }
}

//public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
//        ChannelHolder.channel.writeAndFlush(response);
//    }
//}

