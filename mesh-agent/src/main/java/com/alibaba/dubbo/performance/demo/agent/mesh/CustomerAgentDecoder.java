package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Bytes;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Arrays;

public class CustomerAgentDecoder extends LengthFieldBasedFrameDecoder {
    private static final int HEADER_SIZE = 12;

    private int len;

    public CustomerAgentDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                         int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception();
        }

        len = in.readInt() + 8;

        if (in.readableBytes() < len) {
            throw new Exception();
        }
        ByteBuf buf = in.readBytes(len);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);


        byte[] requestIdBytes = Arrays.copyOfRange(data,0,8);
        long requestId = Bytes.bytes2long(requestIdBytes,0);

        byte[] subArray = Arrays.copyOfRange(data,8, data.length);

        RpcResponse response = new RpcResponse();
        response.setRequestId(String.valueOf(requestId));
        response.setBytes(subArray);
        return response;
    }
}
