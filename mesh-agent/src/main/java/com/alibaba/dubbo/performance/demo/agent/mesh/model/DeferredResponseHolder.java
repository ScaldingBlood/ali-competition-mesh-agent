package com.alibaba.dubbo.performance.demo.agent.mesh.model;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;

public class DeferredResponseHolder {
    public static ConcurrentHashMap<String, DeferredResult<Integer>> resMap = new ConcurrentHashMap<>(2048);
}
