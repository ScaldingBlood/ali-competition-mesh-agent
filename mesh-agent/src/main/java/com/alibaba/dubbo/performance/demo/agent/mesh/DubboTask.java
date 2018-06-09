package com.alibaba.dubbo.performance.demo.agent.mesh;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcInvocation;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcResponse;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.context.request.async.DeferredResult;

public class DubboTask implements Runnable {
    private ChannelHandlerContext ctx;
    private AgentRequest request;
    private RpcResponse response;
    private RpcClient client;
    public DubboTask(ChannelHandlerContext ctx, AgentRequest request, RpcResponse response, RpcClient client) {
        this.ctx = ctx;
        this.request = request;
        this.response = response;
        this.client = client;
    }
    public void run() {
        try {
//            System.out.println(System.currentTimeMillis());
//            DeferredResult<Integer> result = client.invoke((RpcInvocation) request.getData());
            ctx.writeAndFlush(response);
//            System.out.println(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
