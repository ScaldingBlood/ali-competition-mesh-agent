package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.channel.Channel;

import java.util.ArrayList;
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
    private final int channelSize;


    public ConsumerAgentClient() throws Exception {
        registry = new EtcdRegistry(System.getProperty("etcd.url"));
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        connectManager = new AgentConnectManager();
        for(Endpoint e : endpoints) {
            Channel channel = connectManager.getChannel(e.getHost(), e.getPort());
            channelList.add(channel);

            ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>(170);
            ChannelHolder.maps.put(channel, map);
            mapList.add(map);
        }
        this.channelSize = endpoints.size();
    }

    public void sendRequest(String interfaceName, String method, String parameterTypesString, String parameter, Channel targetChannel) throws Exception {
        long id = atomicLong.getAndIncrement();

        ProtoRequest.Request.Builder builder = ProtoRequest.Request.newBuilder();
        builder.setId(id);
        builder.setInterfaceNama(interfaceName);
        builder.setMethodName(method);
        builder.setParameterTypes(parameterTypesString);
        builder.setArguments(parameter);

        int pos = (int)(id % channelSize);
        int tmp = mapList.get(pos).size();
        for(int i = 0; i < channelSize && i != pos; i++) {
            int res = mapList.get(i).size();
            if(res < tmp) {
                tmp = res;
                pos = i;
            }
        }

        mapList.get(pos).put(String.valueOf(id), targetChannel);

        if(!channelList.get(pos).isWritable())
            channelList.set(pos, connectManager.getChannel(endpoints.get(pos).getHost(), endpoints.get(pos).getPort()));
        channelList.get(pos).writeAndFlush(builder.build());
//        System.out.println(System.currentTimeMillis());///////////////
    }
}
