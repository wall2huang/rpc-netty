package com.github.wall2huang.zookeeper;/**
 * Created by Administrator on 2017/7/19.
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * author : Administrator
 * 利用zk实现的服务注册器
 **/
public class ServiceRegister
{
    private String serviceName;
    private String path;

    @Autowired
    private CuratorFramework zkClient;

    public ServiceRegister(String serviceName)
    {
        this.serviceName = serviceName;
        this.path = "/biz/" + serviceName;
    }

    //注册服务名+内容
    public void register(String data) throws Exception
    {
        //现在先先写死服务注册的地址
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path, data.getBytes());
        //并没有监听这个服务结点

    }


}
