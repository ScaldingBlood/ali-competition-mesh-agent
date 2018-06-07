package com.alibaba.dubbo.performance.demo.agent;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class RequestSender {
    public static void main(String[] args) {
        String url = "http://127.0.0.1:8087/invoke";
        AsyncHttpClient asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient();
        ResponseEntity ok = new ResponseEntity("OK", HttpStatus.OK);
        ResponseEntity error = new ResponseEntity("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        DeferredResult<ResponseEntity> result = new DeferredResult<>();


        org.asynchttpclient.Request request = org.asynchttpclient.Dsl.get(url).build();
        long now = System.currentTimeMillis();
        for(int i = 0; i < 1; i++) {
            ListenableFuture<Response> responseFuture = asyncHttpClient.executeRequest(request);
            Runnable callback = () -> {
                try {
                    // 检查返回值是否正确,如果不正确返回500。有以下原因可能导致返回值不对:
                    // 1. agent解析dubbo返回数据不对
                    // 2. agent没有把request和dubbo的response对应起来
                    String value = responseFuture.get().getResponseBody();
                    if ("OK".equals(value)) {
                        System.out.print("time: ");
                        System.out.println(System.currentTimeMillis() - now);
                        result.setResult(ok);
                    } else {
                        result.setResult(error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            responseFuture.addListener(callback, null);
//            System.out.println(result.getResult());
        }
        System.out.println("END!");
    }
}
