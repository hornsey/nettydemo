package com.hornsey.dubbo.registry;

/**
 * @Author hornsey
 * @create 2019/8/24 14:06
 */
public interface RegistryCenter {

	/**
	 * 将服务注册到zk
	 * @param serviceName
	 * @param serviceAddress
	 */
	void register(String serviceName, String serviceAddress);
}
