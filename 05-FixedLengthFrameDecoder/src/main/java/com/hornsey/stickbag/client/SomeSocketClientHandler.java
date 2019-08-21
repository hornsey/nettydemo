package com.hornsey.stickbag.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author hornsey
 * @create 2019/8/19 15:12
 */
public class SomeSocketClientHandler extends ChannelInboundHandlerAdapter {



	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 100; i++) {
			ctx.channel().writeAndFlush("hello world");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
