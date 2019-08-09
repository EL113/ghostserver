package com.yesongdh.exception;

public class BadRequestException extends RuntimeException {
	
	private int code = 4000;
	private String msg = "";
	
	public BadRequestException(int code, String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public BadRequestException(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
