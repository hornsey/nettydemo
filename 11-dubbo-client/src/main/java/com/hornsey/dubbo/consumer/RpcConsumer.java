package com.hornsey.dubbo.consumer;

import com.hornsey.dubbo.api.SomeService;
import com.hornsey.dubbo.client.RpcProxy;

/**
 * @Author hornsey
 * @create 2019/8/23 17:45
 */
public class RpcConsumer {

	public static void main(String[] args) {
		SomeService someService = RpcProxy.create(SomeService.class);
		String result = someService.doSome("Beijing");
		System.out.println(result);
	}
}
