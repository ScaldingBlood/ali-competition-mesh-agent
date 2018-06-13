package com.alibaba.dubbo.performance.demo.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.ProtoRequest;

public class ProtoTest {
    private static ProtoRequest.Request createProtoRequest() {
        ProtoRequest.Request.Builder builder = ProtoRequest.Request.newBuilder();
        builder.setId(0L);
        builder.setMethodName("hash");
        builder.setInterfaceNama("com.alibaba.dubbo.performance.demo.provider.IHelloService");
        builder.setParameterTypes("Ljava/lang/String;");
        builder.setArguments("vExxsQK7104r5aEXOXqiKigZGGV9qglmCQMEZRtFjlyDHi3eI8IPDStazVF1aMGaUOMsBX9eCW2jX64ZcLPYa6O5sO6eAUMUXYIW0W93cokAeUxka3PnmjaPAJPHeU7tTA8HWNzkVv4TlmvNo5kN88UzeW0GajCHQuU5K1WglGVrght01wU5tpaeWm8OtnL91SkEHZpc2uMgXbsPrr8yAZNVIboItdXriBxgnFD2e1JpY6BvA0l7AHpQI20jAkK3xaDEh7qeuJQbEQF6Gj31sGzaWasuZ5cbWjZbGytZjT8bmNwVUm5lMn4o5FGnD1kjfawNkUIV2z9QjJdOgH0g4bY3OxAuFFI5iXQUzwDQQuK3Lh0DmXw9mi4SnZBGkukrsHOj9kriSCInK7SikOZuskTp6BAXsqR1gQjw5c1ZSZ8Gsxg2tK4lgyNQx3ERjjk9yK8Kbz2R6FKRTlWosSC1HFNPuBB5CVd959lTr8mwER0O21yy2SoqpKlKb9jkEoQXqPY4w69ARE38Bt5kPhnc65zfNTc3XZBnrlNNIWAONHLXQuBS5NkBrI07fhEZbQfPYmjH69S7GWpbLA8GN1BtMEuifi5IlnLied1AQxnMC5ME0bhIYZPY090KUVbhKnIEECEgjaaE07oi5vckoNPORKjXN1vydoTNJSWrI85lM7JIjkXdktCubtkomzGzoRYIAAcIP7hiPvPTpDjGRHCgYyr1Mbnl0hYt57MUszc99UW0hyMVUK6pjYOEiTjeWaItgawcUMtUAQfORerY7BHJg6zhq7Hxkn5IQWwNsSet9Uxo2SwaXfYzlRW6OMNyi");
        return builder.build();
    }

    private static byte[] encode(ProtoRequest.Request Request) {
        return Request.toByteArray();
    }

    private static ProtoRequest.Request decode(byte[] bytes) throws Exception {
        return ProtoRequest.Request.parseFrom(bytes);
    }

    public static void main(String[] args) throws Exception {
        ProtoRequest.Request Request = createProtoRequest();
        System.out.println(Request.toString());

        ProtoRequest.Request newResp = decode(encode(Request));
        System.out.println(newResp.toString());

        System.out.println(Request.equals(newResp));
    }
}
