package com.hornsey.rpc.server;

/**
 * @Author hornsey
 * @create 2019/8/22 11:49
 */
public class RpcServerStarter {
	public static void main(String[] args) throws Exception {

		RpcServer rpcServer = new RpcServer();
		rpcServer.publish("com.hornsey.rpc.service");

	}
}
