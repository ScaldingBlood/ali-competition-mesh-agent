package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.JsonUtils;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcFuture;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcInvocation;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcRequestHolder;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import io.netty.channel.Channel;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RpcConsumerClient {
    private AgentConnectManager connectManager = new AgentConnectManager();

    public Object sendRequest(Endpoint endpoint, String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {
        Channel channel = connectManager.getChannel(endpoint.getHost(), endpoint.getPort());
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(method);
        invocation.setAttachment("path", interfaceName);
        invocation.setParameterTypes(parameterTypesString);    // Dubbo内部用"Ljava/lang/String"来表示参数类型是String

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        JsonUtils.writeObject(parameter, writer);
        invocation.setArguments(out.toByteArray());

        AgentRequest agentRequest = new AgentRequest();
        agentRequest.setData(invocation);

        RpcFuture rpcFuture = new RpcFuture();
        RpcRequestHolder.put(String.valueOf(agentRequest.getId()), rpcFuture);

        channel.writeAndFlush(agentRequest);

        Object res = null;
        try {
            res = rpcFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
