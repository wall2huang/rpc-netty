package com.github.wall2huang.configuration;/**
 * Created by Administrator on 2017/7/19.
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
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

}
