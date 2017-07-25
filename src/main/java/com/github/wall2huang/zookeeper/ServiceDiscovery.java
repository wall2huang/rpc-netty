package com.github.wall2huang.zookeeper;/**
 * Created by Administrator on 2017/7/24.
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author : Administrator
 * 服务发现器，一旦字服务发生改变，需要重新读取zk上所有服务的节点状态，这个点肯定是需要优化的
 **/
public class ServiceDiscovery
{
    @Autowired
    private CuratorFramework zkClient;

    private List<String> dataList;

    public ServiceDiscovery() throws Exception
    {
        init();
    }

    /**
     * 服务发现
     * @return
     */
    public String discover()
    {
        String data = null;
        int size = dataList.size();
        if (size > 0)
        {
            if (size == 1)
            {
                data = dataList.get(0);
            }
            else
            {
                // TODO: 2017/7/24 ???
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        return data;
    }

    private void init() throws Exception
    {
        initDataList();
        //监听本节点的变化
        final NodeCache nodeCache = new NodeCache(zkClient, "/biz/");
        nodeCache.getListenable()
                .addListener(new NodeCacheListener()
                {
                    @Override
                    public void nodeChanged() throws Exception
                    {
                        initDataList();
                    }
                });
    }

    private void initDataList() throws Exception
    {
        List<String> services = zkClient.getChildren().forPath("/biz/");
        ArrayList<String> dataList = new ArrayList<>();
        for (String service : services)
        {
            byte[] bytes = zkClient.getData().forPath("/biz/" + service);
            dataList.add(new String(bytes));
        }
        this.dataList = dataList;
    }


}