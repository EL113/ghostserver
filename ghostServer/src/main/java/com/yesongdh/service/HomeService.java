package com.yesongdh.service;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryContent;
import com.yesongdh.bean.StoryContentDot;
import com.yesongdh.bean.StoryList;
import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonPage;
import com.yesongdh.common.CommonPageInfo;
import com.yesongdh.mapper.HomeMapper;
import com.yesongdh.mapper.StoryAuditMapper;
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
	
	@Autowired
	StoryAuditMapper storyAuditMapper;

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
		
		Example statExample = getStatExampleByIdAndType(id, type);
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
	
	private Example getStatExampleByIdAndType(String id, String type) {
		Example example = new Example(StoryStat.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("type", type).andEqualTo("id", id);
		return example;
	}

	public boolean thumbUp(String id, String type, int op) {
		Example example = getStatExampleByIdAndType(id, type);
		StoryStat storyStat = storyStatMapper.selectByExample(example).get(0);
		int thumbUpCount = storyStat != null ? storyStat.getThumbUp() : 0;
		//点赞数不能为负
		if (thumbUpCount == 0 && op == 1) {
			return false;
		}
		
		storyStat.setThumbUp(op == 0 ? thumbUpCount+1 : thumbUpCount-1);
		int row = storyStatMapper.updateByPrimaryKeySelective(storyStat);
		if (row == 0) return false;
		
		return true;
	}
	
	public boolean thumbDown(String id, String type, int op) {
		Example example = getStatExampleByIdAndType(id, type);
		StoryStat storyStat = storyStatMapper.selectByExample(example).get(0);
		int thumbDownCount = storyStat != null ? storyStat.getThumbDown() : 0;
		if (thumbDownCount == 0 && op == 1)	return false;
		
		storyStat.setThumbDown(op == 0 ? thumbDownCount+1 : thumbDownCount-1);
		int row = storyStatMapper.updateByPrimaryKeySelective(storyStat);		
		if (row == 0) return false;
		
		return true;
	}
	
	public boolean collect(String id, String type, int op) {
		Example example = getStatExampleByIdAndType(id, type);
		StoryStat storyStat = storyStatMapper.selectByExample(example).get(0);
		
		int collectCount = storyStat != null ? storyStat.getCollection() : 0;
		if (collectCount == 0 && op == 1)	return false;
		
		storyStat.setCollection(op == 0 ? collectCount + 1 : collectCount - 1);
		int row = storyStatMapper.updateByPrimaryKey(storyStat);
		if (row == 0) return false;
		
		return true;
	}

	//审查表中插入数据
	public String publish(StoryAudit story) {
		String brief = story.getContent().substring(0,50);
		story.setBrief(brief);
		String id = story.getId() == null ? String.valueOf(System.currentTimeMillis() % 100000000) 
				: String.valueOf(story.getId());
		
		Example example = new Example(StoryAudit.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", id);
		storyAuditMapper.deleteByExample(example);
		
		int subId = 0;
		String bufferContent = story.getContent();
		String subContent = "";
		while(bufferContent.length() > 500) {
			subContent = bufferContent.substring(0, 500);
			story.setSubId(subId);
			story.setContent(subContent);
			int row = storyAuditMapper.insertSelective(story);
			if (row != 1) {
				storyAuditMapper.deleteByExample(example);
				return null;
			}
			bufferContent = bufferContent.substring(500);
			subContent = "";
			subId++;
		}
		
		if (bufferContent.length() > 0) {
			storyAuditMapper.insertSelective(story);
		}
		
		return id;
	}

	@Transactional
	public boolean audit(String id, String reason, int op) {
		Example auditExample = new Example(StoryAudit.class);
		Criteria criteria = auditExample.createCriteria();
		criteria.andEqualTo("id", id);
		List<StoryAudit> storyAudits = storyAuditMapper.selectByExample(auditExample);
		StoryAudit storyAudit = storyAudits.get(0);
		
		StoryList storyList = storyAuditToStoryList(storyAudit);
		StoryStat storyStat = storyAuditToStoryStat(storyAudit);
		StoryAudit storyAudit2 = new StoryAudit();
		// 审核通过 审核表状态 重置故事列表记录 故事统计记录 重置内容记录
		if (op == 0) {
			storyAudit2.setStatus("1");
			Example contentExample = new Example(StoryContent.class);
			Criteria criteria2 = contentExample.createCriteria();
			criteria2.andEqualTo("id", storyAudit.getId());
			
			storyAuditMapper.updateByExampleSelective(storyAudit2, auditExample);
			storyListMapper.deleteByPrimaryKey(storyAudit.getId());
			storyListMapper.insertSelective(storyList);
			storyStatMapper.insertSelective(storyStat);
			storyContentMapper.deleteByExample(contentExample);
			
			for(int i=0; i < storyAudits.size(); i++) {
				StoryContent storyContent = storyAuditToStoryContent(storyAudits.get(i));
				storyContentMapper.insertSelective(storyContent);
			}
		} else {
			// 审核不通过 修改原因
			storyAudit2.setReason(reason);
			storyAuditMapper.updateByExampleSelective(storyAudit2, auditExample);
		}
		
		return true;
	}
	
	private StoryList storyAuditToStoryList(StoryAudit storyAudit) {
		StoryList storyList = new StoryList();
		storyList.setAuthor(storyAudit.getAuthor());
		storyList.setAuthorId(storyAudit.getAuthorid());
		storyList.setBrief(storyAudit.getBrief());
		storyList.setCreateTime(storyAudit.getCreateTime());
		storyList.setId(storyAudit.getId());
		storyList.setTitle(storyAudit.getTitle());
		storyList.setType(storyAudit.getType());
		return storyList;
	}
	
	private StoryStat storyAuditToStoryStat(StoryAudit storyAudit) {
		StoryStat storyStat = new StoryStat();
		storyStat.setId(storyAudit.getId());
		storyStat.setType(storyAudit.getType());
		return storyStat;
	}
	
	private StoryContent storyAuditToStoryContent(StoryAudit storyAudit) {
		StoryContent storyContent = new StoryContent();
		storyContent.setContent(storyAudit.getContent());
		storyContent.setId(storyAudit.getId());
		storyContent.setSubId(storyAudit.getSubId());
		return storyContent;
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
