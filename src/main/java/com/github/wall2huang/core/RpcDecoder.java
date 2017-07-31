package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/23.
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * author : Administrator
 **/
public class RpcDecoder extends ByteToMessageDecoder
{

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception
    {

        //请求的前四个字节标志了这个请求的数据长度
        if (byteBuf.readableBytes() < 4)
        {
            return ;
        }

        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0)
        {
            channelHandlerContext.close();
        }

        if (byteBuf.readableBytes() < dataLength)
        {
            byteBuf.resetReaderIndex();
            return ;
        }

        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        //序列化为对象，添加到out对象列表中去
        Object deserialize = SerializationUtil.deserialize(data, genericClass);

        System.out.println("这里是decoder,类型是：" + genericClass + ",直接反序列化的结果是：" + deserialize);

        list.add(deserialize);

    }
}
