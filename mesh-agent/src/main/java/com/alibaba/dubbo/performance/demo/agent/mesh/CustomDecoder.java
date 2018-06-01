package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.mesh.model.CustomMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class CustomDecoder extends LengthFieldBasedFrameDecoder {
    private static final int HEADER_SIZE = 4;

    private int len;

    private byte[] body;

    public CustomDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                         int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(in == null)
            return null;
        if(in.readableBytes() < HEADER_SIZE) {
            throw new RuntimeException("header length err");
        }
        len = in.readInt();
        if(in.readableBytes() < len) {
            throw new RuntimeException("body length err");
        }
        ByteBuf buf = in.readBytes(len);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        body = req;

        CustomMsg msg = new CustomMsg(len, body);
        return msg;
    }
}
