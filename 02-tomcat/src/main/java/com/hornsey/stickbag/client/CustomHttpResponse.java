package com.hornsey.stickbag.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

/**
 * @Author hornsey
 * @create 2019/8/16 20:36
 */
public class CustomHttpResponse {
	private HttpRequest request;
	private ChannelHandlerContext context;

	public CustomHttpResponse(HttpRequest request, ChannelHandlerContext context) {
		this.request = request;
		this.context = context;
	}

	public void write(String content) throws Exception {
		if (StringUtil.isNullOrEmpty(content)) {
			return;
		}

		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(content.getBytes()));
		HttpHeaders headers = response.headers();
		headers.set(HttpHeaderNames.CONTENT_TYPE, "text/json");
		headers.set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		headers.set(HttpHeaderNames.EXPIRES, 0);

		if (HttpUtil.isKeepAlive(request)) {
			headers.set(HttpHeaderNames.CONNECTION,
					HttpHeaderValues.KEEP_ALIVE);
		}
		context.writeAndFlush(response);
	}
}
