package com.yesongdh.common;

import com.alibaba.fastjson.JSONObject;

public class BaseResponse {
	
	private static final String CODE = "code";
	
	private static final String MSG = "msg";
	
	private static final String RESULT = "result";
	
	protected JSONObject success() {
		JSONObject resJson = new JSONObject();
		resJson.put(CODE, 0);
		resJson.put(MSG, "ok");
		return resJson;
	}
	
	protected JSONObject success(Object response) {
		JSONObject resJson = new JSONObject();
		resJson.put(CODE, 0);
		resJson.put(MSG, "ok");
		resJson.put(RESULT, response);
		return resJson;
	}
	
	protected JSONObject fail(Integer code, String message) {
		JSONObject resJson = new JSONObject();
		resJson.put(CODE, code);
		resJson.put(MSG, message);
		return resJson;
	}
	
	protected JSONObject fail(String message) {
		JSONObject resJson = new JSONObject();
		resJson.put(CODE, 4000);
		resJson.put(MSG, message);
		return resJson;
	}
}
