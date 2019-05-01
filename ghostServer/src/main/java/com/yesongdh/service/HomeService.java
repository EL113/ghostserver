package com.yesongdh.service;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.mapper.HomeMapper;

@Service
public class HomeService {
	
	@Autowired
	HomeMapper homeMapper;

	public JSONObject getRecommend(int startIndex) {
		JSONObject resJson = new JSONObject();
		resJson.put("resJson", 0);
		int pageCount = 15;
		
		List<RecommendId> ids = homeMapper.getRecommendIds(pageCount, startIndex);
		List<RecommendItem> items = new LinkedList<>();
		
		for(RecommendId id: ids) {
			RecommendItem item = homeMapper.getRecommendItem(id);
			if (item != null) {
				items.add(item);
			}
		}
		
		resJson.put("result", items);
		return resJson;
	}
}
