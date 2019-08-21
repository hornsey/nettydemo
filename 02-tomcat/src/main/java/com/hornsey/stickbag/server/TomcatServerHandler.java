package com.hornsey.stickbag.server;

import com.hornsey.stickbag.client.CustomHttpRequest;
import com.hornsey.stickbag.client.CustomHttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author hornsey
 * @create 2019/8/16 21:17
 */
public class TomcatServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			CustomHttpRequest req = new CustomHttpRequest(request, ctx);
			CustomHttpResponse resp = new CustomHttpResponse(request, ctx);

			SomeServlet servlet = new SomeServlet();
			servlet.doGet(req, resp);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
