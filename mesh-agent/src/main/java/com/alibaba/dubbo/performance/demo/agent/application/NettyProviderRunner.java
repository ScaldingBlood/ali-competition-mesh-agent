package com.alibaba.dubbo.performance.demo.agent.application;

import com.alibaba.dubbo.performance.demo.agent.mesh.RpcProviderServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyProviderRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(NettyProviderRunner.class);

    @Override
    public void run(String... args) {
        if(System.getProperty("type").equals("provider")) {
            new RpcProviderServer().start();
        }
    }
}
