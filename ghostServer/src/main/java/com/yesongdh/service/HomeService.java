package com.yesongdh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yesongdh.bean.Content;
import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.bean.StoryContent;
import com.yesongdh.bean.StoryContentDot;
import com.yesongdh.bean.StoryList;
import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonPage;
import com.yesongdh.common.CommonPageInfo;
import com.yesongdh.mapper.HomeMapper;
import com.yesongdh.mapper.StoryContentMapper;
import com.yesongdh.mapper.StoryListMapper;
import com.yesongdh.mapper.StoryStatMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class HomeService {
	
	@Autowired
	HomeMapper homeMapper;
	
	@Autowired
	StoryListMapper storyListMapper;
	
	@Autowired
	StoryContentMapper storyContentMapper;
	
	@Autowired
	StoryStatMapper storyStatMapper;

	public PageInfo<StoryList> getRecommend(int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<StoryList> stats = homeMapper.getStoryListByStatOrder();
		
		return new PageInfo<>(stats);
	}

	public StoryContentDot getContent(String id,String type, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		Example example = new Example(StoryContent.class);
		example.selectProperties("content");
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", id);
		List<StoryContent> contentList = storyContentMapper.selectByExample(example);
		
		Example statExample = new Example(StoryStat.class);
		Criteria statCriteria = statExample.createCriteria();
		statCriteria.andEqualTo("id", id).andEqualTo("type", type);
		StoryStat storyStat = storyStatMapper.selectByExample(statExample).get(0);
		CommonPageInfo<StoryContent> contentInfo = new CommonPageInfo<StoryContent>(contentList);
		CommonPage page = contentInfo.getPage();
		
		StringBuffer contentBuffer = new StringBuffer();
		for(StoryContent content: contentList) {
			contentBuffer.append(content.getContent());
		}
		
		StoryContentDot storyContentDot = new StoryContentDot();
		storyContentDot.setStoryContent(contentBuffer.toString());
		storyContentDot.setStoryStat(storyStat);
		storyContentDot.setPage(page);
		return storyContentDot;
	}

	public JSONObject thumbUp(String id, String type, int status) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		Integer thumbUpCount = homeMapper.getThumbUpCount(id, type);
		if (thumbUpCount == 0 && status == 1) {
			return resJson;
		}
		int row = status == 0 ? homeMapper.thumbUp(id, type) : homeMapper.thumbUpCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}
	
	public JSONObject thumbDown(String id, String type, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		int thumbDownCount = homeMapper.getThumbDownCount(id, type);
		if (thumbDownCount == 0 && operation == 1) {
			return resJson;
		}
		int row = operation == 0 ? homeMapper.thumbDown(id, type) : homeMapper.thumbDownCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}
	
	public JSONObject collect(String id, String type, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		int collectCount = homeMapper.getCollectCount(id, type);
		if (collectCount == 0 && operation == 1) {
			return resJson;
		}
		int row = operation == 0 ? homeMapper.collect(id, type) : homeMapper.collectCancel(id, type);
		if (row == 0) {
			resJson.put("code", 1);
		}
		
		return resJson;
	}

	//审查表中插入数据
	public JSONObject publish(String id, String title, String author, String content, String type, String authorId) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 1);
		
		String desc = content.substring(0,50);
		String buffer = "";
		id = id == null ? String.valueOf(System.currentTimeMillis() % 100000000) : id;
		homeMapper.deleteAudit(id);
		int subId = 0;
		while(content.length() > 500) {
			buffer = content.substring(0, 500);
			int row = homeMapper.publish(id, subId, title, author, type, buffer, desc, authorId);
			if (row != 1) {
				homeMapper.resetPublish(id);
				return resJson;
			}
			content = content.substring(500);
			buffer = "";
			subId++;
		}
		
		if (content.length() > 0) {
			homeMapper.publish(id, subId, title, author, type, content, desc, authorId);
		}
		
		resJson.put("code", 0);
		resJson.put("storyId", id);
		return resJson;
	}

	@Transactional
	public JSONObject audit(String id, String reason, int operation) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 1);
		
		List<Content> auditItems = homeMapper.getAuditItem(id);
		Content content = auditItems.get(0);
		if (operation == 0) {
			homeMapper.deleteAudit(id);
			homeMapper.deleteItem(id, content.getType());
			homeMapper.deleteContent(content.getId(), content.getType());
			homeMapper.insertItem(content);
			homeMapper.insertStat(id, content.getType());
			for(int i=0; i < auditItems.size(); i++) {
				Content item = auditItems.get(i);
				homeMapper.insertContent(item);
			}
		} else {
			homeMapper.auditReason(id, reason, operation);
		}
		
		resJson.put("code", 0);
		return resJson;
	}

	public JSONObject auditResult(String ids) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		
		String[] idArray = ids.split(",");
		List<Integer> resultList = new LinkedList<>();
		for(String id: idArray) {
			List<Integer> result = homeMapper.getAuditResult(id);
			System.out.println("result:"+id+","+result);
			if (result == null || result.isEmpty()) {
				resultList.add(1);
			} else {
				resultList.add(result.get(0));
			}
			
		}
		
		resJson.put("resultList", resultList);
		return resJson;
	}

	public JSONObject searchKeyword(String keyword, int startIndex) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		String[] types = {"cp","dp","jl","ly","mj","xy","yc","yy"};
		int count = 15;
		
		List<RecommendItem> list = new LinkedList<>();
		for(String type: types) {
			if (list.size() > 15) {
				break;
			} 
			int countIndex = homeMapper.searchKeywordCount(type, keyword);
			if (startIndex > countIndex) {
				startIndex -= countIndex;
				continue;
			}
			
			List<RecommendItem> items = homeMapper.searchKeyword(type, keyword, startIndex, count);
			list.addAll(items);
			startIndex = 0;
		}
		
		return null;
	}

	public JSONObject typeList(String type, int startIndex) {
		JSONObject resJson = new JSONObject();
		resJson.put("code", 0);
		int count = 15;

		List<RecommendItem> items = homeMapper.getTypeList(type, count, startIndex);
		for(RecommendItem item : items)
			item.setType(type);
		resJson.put("result", items);
		return resJson;
	}

//	public JSONObject delete(String id, String type, String authorId) {
//		JSONObject resJson = new JSONObject();
//		resJson.put("code", 0);
//		
//		RecommendId recommendId = new RecommendId();
//		recommendId.setId(id);
//		recommendId.setId(type);
//		RecommendItem item = homeMapper.getRecommendItem(recommendId);
//		if (authorId.equals(item.getAuthorId())) {
//			homeMapper.deleteStatItem(id, type);
//			homeMapper.deleteItem(id, type);
//			homeMapper.deleteAudit(id);
//			homeMapper.deleteContent(id, type);
//		}
//		return resJson;
//	}
}
