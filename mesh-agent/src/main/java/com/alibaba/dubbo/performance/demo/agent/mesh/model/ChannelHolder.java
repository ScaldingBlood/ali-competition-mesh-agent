package com.alibaba.dubbo.performance.demo.agent.mesh.model;

import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHolder {
//    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    public static Channel channel;
}
