package com.alibaba.dubbo.performance.demo.agent.dubbo;


import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object dubboResp) {
        ChannelHolder.channel.writeAndFlush(dubboResp);
    }
}
