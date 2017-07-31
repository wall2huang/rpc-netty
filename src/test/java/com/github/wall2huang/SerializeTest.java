package com.github.wall2huang;/**
 * Created by Administrator on 2017/7/30.
 */

import com.github.wall2huang.core.SerializationUtil;
import com.github.wall2huang.configuration.service.IHelloService;
import com.github.wall2huang.transport.RpcRequest;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * author : Administrator
 **/
public class SerializeTest
{

    public static void main(String[] args) throws NoSuchMethodException
    {
        Method method = IHelloService.class.getMethod("hello", String.class);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterType(method.getParameterTypes());
        rpcRequest.setParameters(new Object[] {"黄展东"});

        byte[] serialize = SerializationUtil.serialize(rpcRequest);
        RpcRequest deserialize = SerializationUtil.deserialize(serialize, rpcRequest.getClass());
        System.out.println(deserialize.toString());
    }

}
