package com.hornsey.stickbag;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

/**
 * @Author hornsey
 * @create 2019/8/16 15:59
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			System.out.println("请求方式： " + request.method().name());
			System.out.println("请求URL： " + request.uri());

			if ("/favicon.ico".equals(request.uri())) {
				System.out.println("不处理/favicon.ico请求");
				return;
			}

			ByteBuf content = Unpooled.copiedBuffer("Hello Netty", Charset.defaultCharset());
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

			HttpHeaders headers = response.headers();
			headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

			ctx.writeAndFlush(response)
					.addListener(ChannelFutureListener.CLOSE);

		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
