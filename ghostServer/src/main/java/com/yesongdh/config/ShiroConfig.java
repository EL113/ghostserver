package com.yesongdh.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;

import com.yesongdh.shiro.AuthPermissionFilter;
import com.yesongdh.shiro.CustomCredentialsMatcher;
import com.yesongdh.shiro.KickoutSessionFilter;
import com.yesongdh.shiro.ShiroRealm;

@Configuration
public class ShiroConfig {
	
	//将自己的验证方式加入容器
    @Bean
    public ShiroRealm shiroRealm() {
    	ShiroRealm myShiroRealm = new ShiroRealm();
    	myShiroRealm.setCredentialsMatcher(CustomCredentialsMatcher());
//    	myShiroRealm.setCacheManager(ehCacheManager());
//    	myShiroRealm.setCachingEnabled(true);
        return myShiroRealm;
    }

	//权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setSessionManager(sessionManager());
//        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/web/error");
        shiroFilterFactoryBean.setUnauthorizedUrl("/web/unauthorized");
        
        //自定义shiro拦截器 同时在线人数控制拦截器 权限认证拦截器 
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("kickout", kickoutSessionFilter());
        filtersMap.put("auth", authPermissionFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        
        //定义接口和拦截器之间的关系 先检查权限 保证经过kickout过滤器的用户一定是登录过的
        //经过kickout的未登录用户一定是对外接口
        Map<String,String> map = new HashMap<String, String>();
        map.put("/web/logout","anon");
        map.put("/web/login", "anon");
        map.put("/web/**","auth");
        map.put("/**", "kickout");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    //限制同一账号登录同时登录人数控制和权限控制
    @Bean
    public AuthPermissionFilter authPermissionFilter() {
    	AuthPermissionFilter authPermissionFilter = new AuthPermissionFilter();
        return authPermissionFilter;
    }
    
    @Bean
    public KickoutSessionFilter kickoutSessionFilter() {
    	KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
    	kickoutSessionFilter.setCacheManager(ehCacheManager());
    	kickoutSessionFilter.setSessionManager(sessionManager());
        return kickoutSessionFilter;
    }
    
    //采用自定义的密码校验器，密码错误重试次数
    @Bean
    public CredentialsMatcher CustomCredentialsMatcher() {
    	CustomCredentialsMatcher credentialsMatcher = new CustomCredentialsMatcher();
    	credentialsMatcher.setHashAlgorithmName("md5");
    	credentialsMatcher.setHashIterations(1024);
    	credentialsMatcher.setStoredCredentialsHexEncoded(true);
    	credentialsMatcher.retryLimitHashedCredentialsMatcher(ehCacheManager());
		return credentialsMatcher;
	}
    
    //会话管理
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setGlobalSessionTimeout(1800000);
//        sessionManager.setCacheManager(ehCacheManager());
        //会话调度器 定期删除无效的会话缓存
//        sessionManager.setDeleteInvalidSessions(true);
//        sessionManager.setSessionValidationScheduler(configSessionValidationScheduler());
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        sessionManager.setSessionIdCookieEnabled(false);
//        sessionManager.setSessionIdUrlRewritingEnabled(true);
//        sessionManager.setSessionValidationInterval(3600000);
//        SimpleCookie simpleCookie = new SimpleCookie("shiro.session");
//        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }
    
    @Bean
    public ExecutorServiceSessionValidationScheduler configSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        //设置session的失效扫描间隔，单位为毫秒
        sessionValidationScheduler.setInterval(300*1000);
        return sessionValidationScheduler;
    }
    
    //缓存管理器
    @Bean
    public EhCacheManager ehCacheManager() {
    	EhCacheManager ehCacheManager = new EhCacheManager();
    	ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;
    }
    
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        sessionDAO.setActiveSessionsCacheName("shiro-kickout-session");
//        sessionDAO.setCacheManager(ehCacheManager());
        return sessionDAO;
    }
}
