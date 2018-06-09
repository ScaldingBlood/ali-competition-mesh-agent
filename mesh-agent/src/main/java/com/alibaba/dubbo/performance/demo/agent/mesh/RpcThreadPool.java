package com.alibaba.dubbo.performance.demo.agent.mesh;

import java.util.concurrent.*;

public class RpcThreadPool {
    public static Executor getExecutor(int threads, int queues) {
        String name = "RpcThreadPool";
//        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
//                queues == 0 ? new SynchronousQueue<Runnable>()
//                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
//                        : new LinkedBlockingQueue<Runnable>(queues)),
//                new NamedThreadFactory(name, true));
        return new ThreadPoolExecutor(threads, threads, 6, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }
}