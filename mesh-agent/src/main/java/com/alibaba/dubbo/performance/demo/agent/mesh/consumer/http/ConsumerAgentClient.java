package com.alibaba.dubbo.performance.demo.agent.mesh.consumer.http;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.*;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.AgentRequest;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.ChannelHolder;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerAgentClient {
    private AgentConnectManager connectManager;
//    private Logger logger = LoggerFactory.getLogger(ConsumerAgentClient.class);

    private IRegistry registry;
    private List<Endpoint> endpoints;
    private Map<String, Channel> channelList;
    private Map<String, ConcurrentHashMap<String, Channel>> mapList;
    private static AtomicLong atomicLong = new AtomicLong();


    public ConsumerAgentClient() throws Exception {
        registry = new EtcdRegistry(System.getProperty("etcd.url"));
        endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        connectManager = new AgentConnectManager();
        int[] size = new int[] {160, 180, 200}; // map initial size
        channelList = new ConcurrentHashMap<>();
        mapList = new ConcurrentHashMap<>();

        for(int i = 0; i < endpoints.size(); i++) {
            Channel channel = connectManager.getChannel(endpoints.get(i).getHost(), endpoints.get(i).getPort());
            ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>(size[0]);
            mapList.put(endpoints.get(i).getSize(), map);
            channelList.put(endpoints.get(i).getSize(), channel);
            ChannelHolder.maps.put(channel, map);
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

//        int pos = 0;
//        int tmp = mapList.get(0).size();
//        for(int i = 1; i < channelSize; i++) {
//            int res = mapList.get(i).size();
//            if(res < tmp) {
//                tmp = res;
//                pos = i;
//            }
//        }
        String size = "large";
        int tmp = (int)(id % 15);
        if(tmp < 3){
            size = "small";
        } else if(tmp < 8) {
            size = "medium";
        }
//        logger.info("Pos" + pos);
        mapList.get(size).put(String.valueOf(id), targetChannel);

        if(!channelList.get(size).isWritable())
            for(Endpoint e : endpoints)
                if(size.equals(e.getSize()))
                    channelList.put(size, connectManager.getChannel(e.getHost(), e.getPort()));
        channelList.get(size).writeAndFlush(builder.build());
//        System.out.println(System.currentTimeMillis());///////////////
    }
}
