package com.yesongdh.bean;

import java.util.List;

import com.github.pagehelper.Page;

public class StoryContentDot {
	
	private List<StoryContent> storyContent;
	
	private StoryStat storyStat;
	
	private Page<StoryContent> page;

	public List<StoryContent> getStoryContent() {
		return storyContent;
	}

	public void setStoryContent(List<StoryContent> storyContent) {
		this.storyContent = storyContent;
	}

	public StoryStat getStoryStat() {
		return storyStat;
	}

	public void setStoryStat(StoryStat storyStat) {
		this.storyStat = storyStat;
	}

	public Page<StoryContent> getPage() {
		return page;
	}

	public void setPage(Page<StoryContent> page) {
		this.page = page;
	}
}
