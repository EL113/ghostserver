package com.yesongdh.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.service.HomeService;

@RestController
public class HomeController {
	
	@Resource
	HomeService homeService;
	
	@PostMapping("/recommend")
	@ResponseBody
	public JSONObject getRecommend(
			@RequestParam(required = true, name = "startIndex") int startIndex) {
		return homeService.getRecommend(startIndex);
	}

}
