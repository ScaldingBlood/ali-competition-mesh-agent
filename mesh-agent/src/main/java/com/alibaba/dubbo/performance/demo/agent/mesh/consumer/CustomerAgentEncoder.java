package com.alibaba.dubbo.performance.demo.agent.mesh.consumer;

import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CustomerAgentEncoder extends MessageToByteEncoder<AgentRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, AgentRequest request, ByteBuf out) throws Exception {
        if(null == request){
            throw new Exception("msg is null");
        }
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(request.getData());
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        out.writeInt(bytes.length);
        out.writeLong(request.getId());
        out.writeBytes(bytes);
    }
}
