package com.yesongdh.common;

public enum Category {
	
	xy("xy", "校园"), dp("dp", "短篇"), cp("cp", "长篇"), 
	jl("jl", "家里"), ly("ly", "灵异"), mj("mj", "民间"), 
	yc("yc", "原创"), yy("yy", "医院");
	
	private final String code;
	private final String name;
	
	
	private Category(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public static String getCode(String code) {
		for(Category cate: Category.values()) {
			if (cate.getCode().equals(code)) {
				return cate.getName();
			}
		}
		return null;
	}
}
