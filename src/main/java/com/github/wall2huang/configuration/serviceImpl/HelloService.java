package com.github.wall2huang.configuration.serviceImpl;/**
 * Created by Administrator on 2017/7/17.
 */

import com.github.wall2huang.configuration.annotation.RpcService;
import com.github.wall2huang.configuration.service.IHelloService;

/**
 * author : Administrator
 **/
@RpcService(IHelloService.class)
public class HelloService implements IHelloService
{
    @Override
    public String hello(String name)
    {
        return "hello" + name;
    }
}
