package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProviderAgentEncoder extends MessageToByteEncoder<RpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse response, ByteBuf out) throws Exception {
        if(null == response){
            throw new Exception("msg is null");
        }

        byte[] body = response.getBytes();
        out.writeInt(body.length);
        out.writeLong(Long.valueOf(response.getRequestId()));
        out.writeBytes(body);
    }
}
