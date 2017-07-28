package com.github.wall2huang.configuration;/**
 * Created by Administrator on 2017/7/28.
 */

import com.github.wall2huang.core.RpcProxy;
import com.github.wall2huang.zookeeper.ServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : Administrator
 **/
@Configuration
public class SpringClientConfig
{
    /** 无论是客户端还是服务端都需要zkClient **/
    @Bean
    public CuratorFramework initZkClient()
    {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.129:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    @Bean
    public ServiceDiscovery initServiceDiscovery(CuratorFramework zkClient) throws Exception
    {
        return new ServiceDiscovery(zkClient);
    }

    @Bean
    public RpcProxy initRpcProxy(ServiceDiscovery serviceDiscovery)
    {
        return new RpcProxy(serviceDiscovery);
    }

}
