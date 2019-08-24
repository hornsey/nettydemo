package com.hornsey.rpc.server;

import com.hornsey.rpc.api.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * @Author hornsey
 * @create 2019/8/22 11:15
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

	private Map<String, Object> registryMap;

	public RpcServerHandler(Map<String, Object> registryMap) {
		this.registryMap = registryMap;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RpcServerHandler.channelActive from " + ctx.channel().remoteAddress());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive from Client [" + ctx.channel().remoteAddress() + "], msg = [" + msg + "]");
		if (msg instanceof InvokeMessage) {
			InvokeMessage message = (InvokeMessage) msg;
			Object result = "Provider has no method.";
			if (registryMap.containsKey(message.getServiceName())) {
				Object provider = registryMap.get(message.getServiceName());
				result = provider.getClass()
						.getMethod(message.getMethodName(),message.getParamTypes())
						.invoke(provider, message.getParamValues());
			}
			ctx.writeAndFlush(result);
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
