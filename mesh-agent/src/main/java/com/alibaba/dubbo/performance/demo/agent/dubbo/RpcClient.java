package com.alibaba.dubbo.performance.demo.agent.dubbo;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.JsonUtils;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.Request;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcInvocation;

import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RpcClient {
    private Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private ConnecManager connectManager;

    public RpcClient(IRegistry registry) {
        this.connectManager = new ConnecManager();
    }

    public RpcClient() {
        this.connectManager = new ConnecManager();
    }

    public void invoke(String interfaceName, String method, String parameterTypesString, String parameter, long requestId) throws Exception {

        Channel channel = connectManager.getChannel();

        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(method);
        invocation.setAttachment("path", interfaceName);
        invocation.setParameterTypes(parameterTypesString);    // Dubbo内部用"Ljava/lang/String"来表示参数类型是String

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
//        JsonUtils.writeObject(parameter, writer);
//        invocation.setArguments(out.toByteArray());
//        System.out.println(parameter);
//        System.out.println(new String(invocation.getArguments()));
//        System.out.println(("\"" + parameter + "\"\n").equals(new String(invocation.getArguments())));
//        writer.close();
        invocation.setArguments(("\"" + parameter + "\"\n").getBytes());

        Request request = new Request();
//        request.setVersion("2.0.0");
//        request.setTwoWay(true);
        request.setData(invocation);
        request.setId(requestId);

        channel.writeAndFlush(request);
    }

//    public void invoke(RpcInvocation invocation, long requestId) throws Exception {
//        Channel channel = connectManager.getChannel();
//        Request request = new Request();
//        request.setData(invocation);
//        request.setId(requestId);
//
//
//        channel.writeAndFlush(request);
//    }
}