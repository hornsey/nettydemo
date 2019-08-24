package com.hornsey.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hornsey
 * @create 2019/8/22 10:44
 */
public class RpcServer {

	// 服务器注册表
	private Map<String, Object> registryMap = new HashMap<>();
	// 指定包中class集合
	private List<String> classCache = new ArrayList<>();

	public void publish(String providerPackage) throws Exception {

		getProviderClass(providerPackage);

		doRegister();

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
									ClassResolvers.cacheDisabled(null)));
							pipeline.addLast(new RpcServerHandler(registryMap));
						}
					});

			ChannelFuture future = bootstrap.bind(8088).sync();
			System.out.println("MicService register success, port 8088!");
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}

	}

	private void doRegister() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (classCache.size() == 0) {
			return;
		}

		for (String className : classCache) {
			Class<?> clazz = Class.forName(className);
			String interfaceName = clazz.getInterfaces()[0].getName();
			registryMap.put(interfaceName, clazz.newInstance());
		}
		System.out.println("registryMap = " + registryMap);
	}

	public void getProviderClass(String providerPackage) {
		URL resource = this.getClass().getClassLoader()
				               .getResource(providerPackage.replace(".", "/"));
		File dir = new File(resource.getFile());
		for (File file : dir.listFiles()) {

			if (file.isDirectory()) {
				getProviderClass(providerPackage + "." + file.getName());
			} else if (file.getName().endsWith(".class")){
				String fileName = file.getName().replace(".class", "");
				classCache.add(providerPackage + "." + fileName);
			}
		}
		System.out.println(classCache);
	}


//	public static void main(String[] args) {
//		new RpcServer().getProviderClass("com.hornsey.rpc.service");
//	}




}
