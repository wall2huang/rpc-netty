package com.github.wall2huang;/**
 * Created by Administrator on 2017/7/27.
 */

import com.github.wall2huang.configuration.SpringClientConfig;
import com.github.wall2huang.core.RpcProxy;
import com.github.wall2huang.configuration.service.IHelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * author : Administrator
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringClientConfig.class)
public class RpcClientTest
{

    @Autowired
    private RpcProxy rpcProxy;

    @Test
    public void test()
    {
        if (rpcProxy == null)
        {
            System.out.println("rpcProxy没有初始化成功");
        }else
        {
            IHelloService helloService = rpcProxy.create(IHelloService.class);
            System.out.println(helloService.hello("黄展东"));
        }

    }
}
