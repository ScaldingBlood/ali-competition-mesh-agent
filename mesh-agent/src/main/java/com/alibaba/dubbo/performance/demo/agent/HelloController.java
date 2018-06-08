package com.alibaba.dubbo.performance.demo.agent;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcFuture;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcRequestHolder;
import com.alibaba.dubbo.performance.demo.agent.mesh.AgentConnectManager;
import com.alibaba.dubbo.performance.demo.agent.mesh.ConsumerAgentClient;
import com.alibaba.dubbo.performance.demo.agent.mesh.model.DeferredResponseHolder;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);
    
    private IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));

//    private RpcClient rpcClient = new RpcClient(registry);
    private Random random = new Random();
    private List<Endpoint> endpoints = null;
    private Object lock = new Object();
//    private OkHttpClient httpClient = new OkHttpClient();
    private ConsumerAgentClient consumerClient = new ConsumerAgentClient();

    private AgentConnectManager connectManager = new AgentConnectManager();
    private ConcurrentHashMap<String, Integer> resMap = new ConcurrentHashMap<>();

    {
        if (null == endpoints){
            synchronized (lock){
                if (null == endpoints){
                    try {
                        if(System.getProperty("type").equals("consumer")) {
                            endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
                            for(Endpoint endpoint : endpoints) {
                                connectManager.getChannel(endpoint.getHost(), endpoint.getPort());
//                                consumerClient.sendRequest(endpoint, "com.alibaba.dubbo.performance.demo.provider.IHelloService", "hash", "Ljava/lang/String;", "s");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @RequestMapping(value = "")
    public Object invoke(@RequestParam("interface") String interfaceName,
                         @RequestParam("method") String method,
                         @RequestParam("parameterTypesString") String parameterTypesString,
                         @RequestParam("parameter") String parameter) throws Exception {
        String type = System.getProperty("type");   // 获取type参数
         if ("consumer".equals(type)){
            return consumer(interfaceName,method,parameterTypesString,parameter);
        }
        // should start netty before
//        else if ("provider".equals(type)){
//            return provider(interfaceName,method,parameterTypesString,parameter);
//        }
        else {
            return "Environment variable type is needed to set to provider or consumer.";
        }
    }

//    public byte[] provider(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {
//        Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
//        return (byte[]) result;
//    }

    public DeferredResult<Integer> consumer(String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {
        // 简单的负载均衡，随机取一个
        Endpoint endpoint = endpoints.get(random.nextInt(endpoints.size()));

        //netty consumer client send request
        String id = consumerClient.sendRequest(endpoint, interfaceName, method, parameterTypesString, parameter);
//        RpcFuture future = RpcRequestHolder.get(id);
        DeferredResult<Integer> result = new DeferredResult<>();
        DeferredResponseHolder.resMap.put(id, result);
//        Callable<Integer> callback = () -> Integer.valueOf(new String((byte[])future.get()));
//        System.out.println(System.currentTimeMillis() + "after");
        return result;

//        String url =  "http://" + endpoint.getHost() + ":" + endpoint.getPort();
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("interface",interfaceName)
//                .add("method",method)
//                .add("parameterTypesString",parameterTypesString)
//                .add("parameter",parameter)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//            byte[] bytes = response.body().bytes();
//            String s = new String(bytes);
//            return Integer.valueOf(s);
//        }
    }
}
