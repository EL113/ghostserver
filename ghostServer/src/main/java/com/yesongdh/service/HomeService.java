package com.yesongdh.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yesongdh.bean.Content;
import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.mapper.HomeMapper;

@Service
public class HomeService {
	
	@Autowired
	HomeMapper homeMapper;

	public JSONObject getRecommend(int startIndex) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		int pageCount = 15;
		
		List<RecommendId> ids = homeMapper.getRecommendIds(pageCount, startIndex);
		List<RecommendItem> items = new LinkedList<>();
		
		for(RecommendId id: ids) {
			RecommendItem item = homeMapper.getRecommendItem(id);
			if (item != null) {
				item.setType(id.getType());
				items.add(item);
			}
		}
		
		resJson.put("result", items);
		return resJson;
	}

	public JSONObject getContent(String id, String type) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		List<String> content = homeMapper.getContent(id, type);
		Map<String, Integer> stat = homeMapper.getStat(id, type);
		
		resJson.put("content", content);
		resJson.put("stat", stat);
		System.out.println(content + "," + stat);
		return resJson;
	}

	public JSONObject thumbUp(String id, String type, int status) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		int row = status == 0 ? homeMapper.thumbUp(id, type) : homeMapper.thumbUpCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}
	
	public JSONObject thumbDown(String id, String type, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		int row = operation == 0 ? homeMapper.thumbDown(id, type) : homeMapper.thumbDownCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}
	
	public JSONObject collect(String id, String type, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		int row = operation == 0 ? homeMapper.collect(id, type) : homeMapper.collectCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}

	public JSONObject publish(String id, String title, String author, String content, String type) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 1);
		
		String desc = content.substring(0,50);
		String buffer = "";
		id = id == null ? String.valueOf(System.currentTimeMillis() % 100000000) : id;
		homeMapper.deleteAudit(id);
		int subId = 0;
		while(content.length() > 500) {
			buffer = content.substring(0, 500);
			int row = homeMapper.publish(id, subId, title, author, type, buffer, desc);
			if (row != 1) {
				homeMapper.resetPublish(id);
				return resJson;
			}
			content = content.substring(500);
			buffer = "";
			subId++;
		}
		
		if (content.length() > 0) {
			homeMapper.publish(id, subId, title, author, type, content, desc);
		}
		
		resJson.put("code", 0);
		return resJson;
	}

	@Transactional
	public JSONObject audit(String id, String reason, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 1);
		
		List<Content> auditItems = homeMapper.getAuditItem(id);
		Content content = auditItems.get(0);
		if (operation == 0) {
			homeMapper.deleteItem(content);
			homeMapper.deleteContent(content);
			homeMapper.insertItem(content);
			homeMapper.insertStat(id, content.getType());
			for(int i=0; i < auditItems.size(); i++) {
				Content item = auditItems.get(i);
				homeMapper.insertContent(item);
			}
		} else {
			homeMapper.auditReason(id, reason);
		}
		
		resJson.put("code", 0);
		return resJson;
	}
}
