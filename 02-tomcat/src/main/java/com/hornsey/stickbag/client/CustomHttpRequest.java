package com.hornsey.stickbag.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @Author hornsey
 * @create 2019/8/16 20:27
 */
public class CustomHttpRequest {
	private HttpRequest request;
	private ChannelHandlerContext context;

	public CustomHttpRequest(HttpRequest request, ChannelHandlerContext context) {
		this.request = request;
		this.context = context;
	}

	public String getUri() {
		return request.uri();
	}

	public String getMethod() {
		return request.method().name();
	}

	// 获取所有请求参数
	public Map<String, List<String>> getParameters() {
		QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
		return decoder.parameters();
	}

	// 获取指定参数
	public List<String> getParameters(String name) {
		Map<String, List<String>> parameters = this.getParameters();
		List<String> parameter = parameters.get(name);
		return parameter;
	}

	// 获取请求参数的第一个值
	public String getParameter(String name) {
		List<String> list = this.getParameters(name);
		if (list == null) {
			return null;
		}
		return list.get(0);
	}

	// 获取请求路径
	public String getPath() {
		QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
		return decoder.path();
	}
}
