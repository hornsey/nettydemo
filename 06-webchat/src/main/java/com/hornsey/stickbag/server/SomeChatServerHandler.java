package com.hornsey.stickbag.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

/**
 * @Author hornsey
 * @create 2019/8/21 12:07
 */
public class SomeChatServerHandler extends ChannelInboundHandlerAdapter {

	private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		SocketAddress address = channel.remoteAddress();
		System.out.println(address + "---上线");

		group.writeAndFlush(address + "---上线");
		group.add(channel);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		SocketAddress address = channel.remoteAddress();
		System.out.println(address + "---下线");

		group.writeAndFlush(address + "---上线， 目前在线人数：" + group.size() + "\n");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Channel channel = ctx.channel();
		group.forEach(ch -> {
			if (channel == ch) {
				ch.writeAndFlush("Me: " + msg );
			} else {
				ch.writeAndFlush(channel.remoteAddress() + ":" + msg);
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
