package com.github.wall2huang.transport;/**
 * Created by Administrator on 2017/7/20.
 */

/**
 * author : Administrator
 * 定义了传输协议，包括反射需要用到的大部分参数，及请求ID
 **/
public class RpcRequest
{

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] parameterType[];

    private Object[] parameters;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public Class<?>[][] getParameterType()
    {
        return parameterType;
    }

    public void setParameterType(Class<?>[][] parameterType)
    {
        this.parameterType = parameterType;
    }

    public Object[] getParameters()
    {
        return parameters;
    }

    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }
}
