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
}
