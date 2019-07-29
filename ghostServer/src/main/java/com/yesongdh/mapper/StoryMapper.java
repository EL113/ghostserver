package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.StoryList;
import com.yesongdh.common.CommonMapper;

public interface StoryMapper extends CommonMapper<StoryList> {

}
