package com.hornsey.stickbag.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author hornsey
 * @create 2019/8/16 21:17
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

	private static int count = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "发来的第 " + ++count + " 个包： " + (String)msg);
//		TimeUnit.MILLISECONDS.sleep(500);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
