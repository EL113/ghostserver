package com.yesongdh.bean;

import com.yesongdh.common.CommonPage;

public class StoryContentDot {
	
	private String storyContent;
	
	private StoryStat storyStat;
	
	private CommonPage page;

	public String getStoryContent() {
		return storyContent;
	}

	public void setStoryContent(String storyContent) {
		this.storyContent = storyContent;
	}

	public StoryStat getStoryStat() {
		return storyStat;
	}

	public void setStoryStat(StoryStat storyStat) {
		this.storyStat = storyStat;
	}

	public CommonPage getPage() {
		return page;
	}

	public void setPage(CommonPage page) {
		this.page = page;
	}
}
