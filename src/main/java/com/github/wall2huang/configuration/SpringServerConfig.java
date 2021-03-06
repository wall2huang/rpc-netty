package com.github.wall2huang.configuration;/**
 * Created by Administrator on 2017/7/19.
 */

import com.github.wall2huang.configuration.service.IHelloService;
import com.github.wall2huang.configuration.serviceImpl.HelloService;
import com.github.wall2huang.core.RpcServer;
import com.github.wall2huang.zookeeper.ServiceRegister;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * author : Administrator
 **/
@Configuration
@ComponentScan
public class SpringServerConfig
{
    @Bean
    public CuratorFramework initZkClient()
    {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.129:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    @Bean
    public ServiceRegister initServiceRegister(CuratorFramework zkClient)
    {
        return new ServiceRegister(zkClient);
    }

    @Bean
    public RpcServer initRpcServer(ServiceRegister serviceRegister)
    {
        return new RpcServer(Constant.SERVER_ADDRESS, serviceRegister);
    }

    @Bean
    public IHelloService initHelloService()
    {
        return new HelloService();
    }





}
