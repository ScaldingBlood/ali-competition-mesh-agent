package com.alibaba.dubbo.performance.demo.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.ProtoResponse;

public class ProtoTest {
    private static ProtoResponse.Response createProtoResponse() {
        ProtoResponse.Response.Builder builder = ProtoResponse.Response.newBuilder();
        builder.setId(1);
        builder.setRes(20);
        return builder.build();
    }

    private static byte[] encode(ProtoResponse.Response response) {
        return response.toByteArray();
    }

    private static ProtoResponse.Response decode(byte[] bytes) throws Exception {
        return ProtoResponse.Response.parseFrom(bytes);
    }

    public static void main(String[] args) throws Exception {
        ProtoResponse.Response response = createProtoResponse();
        System.out.println(response.toString());

        ProtoResponse.Response newResp = decode(encode(response));
        System.out.println(newResp.toString());

        System.out.println(response.equals(newResp));
    }
}
