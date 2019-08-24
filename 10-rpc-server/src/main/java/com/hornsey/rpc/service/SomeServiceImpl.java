package com.hornsey.rpc.service;

import com.hornsey.rpc.api.SomeService;

/**
 * @Author hornsey
 * @create 2019/8/22 10:42
 */
public class SomeServiceImpl implements SomeService {

	@Override
	public String doSome(String depart) {
		return "Welcome to " + depart;
	}
}
