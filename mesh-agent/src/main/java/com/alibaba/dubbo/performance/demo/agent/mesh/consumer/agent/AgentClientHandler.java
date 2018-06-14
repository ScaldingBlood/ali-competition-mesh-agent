package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
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
//        resp.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ConcurrentHashMap<String, Channel> map = ChannelHolder.maps.get(channelHandlerContext.channel());
        try {
            map.get(requestId).writeAndFlush(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.remove(requestId);
    }
}
