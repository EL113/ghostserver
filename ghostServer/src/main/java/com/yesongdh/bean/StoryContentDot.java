package com.yesongdh.bean;

import java.util.List;

public class StoryContentDot {
	
	private int thumbUp = 0;

    private int thumbDown = 0;

    private int collect = 0;
    
    private int maxPage = 0;

    private int currentPage = 0;

    private List<String> content;
	
	public void setStoryStat(StoryStat stat) {
		this.thumbUp = stat.getThumbUp();
		this.thumbDown = stat.getThumbDown();
		this.collect = stat.getCollection();
	}
	
	public int getCollect() {
		return collect;
	}
	
	public void setCollect(int collect) {
		this.collect = collect;
	}
    
	public List<String> getContent() {
		return content;
	}
	
	public void setContent(List<String> content) {
		this.content = content;
	}

	public int getThumbUp() {
		return thumbUp;
	}

	public void setThumbUp(int thumbUp) {
		this.thumbUp = thumbUp;
	}

	public int getThumbDown() {
		return thumbDown;
	}

	public void setThumbDown(int thumbDown) {
		this.thumbDown = thumbDown;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
