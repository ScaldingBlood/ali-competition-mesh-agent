package com.alibaba.dubbo.performance.demo.agent.mesh.consumer;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.DeferredResponseHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class AgentClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) {
        String requestId = response.getRequestId();
        ByteBuf buf = Unpooled.copiedBuffer(response.getBytes());
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        resp.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        resp.headers().set(CONTENT_LENGTH, buf.readableBytes());
        ChannelHolder.channelMap.get(requestId).writeAndFlush(resp);
        ChannelHolder.channelMap.remove(requestId);
//        DeferredResponseHolder.resMap.get(requestId).setResult(Integer.valueOf(new String(response.getBytes())));
//        DeferredResponseHolder.resMap.remove(requestId);
    }
}
