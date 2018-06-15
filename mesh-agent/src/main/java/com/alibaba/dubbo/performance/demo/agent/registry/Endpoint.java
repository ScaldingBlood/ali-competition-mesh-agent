package com.alibaba.dubbo.performance.demo.agent.registry;

public class Endpoint {
    private final String host;
    private final int port;
    private final String size;

    public Endpoint(String host,int port, String size){
        this.host = host;
        this.port = port;
        this.size = size;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSize() {
        return size;
    }

    public String toString(){
        return host + ":" + port + "-" + size;
    }

    public boolean equals(Object o){
        if (!(o instanceof Endpoint)){
            return false;
        }
        Endpoint other = (Endpoint) o;
        return other.host.equals(this.host) && other.port == this.port;
    }

    public int hashCode(){
        return host.hashCode() + port;
    }
}
