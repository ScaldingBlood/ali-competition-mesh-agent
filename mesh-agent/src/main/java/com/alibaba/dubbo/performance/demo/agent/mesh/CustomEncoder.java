package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.mesh.model.CustomMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CustomEncoder extends MessageToByteEncoder<CustomMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CustomMsg msg, ByteBuf out) throws Exception {
        if(null == msg){
            throw new Exception("msg is null");
        }

        byte[] body = msg.getBody();
        out.writeInt(body.length);
        out.writeBytes(body);
    }
}
