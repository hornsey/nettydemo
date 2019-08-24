package com.hornsey.dubbo.loadbalance;

import java.util.List;

/**
 * @Author hornsey
 * @create 2019/8/24 16:18
 */
public interface LoadBalance {
	String choose(List<String> services);
}
