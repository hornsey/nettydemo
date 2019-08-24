package com.hornsey.dubbo.registry;

import com.hornsey.dubbo.api.ApiConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

/**
 * @Author hornsey
 * @create 2019/8/24 14:07
 */
public class ZKRegistryCenter implements RegistryCenter {

	// zkclient
	private CuratorFramework curator;

	public ZKRegistryCenter() {
		// 创建客户端
		curator = CuratorFrameworkFactory.builder()
				.connectString(ApiConstant.ZK_CLUSTER)
				.sessionTimeoutMs(3000)
				.retryPolicy(new RetryNTimes(3, 2000))
				.build();
		// 启动客户端
		curator.start();
	}

	@Override
	public void register(String serviceName, String serviceAddress) {
		String servicePath = ApiConstant.ZK_DUBBO_PATH + "/" + serviceName;

		try {
			if (curator.checkExists().forPath(servicePath) == null) {
				curator.create()
						// 父节点不存在先创建
						.creatingParentContainersIfNeeded()
						.withMode(CreateMode.PERSISTENT)
						.forPath(servicePath, "0".getBytes());
			}

			String addressPath = servicePath + "/" + serviceAddress;
			String hostNode = curator.create()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(addressPath);
			System.out.println("hostNode = " + hostNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//测试
	public static void main(String[] args) {
		RegistryCenter registryCenter = new ZKRegistryCenter();
		registryCenter.register("com.hornsey.dubbo.api.TestService", "127.0.0.1:8888");
	}
}
