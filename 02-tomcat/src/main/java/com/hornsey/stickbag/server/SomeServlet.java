package com.hornsey.stickbag.server;

import com.hornsey.stickbag.client.CustomHttpRequest;
import com.hornsey.stickbag.client.CustomHttpResponse;
import com.hornsey.stickbag.client.CustomHttpServlet;

/**
 * @Author hornsey
 * @create 2019/8/16 21:21
 */
public class SomeServlet extends CustomHttpServlet {
	@Override
	public void doGet(CustomHttpRequest request, CustomHttpResponse response) throws Exception {
		String param = request.getParameter("name");
		String uri = request.getUri();
		String method = request.getMethod();
		String path = request.getPath();
		String content = "method = " + method + "\n" +
				                 "uri = " + uri + "\n" +
				                 "path = " + path + "\n" +
				                 "param = " + param;
		response.write(content);
	}

	@Override
	public void doPost(CustomHttpRequest request, CustomHttpResponse response) throws Exception {
		doGet(request, response);
	}
}
