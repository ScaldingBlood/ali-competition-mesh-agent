package com.alibaba.dubbo.performance.demo.agent.mesh.model;

public class CustomMsg {
    private int len;
    private byte[] body;

    public CustomMsg() {
    }

    public CustomMsg(int len, byte[] body) {
        this.len = len;
        this.body = body;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
