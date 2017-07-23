package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/19.
 */

import com.github.wall2huang.annotation.RpcService;
import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * author : Administrator
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean
{
    private Map<String, Object> serviceMap = new HashMap<String, Object>();

    @Override
    public void afterPropertiesSet() throws Exception
    {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try
        {
            serverBootstrap.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(40001)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            // 添加编码器和解码器
                            socketChannel.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                            socketChannel.pipeline().addLast(new RpcEncoder(RpcRequest.class));

                            //添加真正业务处理handler


                        }
                    });
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            nioEventLoopGroup.shutdownGracefully().sync();
        }


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beansWithAnnotation.isEmpty())
        {
            for (Object bean : beansWithAnnotation.values())
            {
                String serviceName = bean.getClass().getAnnotation(RpcService.class).value();
                serviceMap.put(serviceName, bean);
            }
        }
    }

}
