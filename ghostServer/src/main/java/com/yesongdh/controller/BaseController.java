package com.yesongdh.controller;

import com.alibaba.fastjson.JSONObject;

public class BaseController {
	JSONObject resJson = new JSONObject();
	
	private static final String CODE = "code";
	
	private static final String MSG = "msg";
	
	private static final String RESULT = "result";
	
	protected JSONObject success(Object response) {
		resJson.put(CODE, 0);
		resJson.put(MSG, "");
		resJson.put(RESULT, response);
		return resJson;
	}
	
	protected JSONObject fail(Integer code, String message) {
		resJson.put(CODE, code);
		resJson.put(MSG, message);
		return resJson;
	}
}
