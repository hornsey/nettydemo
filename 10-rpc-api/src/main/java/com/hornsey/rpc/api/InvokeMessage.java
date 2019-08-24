package com.hornsey.rpc.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author hornsey
 * @create 2019/8/22 10:34
 */
@Data
public class InvokeMessage implements Serializable {

	// 服务名称
	private String serviceName;

	// 方法名
	private String methodName;

	// 参数列表
	private Class<?>[] paramTypes;

	// 方法参数值
	private Object[] paramValues;
}
