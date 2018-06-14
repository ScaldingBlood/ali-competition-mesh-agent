package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerAgentClient {
    private AgentConnectManager connectManager;

    private IRegistry registry;
    private List<Endpoint> endpoints;
    private List<Channel> channelList = new ArrayList<>();
    private List<ConcurrentHashMap<String, Channel>> mapList = new ArrayList<>();
    private static AtomicLong atomicLong = new AtomicLong();

    public ConsumerAgentClient() throws Exception {
        registry = new EtcdRegistry(System.getProperty("etcd.url"));
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        connectManager = new AgentConnectManager();
        for(Endpoint e : endpoints) {
            Channel channel = connectManager.getChannel(e.getHost(), e.getPort());
            channelList.add(channel);

            ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();
            ChannelHolder.maps.put(channel, map);
            mapList.add(map);
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
        int pos = 0;
        for(int i = 0; i < channelList.size(); i++) {
            int res = ChannelHolder.maps.get(channelList.get(i)).size();
            if(res < tmp) {
                tmp = res;
                pos = i;
            }
        }

        ChannelHolder.maps.get(channelList.get(pos)).put(String.valueOf(id), targetChannel);

        channelList.get(pos).writeAndFlush(builder.build());
//        System.out.println(System.currentTimeMillis());///////////////
    }
}
