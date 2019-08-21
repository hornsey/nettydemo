package com.hornsey.stickbag.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Author hornsey
 * @create 2019/8/16 15:52
 */
public class SomeServer {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup parent = new NioEventLoopGroup();
		EventLoopGroup child = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(parent, child)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
						pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
						pipeline.addLast(new SomeServerHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.bind(8088).sync();
			System.out.println("Server start...");
			future.channel().closeFuture().sync();
		} finally {
			parent.shutdownGracefully();
			child.shutdownGracefully();
		}
	}


















//	public static void main(String[] args) {
//		EventLoopGroup boss = new NioEventLoopGroup();
//		EventLoopGroup worker = new NioEventLoopGroup();
//
//		ServerBootstrap bootstrap = new ServerBootstrap();
//
//		bootstrap.group(boss, worker)
//				.option(ChannelOption.SO_BACKLOG, 1024)
//				.childOption(ChannelOption.SO_KEEPALIVE, true)
//				.channel(NioServerSocketChannel.class)
//				.childHandler(new ChannelInitializer<SocketChannel>() {
//					@Override
//					protected void initChannel(SocketChannel socketChannel) throws Exception {
//						ChannelPipeline pipeline = socketChannel.pipeline();
//						pipeline.addLast(new HttpRequestDecoder());
//						pipeline.addLast(new HttpResponseEncoder());
//						pipeline.addLast(new SomeServerHandler());
//					}
//				});
//		try {
//			ChannelFuture future = bootstrap.bind(8888).sync();
//			future.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			boss.shutdownGracefully();
//			worker.shutdownGracefully();
//		}
//	}
}
