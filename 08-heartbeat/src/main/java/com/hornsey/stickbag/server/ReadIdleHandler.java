package com.hornsey.stickbag.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.LocalDateTime;

/**
 * @Author hornsey
 * @create 2019/8/21 15:53
 */
public class ReadIdleHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) evt).state();
			if (state == IdleState.READER_IDLE) {
				System.out.println(ctx.channel().remoteAddress() + " 连接超时，已断开连接");
				ctx.disconnect();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "@" + LocalDateTime.now()  + " 已连接");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
