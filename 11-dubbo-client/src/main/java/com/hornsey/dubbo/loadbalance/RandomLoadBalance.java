package com.hornsey.dubbo.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @Author hornsey
 * @create 2019/8/24 16:18
 */
public class RandomLoadBalance implements LoadBalance {
	@Override
	public String choose(List<String> services) {
		int index = new Random().nextInt(services.size());
		return services.get(index);
	}
}
