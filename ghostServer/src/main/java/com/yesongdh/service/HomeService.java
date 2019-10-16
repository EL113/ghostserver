package com.yesongdh.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryContent;
import com.yesongdh.bean.StoryContentDot;
import com.yesongdh.bean.StoryList;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonPage;
import com.yesongdh.common.CommonPageInfo;
import com.yesongdh.common.ScoreStrategy;
import com.yesongdh.common.ScoreStrategyImp;
import com.yesongdh.mapper.HomeMapper;
import com.yesongdh.mapper.StoryAuditMapper;
import com.yesongdh.mapper.StoryContentMapper;
import com.yesongdh.mapper.StoryListMapper;
import com.yesongdh.mapper.StoryReportMapper;
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
	
	@Autowired
	StoryReportMapper storyReportMapper;

	public JSONObject getRecommend(int pageNo, int pageSize) {
		JSONObject result = new JSONObject();
		PageHelper.startPage(pageNo, pageSize);
		List<StoryList> stats = homeMapper.getStoryListByStatOrder();
		Integer statsCount = homeMapper.getStoryListByStatOrderCount();
		int maxPageNo = statsCount / pageSize;
		
		result.put("list", stats);
		result.put("maxPageNo", maxPageNo);
		return result;
	}

	public StoryContentDot getContent(String id,String type, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		Example example = new Example(StoryContent.class);
		example.selectProperties("content");
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", id);
		List<StoryContent> contentList = storyContentMapper.selectByExample(example);
		StoryContent record = new StoryContent();
		record.setId(Integer.valueOf(id));
		int count = storyContentMapper.selectCount(record);
		
		Example statExample = getStatExampleByIdAndType(id, type);
		StoryStat storyStat = storyStatMapper.selectOneByExample(statExample);
		//统计表没有记录则新建一条记录
		if (storyStat == null && !contentList.isEmpty()) {
			storyStat = new StoryStat();
			storyStat.setId(Integer.valueOf(id));
			storyStat.setType(type);
			storyStat.setDefault();
			storyStatMapper.insert(storyStat);
		}
		
		List<String> contents = new ArrayList<String>();
		for(StoryContent content: contentList) {
			contents.add(content.getContent());
		}
		
		StoryContentDot storyContentDot = new StoryContentDot();
		storyContentDot.setContent(contents);
		storyContentDot.setStoryStat(storyStat);
		storyContentDot.setCurrentPage(pageNo);
		storyContentDot.setMaxPage(count % pageSize == 0 ? count / pageSize : count/pageSize + 1);
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
		calScore(storyStat);
		int row = storyStatMapper.updateByPrimaryKeySelective(storyStat);
		if (row == 0) return false;
		
		return true;
	}
	
	private void calScore(StoryStat storyStat) {
		ScoreStrategy scoreStrategy = new ScoreStrategyImp();
		int score = scoreStrategy.calScore(storyStat);
		storyStat.setScore(score);
	}
	
	public boolean thumbDown(String id, String type, int op) {
		Example example = getStatExampleByIdAndType(id, type);
		StoryStat storyStat = storyStatMapper.selectByExample(example).get(0);
		int thumbDownCount = storyStat != null ? storyStat.getThumbDown() : 0;
		if (thumbDownCount == 0 && op == 1)	return false;
		
		storyStat.setThumbDown(op == 0 ? thumbDownCount+1 : thumbDownCount-1);
		calScore(storyStat);
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
		calScore(storyStat);
		int row = storyStatMapper.updateByPrimaryKey(storyStat);
		if (row == 0) return false;
		
		return true;
	}

	//审查表中插入数据
	@Transactional
	public String publish(StoryAudit story) {
		String brief = story.getContent().length() < 50 ? story.getContent() : story.getContent().substring(0,50);
		story.setBrief(brief);
		String id = story.getId() == null ? String.valueOf(System.currentTimeMillis() % 100000000) 
				: String.valueOf(story.getId());
		story.setId(Integer.valueOf(id));
		
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
			story.setSubId(subId);
			story.setContent(bufferContent);
			storyAuditMapper.insertSelective(story);
		}
		
		return id;
	}
	
	@Transactional
	public boolean report(String id, String reason) {
		StoryReport storyReport = new StoryReport();
		storyReport.setId(Integer.valueOf(id));
		storyReport.setStoryId(Integer.valueOf(id));
		storyReport.setReason(reason);
		int row = storyReportMapper.insertSelective(storyReport);
		return row == 1;
	}

	public List<StoryAudit> auditList(List<String> ids) {
		if (ids.isEmpty()) {
			return new ArrayList<>();
		}
		
		return storyAuditMapper.getStoryAudits(ids);
	}

	//------------------------------------- 管理模块 end -----------------------------------------
}
