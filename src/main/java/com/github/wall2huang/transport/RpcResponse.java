package com.github.wall2huang.transport;/**
 * Created by Administrator on 2017/7/20.
 */

/**
 * author : Administrator
 * 定义了传输返回的协议格式
 **/
public class RpcResponse
{

    private String requestId;

    private Throwable exception;

    private Object result;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void setException(Throwable exception)
    {
        this.exception = exception;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}
