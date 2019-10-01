package com.yesongdh.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.yesongdh.bean.Admin;
import com.yesongdh.bean.Permission;
import com.yesongdh.bean.Role;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryContent;
import com.yesongdh.bean.StoryList;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonMapper;
import com.yesongdh.mapper.AdminMapper;
import com.yesongdh.mapper.PermissionMapper;
import com.yesongdh.mapper.RoleMapper;
import com.yesongdh.mapper.StoryAuditMapper;
import com.yesongdh.mapper.StoryContentMapper;
import com.yesongdh.mapper.StoryListMapper;
import com.yesongdh.mapper.StoryReportMapper;
import com.yesongdh.mapper.StoryStatMapper;
import com.yesongdh.util.CryptUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Transactional
@Service
public class WebManagerService {

	
	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	PermissionMapper permissionMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	StoryAuditMapper storyAuditMapper;
	
	@Autowired
	StoryListMapper storyListMapper;
	
	@Autowired
	StoryContentMapper storyContentMapper;
	
	@Autowired
	StoryStatMapper storyStatMapper;
	
	@Autowired
	StoryReportMapper storyReportMapper;
	
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

	private String adminAdd(Admin admin) {
		List<Role> roles = admin.getRoles();
		if (roles == null || admin.getRoles().isEmpty()) {
			return "用户角色不能为空";
		}
		
		if (StringUtils.isEmpty(admin.getName())) {
			return "用户名称不能为空";
		}
		
		Admin record = new Admin();
		record.setName(admin.getName());
		List<Admin> admins = adminMapper.select(record);
		if (admins != null && !admins.isEmpty()) {
			return "用户名称重复";
		}
		
		//设置用户信息
		String roleName = roles.size() == 1 ? roles.get(0).getRoleName() : getRoleName(roles);
		admin.setRole(roleName);
		String uuId = UUID.randomUUID().toString();
		admin.setSalt(uuId);
		adminMapper.insert(admin);
		admin = adminMapper.selectOne(admin);
		
		//设置新角色信息和权限信息
		Role role = new Role();
		role.setRoleName(roleName);
		roleMapper.insertSelective(role);
		Role roleRecord = roleMapper.selectOne(role);
		
		List<String> permsList = setPermList(roles);
		roleMapper.insertRolePerms(roleRecord.getId(), permsList);
		return null;
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
		PageHelper.startPage(pageNo, pageSize);
		List<Admin> adminList = adminMapper.select(admin);
		return adminList;
	}

	public String adminOperate(Admin admin, String operate) {
		operate = operate.toLowerCase();
		if ("add".equals(operate)) {
			return adminAdd(admin);
		}
		
		if ("delete".equals(operate)) {		//TODO 可能需要批量删除接口
			adminMapper.deleteByPrimaryKey(admin.getId());
		}
		
		if ("mod".equals(operate)) {
			admin.clearField();		//清除掉部分不能修改的数据
			CryptUtil.encrptPasswd(admin);
			adminMapper.updateByPrimaryKeySelective(admin);
		}
		
		return null;
	}

	public String permOperate(String operate, List<Permission> permissions) {
		String checkName = null;
		if ("add".equals(operate) || "mod".equals(operate)) {
			checkName = checkPermName(permissions);
		}
		
		if (checkName != null) {
			return checkName;
		}
		
		return operate(operate, permissions, permissionMapper) ? null : "操作失败";
	}
	
	private String checkPermName(List<Permission> permissions) {
		Set<String> permNames = new HashSet<>();
		for (Permission perm : permissions) {
			permNames.add(perm.getPermissionName());
		}
		
		if (permNames.size() != permissions.size()) {
			return "存在同名权限";
		}
		
		Example example = new Example(Permission.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("permissionName", permNames);
		List<Permission> permRecords = permissionMapper.selectByExample(example);
		return permRecords.isEmpty() ? null : "存在同名权限";
	}

	public List<Permission> permList(Permission permission, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		
		if (permission.getRoleId() != null) {
			return permissionMapper.permList(permission.getRoleId());
		}
		
		return permissionMapper.select(permission);
	}

	
	public boolean roleOperate(String operate, List<Role> roles) {
		if ("add".equals(operate) && !checkRoleNames(roles)) {
			return false;
		}
		
		operate(operate, roles, roleMapper);
		
		if ("add".equals(operate) || "mod".equals(operate)) {
			insertRolePerms(roles);
		}
		
		if ("del".equals(operate)) {
			deleteRolePerms(roles);
		}
		return true;
	}
	
	private void insertRolePerms(List<Role> roles) {
		for(Role role: roles) {
			Role role2 = roleMapper.selectOne(role);
			roleMapper.deleteRolePerms(role2.getId());
			roleMapper.insertRolePerms(role2.getId(), role.getPerms());
		}
	}
	
	private void deleteRolePerms(List<Role> roles) {
		for(Role role: roles) {
			Role role2 = roleMapper.selectOne(role);
			roleMapper.deleteRolePerms(role2.getId());
		}
	}
	
	private boolean checkRoleNames(List<Role> roles) {
		Set<String> roleNames = new HashSet<>();
		for(Role role: roles)
			roleNames.add(role.getRoleName());
		
		if (roleNames.size() != roles.size()) {
			return false;
		}
		
		Example example = new Example(Role.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("roleName", roleNames);
		List<Role> roles2 = roleMapper.selectByExample(example);
		if (roles2 != null && !roles2.isEmpty()) {
			return false;
		}
		
		return true;
	}

	public List<Role> roleList(Role role, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		return roleMapper.select(role);
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
}
