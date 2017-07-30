package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/24.
 */

import com.github.wall2huang.transport.RpcRequest;
import com.github.wall2huang.transport.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * author : Administrator
 **/
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>
{
    private String host;
    private int port;
    private RpcResponse rpcResponse;
    private Object o = new Object();

    public RpcClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    // TODO: 2017/7/28 可能是这里卡住了
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception
    {
        this.rpcResponse = rpcResponse;
        synchronized (o)
        {
            o.notifyAll();
        }
    }

    public RpcResponse send(RpcRequest request) throws InterruptedException
    {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception
                        {
                            channel.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                            channel.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                            channel.pipeline().addLast(RpcClient.this);//client实现了SimpleChannelInboundHandler用来处理接受到的请求
                        }

                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            /** 写入这个对象 **/
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (o)
            {
                o.wait();//等待，直到messReceived方法接收到返回值
            }

            if (this.rpcResponse != null)
            {
                future.channel().close().sync();
            }
            return rpcResponse;
        }
        finally
        {
            nioEventLoopGroup.shutdownGracefully();
        }


    }


}
