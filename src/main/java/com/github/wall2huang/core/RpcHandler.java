package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/23.
 */

import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * author : Administrator
 * 真正的RPC业务处理handler，接受已经序列化完的RpcRequest对象
 * 利用反射实例化对象，执行方法返回结果
 **/
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest>
{

    private Map<String, Object> serviceMap;

    public RpcHandler(Map<String, Object> serviceMap)
    {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception
    {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try
        {
            Object result = handler(rpcRequest);
            rpcResponse.setResult(result);
        } catch (Throwable e)
        {
            rpcResponse.setException(e);
        }
        channelHandlerContext.writeAndFlush(rpcResponse)
        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }


    private Object handler(RpcRequest request) throws Exception
    {
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);

        Class<?> cls = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterType = request.getParameterType();
        Object[] parameters = request.getParameters();
        Method method = cls.getMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
