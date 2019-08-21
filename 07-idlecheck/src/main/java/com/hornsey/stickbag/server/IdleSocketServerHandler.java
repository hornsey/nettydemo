package com.hornsey.stickbag.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Author hornsey
 * @create 2019/8/21 14:53
 */
public class IdleSocketServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String eventDes = null;
			switch (event.state()) {
				case READER_IDLE: eventDes = "读空闲超时"; break;
				case WRITER_IDLE: eventDes = "写空闲超时"; break;
				case ALL_IDLE: eventDes = "读写空闲超时"; break;
			}
			System.out.println(ctx.channel().remoteAddress() + ":" + eventDes);
			ctx.channel().close();
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
