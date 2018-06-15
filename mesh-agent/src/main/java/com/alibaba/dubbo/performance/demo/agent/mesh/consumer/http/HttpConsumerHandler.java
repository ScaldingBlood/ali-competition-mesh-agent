package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URLDecoder;

public class HttpConsumerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private ConsumerAgentClient consumerAgentClient;

    public HttpConsumerHandler(ConsumerAgentClient consumerAgentClient) {
        this.consumerAgentClient = consumerAgentClient;
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        byte[] content = new byte[request.content().readableBytes()];
        request.content().readBytes(content);
        String[] contentStr = URLDecoder.decode(new String(content), "UTF-8").split("&");
        consumerAgentClient.sendRequest(
                contentStr[0].split("=")[1],
                contentStr[1].split("=")[1],
                contentStr[2].split("=")[1],
                contentStr[3].endsWith("=") ? "" : contentStr[3].split("=")[1],
                channelHandlerContext.channel()
        );
    }
}
