package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Bytes;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class ProviderAgentDecoder extends LengthFieldBasedFrameDecoder {
    private static final int HEADER_SIZE = 4;

    private int len;

    public ProviderAgentDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
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

        len = in.readInt();

        if (in.readableBytes() < len) {
            throw new Exception();
        }
        ByteBuf buf = in.readBytes(len);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        byte[] subArray = Arrays.copyOfRange(data,HEADER_SIZE, data.length);

        Object request = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(subArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            request = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return request;
    }
}
