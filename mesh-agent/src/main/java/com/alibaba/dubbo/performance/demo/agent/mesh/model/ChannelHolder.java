package com.alibaba.dubbo.performance.demo.agent.mesh.model;

import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHolder {
    public static ConcurrentHashMap<Channel, ConcurrentHashMap<String, Channel>> maps = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Channel> rpcChannels = new ConcurrentHashMap<>();
}
