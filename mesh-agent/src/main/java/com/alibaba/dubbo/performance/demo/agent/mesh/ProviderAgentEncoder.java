package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.CustomMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
