package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/20.
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
 **/
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest>
{

    private final Map<String, Object> handlerMap;

    public ServerHandler(Map<String, Object> handlerMap)
    {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RpcRequest request) throws Exception
    {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try
        {
            Object result = handler(request);
            response.setResult(result);
        } catch (Throwable e)
        {
            response.setException(e);
        }
        ctx.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);

    }

    private Object handler(RpcRequest request) throws Exception
    {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> cls = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterType = request.getParameterType();
        Object[] parameters = request.getParameters();

        Method method = cls.getMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
