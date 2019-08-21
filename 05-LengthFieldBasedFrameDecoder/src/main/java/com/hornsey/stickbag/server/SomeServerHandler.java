package com.hornsey.stickbag.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author hornsey
 * @create 2019/8/16 21:17
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + ", " + msg);
		ctx.channel().writeAndFlush("from server : " + UUID.randomUUID());
		TimeUnit.MILLISECONDS.sleep(500);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		cause.printStackTrace();
		ctx.close();
	}
}
