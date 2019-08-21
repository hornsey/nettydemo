package com.hornsey.stickbag.client;

/**
 * @Author hornsey
 * @create 2019/8/16 21:10
 */
public abstract class CustomHttpServlet {
	public abstract void doGet(CustomHttpRequest request,
	                           CustomHttpResponse response) throws Exception;

	public abstract void doPost(CustomHttpRequest request,
	                           CustomHttpResponse response) throws Exception;
}
