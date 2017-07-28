package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/24.
 */

import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import com.github.wall2huang.zookeeper.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * author : Administrator
 **/
public class RpcProxy
{

    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(ServiceDiscovery serviceDiscovery)
    {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> interfacesCls)
    {
        return (T) Proxy.newProxyInstance(interfacesCls.getClassLoader(),
                new Class[]{interfacesCls}, new InvocationHandler()
                {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                    {
                        RpcRequest rpcRequest = new RpcRequest();
                        rpcRequest.setRequestId(UUID.randomUUID().toString());
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameterType(method.getParameterTypes());
                        rpcRequest.setParameters(args);

                        String serverAddress = "";
                        if (serviceDiscovery != null)
                        {
                            serverAddress = serviceDiscovery.discover();
                        }
                        /** 服务名加端口 **/
                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        RpcClient client = new RpcClient(host, port);
                        RpcResponse response = client.send(rpcRequest);
                        if (response.getException() != null)
                        {
                            throw response.getException();
                        } else
                        {
                            return response.getResult();
                        }
                    }
                });
    }
}
