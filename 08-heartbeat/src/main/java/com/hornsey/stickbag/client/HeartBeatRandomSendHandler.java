package com.hornsey.stickbag.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author hornsey
 * @create 2019/8/19 15:12
 */
public class HeartBeatRandomSendHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("HeartBeat<====>channelActive@" + Thread.currentThread().getName());
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
