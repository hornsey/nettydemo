package com.hornsey.dubbo.server;

import com.hornsey.dubbo.registry.ZKRegistryCenter;

/**
 * @Author hornsey
 * @create 2019/8/22 11:49
 */
public class RpcServerStarter {
	public static void main(String[] args) throws Exception {

		String serviceAddress = "127.0.0.1:8088";
		String packageName = "com.hornsey.dubbo.service";
		RpcServer rpcServer = new RpcServer();
		rpcServer.publish(new ZKRegistryCenter(), serviceAddress, packageName);

	}
}
