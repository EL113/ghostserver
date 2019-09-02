package com.yesongdh.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yesongdh.bean.Admin;
import com.yesongdh.bean.Permission;
import com.yesongdh.bean.Role;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryContent;
import com.yesongdh.bean.StoryContentDot;
import com.yesongdh.bean.StoryList;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonMapper;
import com.yesongdh.common.CommonPage;
import com.yesongdh.common.CommonPageInfo;
import com.yesongdh.common.ScoreStrategy;
import com.yesongdh.common.ScoreStrategyImp;
import com.yesongdh.mapper.AdminMapper;
import com.yesongdh.mapper.HomeMapper;
import com.yesongdh.mapper.PermissionMapper;
import com.yesongdh.mapper.RoleMapper;
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
	
	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	PermissionMapper permissionMapper;
	
	@Autowired
	RoleMapper roleMapper;

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
		String brief = story.getContent().substring(0,50);
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
	
	//----------------------------------------- 管理模块 -----------------------------------------
	
	public List<StoryAudit> auditList(StoryAudit storyAudit, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<StoryAudit> storyAudits = storyAuditMapper.getStoryAuditsByCondition(storyAudit);
		return storyAudits;
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
	

	public List<StoryReport> reportList(StoryReport storyReport, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<StoryReport> storyReports = storyReportMapper.getStoryReportsByCondition(storyReport);
		return storyReports;
	}
	
	public boolean handleReport(String id, String handler, String reason) {
		StoryReport report = new StoryReport();
		report.setId((int)System.currentTimeMillis() % 1000000);
		report.setHandler(handler);
		report.setStoryId(Integer.valueOf(id));
		int row = storyReportMapper.insertSelective(report);
		return row == 1;
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

	public boolean deleteStory(String id) {
		StoryList storyList = new StoryList();
		storyList.setId(Integer.valueOf(id));
		
		StoryContent storyContent = new StoryContent();
		storyContent.setId(Integer.valueOf(id));
		
		storyListMapper.delete(storyList);
		storyStatMapper.deleteByPrimaryKey(id);
		storyContentMapper.delete(storyContent);
		return true;
	}

	private boolean adminAdd(Admin admin) {
		List<Role> roles = admin.getRoles();
		if (roles == null || admin.getRoles().isEmpty()) {
			return false;
		}
		
		//设置用户信息
		int adminId = (int) (System.currentTimeMillis() % 1000000);
		String roleName = roles.size() == 1 ? roles.get(0).getRoleName() : getRoleName(roles);
		admin.setRole(roleName);
		admin.setId(adminId);
		adminMapper.insert(admin);
		
		//设置新角色信息和权限信息
		adminMapper.insertRole(roleName);
		List<String> permsList = setPermList(roles);
		adminMapper.insertRolePerms(adminId, permsList);
		return true;
	}

	private List<String> setPermList(List<Role> roles) {
		Set<String> permsList = new HashSet<>();
		for (Role role : roles) {
			permsList.addAll(role.getPerms());
		}
		return new ArrayList<String>(permsList);
	}

	private String getRoleName(List<Role> roles) {
		StringBuffer roleName = new StringBuffer();
		for (int i = 0; i < roles.size(); i++) {
			Role role = roles.get(0);
			if (i == 0) {
				roleName.append(role.getRoleName());
			} else {
				roleName.append("_").append(role.getRoleName());
			}
		}
		return roleName.toString();
	}

	public List<Admin> adminList(Admin admin, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Admin> adminList = adminMapper.selectByRowBounds(admin, rowBounds);
		return adminList;
	}

	public boolean adminOperate(Admin admin, String operate) {
		operate = operate.toLowerCase();
		if ("add".equals(operate)) {
			return adminAdd(admin);
		}
		
		if ("delete".equals(operate)) {
			return adminMapper.deleteByPrimaryKey(admin.getId()) == 1;
		}
		
		if ("mod".equals(operate)) {
			return adminMapper.updateByPrimaryKeySelective(admin) == 1;
		}
		
		return false;
	}

	public boolean permOperate(String operate, List<Permission> permissions) {
		return operate(operate, permissions, permissionMapper);
	}

	public List<Permission> permList(String role) {
		return permissionMapper.permList(role);
	}

	private <T> boolean operate(String operate, List<T> operateList, CommonMapper<T> commonMapper) {
		operate = operate.toLowerCase();
		if ("add".equals(operate)) {
			return commonMapper.insertList(operateList) == operateList.size();
		}
		
		if ("delete".equals(operate)) {
			for (T operateItem : operateList) {
				commonMapper.delete(operateItem);
			}
		}
		
		if ("mod".equals(operate)) {
			for (T operateItem : operateList) {
				commonMapper.updateByPrimaryKeySelective(operateItem);
			}
		}
		return true;
	}

	public boolean roleOperate(String operate, List<Role> roles) {
		return operate(operate, roles, roleMapper);
	}
	
	public List<Permission> roleList(String admin) {
		return roleMapper.roleList(admin);
	}
	//------------------------------------- 管理模块 end -----------------------------------------
}
