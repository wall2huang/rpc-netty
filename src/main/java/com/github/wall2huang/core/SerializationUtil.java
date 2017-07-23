package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/23.
 */

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author : Administrator
 * 序列化工具，使用protocol协议的序列化工具
 **/
public class SerializationUtil
{

    private static Map<Class<?>, Schema<?>> cacheSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private static <T> Schema<T> getShema(Class<T> cls)
    {
        Schema<T> schema = (Schema<T>)cacheSchema.get(cls);
        if (schema == null)
        {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null)
            {
                cacheSchema.put(cls, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj)
    {
        Class<T> cls = (Class<T>)obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try
        {
            Schema<T> shema = getShema(cls);
            return ProtostuffIOUtil.toByteArray(obj, shema, buffer);
        } catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }finally
        {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls)
    {
        try
        {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getShema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }


}
