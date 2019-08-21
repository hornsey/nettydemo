package com.hornsey.stickbag.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author hornsey
 * @create 2019/8/19 15:12
 */
public class SomeSocketClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + ", " + s);
		ctx.channel().writeAndFlush("from client : " + LocalDateTime.now() + " - " + getRandomString());
		TimeUnit.MILLISECONDS.sleep(500);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().writeAndFlush("from client: begin talking");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private String getRandomString() {
		String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		int length = random.nextInt(1024);
		System.out.println("length = " + length);
		char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			result[i] = src.charAt(random.nextInt(src.length()));
		}
		System.out.println(new String(result));
		return new String(result);
	}

	public static void main(String[] args) {
		new SomeSocketClientHandler().getRandomString();
	}
}
