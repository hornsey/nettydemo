package com.hornsey.dubbo.discovery;

import com.hornsey.dubbo.api.ApiConstant;
import com.hornsey.dubbo.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hornsey
 * @create 2019/8/24 16:04
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {

	List<String> servers = new ArrayList<>();

	private CuratorFramework curator;

	public ServiceDiscoveryImpl() {
		this.curator = CuratorFrameworkFactory.builder()
				.connectString(ApiConstant.ZK_CLUSTER)
				.sessionTimeoutMs(3000)
				.retryPolicy(new RetryNTimes(3, 2000))
				.build();
		curator.start();
	}

	@Override
	public String discover(String serviceName) {

		String servicePath = ApiConstant.ZK_DUBBO_PATH + "/" + serviceName;
		try {
			servers = curator.getChildren().forPath(servicePath);
			if (servers.size() == 0) {
				return null;
			}
			registerWatch(servicePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RandomLoadBalance().choose(servers);
	}

	// 向指定路径子节点添加watcher
	private void registerWatch(String servicePath) throws Exception {
		PathChildrenCache cache = new PathChildrenCache(curator, servicePath, true);
		cache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) ->
				                                  servers = curatorFramework.getChildren().forPath(servicePath));
		cache.start();
	}
}
