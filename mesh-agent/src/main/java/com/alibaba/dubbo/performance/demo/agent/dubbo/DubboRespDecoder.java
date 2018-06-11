package com.alibaba.dubbo.performance.demo.agent.dubbo;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Bytes;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class DubboRespDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        try {
            byte[] data = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(data);

            byte[] requestIdBytes = Arrays.copyOfRange(data, 0, 8);
            long requestId = Bytes.bytes2long(requestIdBytes, 0);

            byte[] subArray = Arrays.copyOfRange(data, 14, data.length-1);

            RpcResponse response = new RpcResponse();
            response.setRequestId(String.valueOf(requestId));
            response.setBytes(subArray);

            list.add(response);
        } finally {
            if (byteBuf.isReadable()) {
                byteBuf.discardReadBytes();
            }
        }
    }
}
