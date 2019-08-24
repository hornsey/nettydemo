package com.hornsey.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author hornsey
 * @create 2019/8/23 17:30
 */
public class RpcClientHandler extends SimpleChannelInboundHandler {

	private Object result;

	public Object getResult() {
		return this.result;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
		System.out.println("Server: " + ctx.channel().remoteAddress());
		this.result = o;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
