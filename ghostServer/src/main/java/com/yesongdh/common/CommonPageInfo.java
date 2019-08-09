package com.yesongdh.common;

import java.util.List;

import com.github.pagehelper.PageInfo;

public class CommonPageInfo<T> {
	
	private CommonPage page;	//将分页信息分离出来方便外部封装其他类
	
	private PageInfo<T> pageInfo;

	public CommonPageInfo(List<T> dataList) {
		pageInfo = new PageInfo<T>(dataList);
		page = new CommonPage();
		page.setEndRow(pageInfo.getEndRow());
		page.setHasNextPage(pageInfo.isHasNextPage());
		page.setHasPreviousPage(pageInfo.isHasPreviousPage());
		page.setIsFirstPage(pageInfo.isIsFirstPage());
		page.setIsLastPage(pageInfo.isIsLastPage());
		page.setNavigateFirstPage(pageInfo.getNavigateFirstPage());
		page.setNavigateLastPage(pageInfo.getNavigateLastPage());
		page.setNavigatepageNums(pageInfo.getNavigatepageNums());
		page.setNavigatePages(pageInfo.getNavigatePages());
		page.setNextPage(pageInfo.getNextPage());
		page.setPageNum(pageInfo.getPageNum());
		page.setPages(pageInfo.getPages());
		page.setPageSize(pageInfo.getPageSize());
		page.setPrePage(pageInfo.getPrePage());
		page.setSize(pageInfo.getSize());
		page.setStartRow(pageInfo.getStartRow());
	}
	
	public CommonPage getPage() {
		return page;
	}

	public void setPage(CommonPage page) {
		this.page = page;
	}

	public PageInfo<T> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo<T> pageInfo) {
		this.pageInfo = pageInfo;
	}
	
}
