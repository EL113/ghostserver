package com.yesongdh.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yesongdh.bean.StoryAudit;
import com.yesongdh.mapper.StoryAuditMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component
public class Timer {

	@Autowired
	StoryAuditMapper storyAuditMapper;
	
//	@Scheduled(cron = "0 0 0 1 * *")
//	public void deleteInvalidStory() {
//		Example example = new Example(StoryAudit.class);
//		Criteria criteria = example.createCriteria();
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_MONTH, -30);
//		criteria.andLessThan("create_time", calendar.getTime());
//		storyAuditMapper.deleteByExample(example);
//	}
}
