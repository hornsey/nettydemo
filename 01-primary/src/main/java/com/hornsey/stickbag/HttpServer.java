package com.hornsey.stickbag;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author hornsey
 * @create 2019/8/16 15:52
 */
public class HttpServer {

	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();

		bootstrap.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.childHandler(new HttpChannelInitializer());
		try {
			ChannelFuture future = bootstrap.bind(8888).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
