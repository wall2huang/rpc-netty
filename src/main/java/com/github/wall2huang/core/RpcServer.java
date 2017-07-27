package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/19.
 */

import com.github.wall2huang.annotation.RpcService;
import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import com.github.wall2huang.zookeeper.ServiceRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * author : Administrator
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean
{
    private ServiceRegister serviceRegister;

    private String serviceAddress;

    private Map<String, Object> serviceMap = new HashMap<String, Object>();

    public RpcServer(String serviceAddress, ServiceRegister serviceRegister)
    {
        this.serviceAddress = serviceAddress;
        this.serviceRegister = serviceRegister;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try
        {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            // 添加编码器和解码器
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new ServerHandler(serviceMap)); //添加真正业务处理handler

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] split = serviceAddress.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);

            ChannelFuture future = serverBootstrap.bind(host, port).sync();

            if (serviceRegister != null)
            {
                serviceRegister.register(serviceAddress);//写入的是服务地址
            }

            future.channel().closeFuture().sync();
        } finally
        {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
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
