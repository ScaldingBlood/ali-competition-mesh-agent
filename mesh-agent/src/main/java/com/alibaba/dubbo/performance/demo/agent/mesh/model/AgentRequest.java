package com.alibaba.dubbo.performance.demo.agent.mesh.model;

import java.util.concurrent.atomic.AtomicLong;

public class AgentRequest {
    private static AtomicLong atomicLong = new AtomicLong();
    private long id;
    private Object mData;

    public AgentRequest(){
        id = atomicLong.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object mData) {
        this.mData = mData;
    }
}
