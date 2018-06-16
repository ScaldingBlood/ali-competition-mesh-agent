package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
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
    private final int size;


    public ConsumerAgentClient() throws Exception {
        registry = new EtcdRegistry(System.getProperty("etcd.url"));
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        connectManager = new AgentConnectManager();
        for(Endpoint e : endpoints) {
            for(int i=0; i < 2; i++) {
                Channel channel = connectManager.getChannel(e.getHost(), e.getPort(), i);
                channelList.add(channel);

                ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>(180);
                ChannelHolder.maps.put(channel, map);
                mapList.add(map);
            }
        }
        this.size = endpoints.size();
    }

    public void sendRequest(String interfaceName, String method, String parameterTypesString, String parameter, Channel targetChannel) throws Exception {
        long id = atomicLong.getAndIncrement();

        ProtoRequest.Request.Builder builder = ProtoRequest.Request.newBuilder();
        builder.setId(id);
        builder.setInterfaceNama(interfaceName);
        builder.setMethodName(method);
        builder.setParameterTypes(parameterTypesString);
        builder.setArguments(parameter);

        int pos = (int)(id % size);
        int start = (int)(id % 2);
        int tmp = mapList.get(pos * 2 + start).size();
        for(int i = 0; i < size; i++) {
            int res = mapList.get(i * 2 + start).size();
            if(res < tmp) {
                tmp = res;
                pos = i;
            }
        }

        mapList.get(pos * 2 + start).put(String.valueOf(id), targetChannel);

        channelList.get(pos * 2 + start).writeAndFlush(builder.build());
//        System.out.println(System.currentTimeMillis());///////////////
    }
}
