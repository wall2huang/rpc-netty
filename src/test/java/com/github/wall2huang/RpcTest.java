package com.github.wall2huang;/**
 * Created by Administrator on 2017/7/26.
 */

import com.github.wall2huang.core.RpcProxy;
import com.github.wall2huang.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * author : Administrator
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BootStrap.class})
public class RpcTest
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
            HelloService helloService = rpcProxy.create(HelloService.class);
            System.out.println(helloService.hello("黄展东"));
        }

    }

}
