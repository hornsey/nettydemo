package com.hornsey.dubbo.server;

import com.hornsey.dubbo.registry.RegistryCenter;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author hornsey
 * @create 2019/8/22 10:44
 */
public class RpcServer {

	// 服务器注册表
	private ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();
	// 指定包中class集合
	private List<String> classCache = Collections.synchronizedList(new ArrayList<>());

	public void publish(RegistryCenter registryCenter, String serviceAddress,
	                    String providerPackage) throws Exception {

		// 扫描指定包下的所有实现类：要将指定包下的class添加到一个集合
		getProviderClass(providerPackage);

		// 将服务注册到zk
		doRegister(registryCenter, serviceAddress);

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

			String ip = serviceAddress.split(":")[0];
			String port = serviceAddress.split(":")[1];

			ChannelFuture future = bootstrap.bind(ip, Integer.valueOf(port)).sync();
			System.out.println("MicService register success, port " + port);
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	//将接口注册到zk
	private void doRegister(RegistryCenter registryCenter, String serviceAddress) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (classCache.size() == 0) {
			return;
		}

		boolean isRegisted = false;
		for (String className : classCache) {
			Class<?> clazz = Class.forName(className);
			String interfaceName = clazz.getInterfaces()[0].getName();
			registryMap.put(interfaceName, clazz.newInstance());
			if (!isRegisted) {
				registryCenter.register(interfaceName, serviceAddress);
				isRegisted = true;
			}
		}
		System.out.println("registryMap = " + registryMap);
	}

	// 扫描指定包下的所有实现类：要将指定包下的class添加到一个集合
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

}
