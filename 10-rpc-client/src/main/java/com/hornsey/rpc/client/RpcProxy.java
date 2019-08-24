package com.hornsey.rpc.client;


import com.hornsey.rpc.api.InvokeMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author hornsey
 * @create 2019/8/23 17:24
 */
public class RpcProxy {

	public static <T> T create(Class<?> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[]{clazz},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (Object.class.equals(method.getDeclaringClass())) {
							return method.invoke(this, args);
						}
						return rpcInvoke(clazz, method, args);
					}

				});
	}

	private static Object rpcInvoke(Class<?> clazz, Method method, Object[] args) {
		RpcClientHandler handler = new RpcClientHandler();

		NioEventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast(new ObjectEncoder());
						pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
								ClassResolvers.cacheDisabled(null)));
						pipeline.addLast(handler);
					}
				});

		try {
			ChannelFuture future = bootstrap.connect("localhost", 8088).sync();
			InvokeMessage message = new InvokeMessage();
			message.setServiceName(clazz.getName());
			message.setMethodName(method.getName());
			message.setParamTypes(method.getParameterTypes());
			message.setParamValues(args);

			future.channel().writeAndFlush(message);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

		return handler.getResult();
	}








}
