package com.alibaba.dubbo.performance.demo.agent;


import com.alibaba.dubbo.performance.demo.agent.mesh.consumer.ConsumerAgentServer;
import com.alibaba.dubbo.performance.demo.agent.mesh.provider.ProviderAgentServer;

public class AgentApp {
    // agent会作为sidecar，部署在每一个Provider和Consumer机器上
    // 在Provider端启动agent时，添加JVM参数-Dtype=provider -Dserver.port=30000 -Ddubbo.protocol.port=20889
    // 在Consumer端启动agent时，添加JVM参数-Dtype=consumer -Dserver.port=20000
    // 添加日志保存目录: -Dlogs.dir=/path/to/your/logs/dir。请安装自己的环境来设置日志目录。

    public static void main(String[] args) throws Exception {
        if(System.getProperty("type").equals("provider")) {
            new ProviderAgentServer().start();
        } else {
            new ConsumerAgentServer().start();
        }
    }
}
