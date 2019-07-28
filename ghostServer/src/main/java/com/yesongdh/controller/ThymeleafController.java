package com.yesongdh.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.yesongdh.service.HomeService;

@Controller
public class ThymeleafController {
	@Autowired
	HomeService homeServce;

//    @RequestMapping(value = "/home.html")
//    public String greeting(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
//                           Model model) {
//    	JSONObject resJson = homeServce.getRecommend((page - 1) * 15);
//    	//TODO 这里可以设置为配置文件进行配置，或者从数据库获取
//    	List<Map<String, String>> categoryList = setupDefaultCategories();
//    	
//        model.addAttribute("recommend", resJson);
//        model.addAttribute("category", categoryList);
//        model.addAttribute("currentPage", page);
//        return "blog-home";
//    }
    
    private List<Map<String, String>> setupDefaultCategories() {
    	String[] titles = {"长篇","短篇","校园","医院","家里","民间","灵异","原创","内涵"};
    	String[] codes = {"cp","dp","cy","yy","jl","mj","ly","yc","nh"};
    	List<Map<String, String>> categoryList = new LinkedList<>();
    	Map<String, String> item;
    	for(int i=0; i<titles.length; i++) {
    		item = new HashMap<>();
    		item.put("title", titles[i]);
    		item.put("code", codes[i]);
    		categoryList.add(item);
    	}
		return categoryList;
	}

	@RequestMapping(value = "/content")
    public String contentWeb(
    		@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "page") int page,
                           Model model) {
    	JSONObject resJson = homeServce.getContent(id, type, page);
        model.addAttribute("recommend", resJson);
        return "content";
    }
    
    @RequestMapping(value = "/edit.html")
    public String edit() {
    	System.out.println("edit.html");
        return "blog-edit";
    }
    
    @RequestMapping(value = "/list/type.html")
    public String listType() {
        return "blog-edit";
    }
}
