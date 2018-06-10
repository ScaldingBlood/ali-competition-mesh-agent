package com.alibaba.dubbo.performance.demo.agent.mesh.provider;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Bytes;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;

public class ProviderAgentDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);

        byte[] requestIdBytes = Arrays.copyOfRange(data,0,8);
        long requestId = Bytes.bytes2long(requestIdBytes,0);

        byte[] subArray = Arrays.copyOfRange(data,8, data.length);

        Object request = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(subArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            request = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        AgentRequest agentRequest = new AgentRequest();
        agentRequest.setId(requestId);
        agentRequest.setData(request);
        list.add(agentRequest);
    }
}
