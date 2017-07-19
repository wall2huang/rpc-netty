package com.github.wall2huang.service;/**
 * Created by Administrator on 2017/7/17.
 */

import com.github.wall2huang.annotation.RpcService;
import com.github.wall2huang.interfaces.IHelloService;

/**
 * author : Administrator
 **/
@RpcService("helloService")
public class HelloService implements IHelloService
{
    @Override
    public String hello(String name)
    {
        return "hello" + name;
    }
}
