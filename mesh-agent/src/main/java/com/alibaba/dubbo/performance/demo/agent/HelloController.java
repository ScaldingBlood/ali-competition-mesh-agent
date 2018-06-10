//package com.alibaba.dubbo.performance.demo.agent;
//
//import com.alibaba.dubbo.performance.demo.agent.mesh.provider.AgentConnectManager;
//import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.ConsumerAgentClient;
//import com.alibaba.dubbo.performance.demo.agent.mesh.model.DeferredResponseHolder;
//import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
//import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
//import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.async.DeferredResult;
//
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//
//@RestController
//public class HelloController {
//
//    private Logger logger = LoggerFactory.getLogger(HelloController.class);
//
//    private IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
//
////    private RpcClient rpcClient = new RpcClient(registry);
//    private Random random = new Random();
//    private List<Endpoint> endpoints = null;
////    private OkHttpClient httpClient = new OkHttpClient();
//    private ConsumerAgentClient consumerClient = new ConsumerAgentClient();
//
//    private AgentConnectManager connectManager = new AgentConnectManager();
//
//
//    @RequestMapping(value = "")
//    public Object invoke(@RequestParam("interface") String interfaceName,
//                         @RequestParam("method") String method,
//                         @RequestParam("parameterTypesString") String parameterTypesString,
//                         @RequestParam("parameter") String parameter) throws Exception {
//        String type = System.getProperty("type");   // 获取type参数
//         if ("consumer".equals(type)){
//            return consumer(interfaceName,method,parameterTypesString,parameter);
//        }
//        // should start netty before
////        else if ("provider".equals(type)){
////            return provider(interfaceName,method,parameterTypesString,parameter);
////        }
//        else {
//            return "Environment variable type is needed to set to provider or consumer.";
//        }
//    }
//
////    public byte[] provider(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {
////        Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
////        return (byte[]) result;
////    }
//
//    public DeferredResult<Integer> consumer(String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {
//        // 简单的负载均衡，随机取一个
//        Endpoint endpoint = endpoints.get(random.nextInt(endpoints.size()));
//
//        //netty consumer client send request
//        String id = consumerClient.sendRequest(endpoint, interfaceName, method, parameterTypesString, parameter);
////        RpcFuture future = RpcRequestHolder.get(id);
//        DeferredResult<Integer> result = new DeferredResult<>();
//        DeferredResponseHolder.resMap.put(id, result);
////        Callable<Integer> callback = () -> Integer.valueOf(new String((byte[])future.get()));
////        System.out.println(System.currentTimeMillis() + "after");
//        return result;
//
////        String url =  "http://" + endpoint.getHost() + ":" + endpoint.getPort();
////
////        RequestBody requestBody = new FormBody.Builder()
////                .add("interface",interfaceName)
////                .add("method",method)
////                .add("parameterTypesString",parameterTypesString)
////                .add("parameter",parameter)
////                .build();
////
////        Request request = new Request.Builder()
////                .url(url)
////                .post(requestBody)
////                .build();
////
////        try (Response response = httpClient.newCall(request).execute()) {
////            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
////            byte[] bytes = response.body().bytes();
////            String s = new String(bytes);
////            return Integer.valueOf(s);
////        }
//    }
//}
