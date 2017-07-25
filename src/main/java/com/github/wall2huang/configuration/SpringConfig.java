package com.github.wall2huang.configuration;/**
 * Created by Administrator on 2017/7/19.
 */

import com.github.wall2huang.core.RpcProxy;
import com.github.wall2huang.core.RpcServer;
import com.github.wall2huang.zookeeper.ServiceDiscovery;
import com.github.wall2huang.zookeeper.ServiceRegister;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : Administrator
 **/
@Configuration
public class SpringConfig
{
    @Bean
    public CuratorFramework initZkClient()
    {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.129:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    @Bean
    public ServiceRegister initServiceRegister()
    {
        return new ServiceRegister();
    }

    @Bean
    public RpcServer initRpcServer()
    {
        return new RpcServer(Constant.SERVER_ADDRESS);
    }

    @Bean
    public ServiceDiscovery initServiceDiscovery() throws Exception
    {
        return new ServiceDiscovery();
    }

    @Bean
    public RpcProxy initRpcProxy()
    {
        return new RpcProxy(Constant.SERVER_ADDRESS);
    }



}
