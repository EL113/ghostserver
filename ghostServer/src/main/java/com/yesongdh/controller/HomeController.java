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
	
	@PostMapping("/content")
	@ResponseBody
	public JSONObject getContent(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type) {
		return homeService.getContent(id, type);
	}
	
	@PostMapping("/thumbUp")
	@ResponseBody
	public JSONObject thumbUp(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "operation") int operation) {
		return homeService.thumbUp(id, type, operation);
	}
	
	@PostMapping("/thumbDown")
	@ResponseBody
	public JSONObject thumbDown(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "operation") int operation) {
		return homeService.thumbDown(id, type, operation);
	}
	
	@PostMapping("/collect")
	@ResponseBody
	public JSONObject collect(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "operation") int operation) {
		return homeService.collect(id, type, operation);
	}
	
	@PostMapping("/publish")
	@ResponseBody
	public JSONObject publish(
			@RequestParam(required = false, name = "id") String id,
			@RequestParam(required = true, name = "title") String title,
			@RequestParam(required = true, name = "author") String author,
			@RequestParam(required = true, name = "content") String content,
			@RequestParam(required = true, name = "type") String type) {
		return homeService.publish(id, title, author, content, type);
	}
	
	@PostMapping("/audit")
	@ResponseBody
	public JSONObject audit(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "operation") int operation,
			@RequestParam(required = false, name = "reason") String reason) {
		return homeService.audit(id, reason, operation);
	}
	
}
