package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerAgentClient {
    private AgentConnectManager connectManager;

    private IRegistry registry;
    private List<Endpoint> endpoints;
    public HashMap<String, ConcurrentHashMap<String, Channel>> maps = new HashMap<>();
    private static AtomicLong atomicLong = new AtomicLong();

    public ConsumerAgentClient() throws Exception {
        registry = new EtcdRegistry(System.getProperty("etcd.url"));
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        connectManager = new AgentConnectManager();

        for(Endpoint endpoint : endpoints) {
            String host = endpoint.getHost();
            maps.put(host, new ConcurrentHashMap<>());
            connectManager.initBootstrap(endpoint.getHost(), maps.get(host));
        }

    }

    public void sendRequest(String interfaceName, String method, String parameterTypesString, String parameter, Channel targetChannel) throws Exception {
        long id = atomicLong.getAndIncrement();

        ProtoRequest.Request.Builder builder = ProtoRequest.Request.newBuilder();
        builder.setId(id);
        builder.setInterfaceNama(interfaceName);
        builder.setMethodName(method);
        builder.setParameterTypes(parameterTypesString);
        builder.setArguments(parameter);

        int tmp = Integer.MAX_VALUE;
        Endpoint endpoint = endpoints.get(0);
        for(Endpoint e : endpoints) {
            int res = maps.get(e.getHost()).size();
            if(res < tmp) {
                tmp = res;
                endpoint = e;
            }
        }

        Channel channel = connectManager.getChannel(endpoint.getHost(), endpoint.getPort());
        maps.get(endpoint.getHost()).put(String.valueOf(id), targetChannel);

        channel.writeAndFlush(builder.build());
//        System.out.println(System.currentTimeMillis());///////////////
    }
}
