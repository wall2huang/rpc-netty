package com.github.wall2huang.zookeeper;/**
 * Created by Administrator on 2017/7/24.
 */

import com.github.wall2huang.configuration.Constant;
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
    private CuratorFramework zkClient;

    private List<String> dataList;

    public ServiceDiscovery(CuratorFramework zkClient) throws Exception
    {
        this.zkClient = zkClient;
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
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        return data;
    }

    private void init() throws Exception
    {
        initDataList();
        //监听本节点的变化
        final NodeCache nodeCache = new NodeCache(zkClient, Constant.ZK_DATA_PATH);
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
        /** 获得/biz上的子节点**/
        List<String> nodes = zkClient.getChildren().forPath(Constant.ZK_REGISTER);
        ArrayList<String> dataList = new ArrayList<>();
        /** 每个子节点记载着服务的地址 **/
        for (String node : nodes)
        {
            byte[] bytes = zkClient.getData().forPath(Constant.ZK_REGISTER + "/" + node);
            dataList.add(new String(bytes));
        }
        this.dataList = dataList;
    }


}
