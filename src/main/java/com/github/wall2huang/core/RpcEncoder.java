package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/23.
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * author : Administrator
 * 编码器
 **/
public class RpcEncoder extends MessageToByteEncoder
{
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception
    {
        System.out.println("这里是encoder,类型是：" + genericClass + ",对象是：" + o);
        if (genericClass.isInstance(o))
        {
            byte[] data = SerializationUtil.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
