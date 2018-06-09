package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcInvocation;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.context.request.async.DeferredResult;

public class DubboTask implements Runnable {
    private AgentRequest request;
    private RpcClient client;
    public DubboTask(AgentRequest request, RpcClient client) {
        this.request = request;
        this.client = client;
    }
    public void run() {
        try {
            client.invoke((RpcInvocation) request.getData(), request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
