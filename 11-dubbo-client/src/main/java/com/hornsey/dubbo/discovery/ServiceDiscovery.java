package com.hornsey.dubbo.discovery;

/**
 * @Author hornsey
 * @create 2019/8/24 16:03
 */
public interface ServiceDiscovery {

	/**
	 * 根据服务名称返回提供者IP+port
	 * @param serviceName
	 * @return
	 */
	String discover(String serviceName);
}
