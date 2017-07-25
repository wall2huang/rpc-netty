package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/24.
 */

import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import com.github.wall2huang.zookeeper.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * author : Administrator
 **/
public class RpcProxy
{

    private String serverAddress;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    public <T> Object create(Class<T> cls)
    {
        return Proxy.newProxyInstance(cls.getClassLoader(),
                cls.getInterfaces(), new InvocationHandler()
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

                        if (serviceDiscovery != null)
                        {
                            String serverAddress = serviceDiscovery.discover();
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
                            return response;
                        }
                    }
                });
    }
}
