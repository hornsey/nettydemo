package com.hornsey.stickbag.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author hornsey
 * @create 2019/8/21 16:26
 */
public class ReconnectSendHandler extends ChannelInboundHandlerAdapter {

	private Bootstrap bootstrap;
	private int retries;
	private int retriesRaw;
	private boolean disconnected = true;

	public ReconnectSendHandler(Bootstrap bootstrap, int retries) {
		this.bootstrap = bootstrap;
		this.retries = retries;
		this.retriesRaw = retries;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("====>Connected to Server");
		retries = retriesRaw;
		disconnected = false;
		System.out.println("<====>channelActive@" + Thread.currentThread().getName());
		randomSend(ctx.channel());
	}

	private void randomSend(Channel channel) {
		int heartBeatInterval = new Random().nextInt(6) + 1;
		System.out.println("==>Next heart beat will send after " + heartBeatInterval + " seconds.");

		// 异步任务发送心跳
		ScheduledFuture<?> future = channel.eventLoop().schedule(() -> {
			if (channel.isActive()) {
				System.out.println("==>Send heart beat to Server");
				channel.writeAndFlush("PING");
			} else {
				System.out.println("==>Disconnected from Server");
				channel.closeFuture();
				throw new RuntimeException();
			}
		}, heartBeatInterval, TimeUnit.SECONDS);

		future.addListener((fu) -> {
			if (fu.isSuccess()) {
				randomSend(channel);
			}
		});
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ScheduledFuture<?> future = ctx.channel().eventLoop().schedule(() -> {
			System.out.println("====>Reconnecting...");
			System.out.println("====>channelInactive@" + Thread.currentThread().getName());
			try {
				bootstrap.connect("localhost", 8088).sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, 1, TimeUnit.SECONDS);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
