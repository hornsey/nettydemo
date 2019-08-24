package com.hornsey.dubbo.service;


import com.hornsey.dubbo.api.SomeService;

/**
 * @Author hornsey
 * @create 2019/8/22 10:42
 */
public class SomeServiceImpl implements SomeService {

	@Override
	public String doSome(String city) {
		return "Welcome to " + city;
	}
}
