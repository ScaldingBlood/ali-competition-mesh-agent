package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Bytes;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class CustomerAgentDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);


        byte[] requestIdBytes = Arrays.copyOfRange(data,0,8);
        long requestId = Bytes.bytes2long(requestIdBytes,0);

        byte[] subArray = Arrays.copyOfRange(data,8, data.length);

        RpcResponse response = new RpcResponse();
        response.setRequestId(String.valueOf(requestId));
        response.setBytes(subArray);
        list.add(response);
    }
}
