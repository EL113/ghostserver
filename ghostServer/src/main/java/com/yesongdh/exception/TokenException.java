package com.yesongdh.exception;

public class TokenException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code = 4000;
	private String msg = "";
	
	public TokenException(int code, String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public TokenException(String msg) {
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
