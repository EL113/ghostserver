package com.yesongdh.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
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
    	myShiroRealm.setCacheManager(ehCacheManager());
    	myShiroRealm.setCachingEnabled(true);
        return myShiroRealm;
    }

	//权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
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
//        filtersMap.put("kickout", kickoutSessionFilter());
        filtersMap.put("auth", authPermissionFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        
        Map<String,String> map = new HashMap<String, String>();
        //定义接口和拦截器之间的关系
        map.put("/web/logout","anon");
        map.put("/web/login", "anon");
        map.put("/web/**","auth");
//        map.put("/web/**", "kickout");
//        map.put(/**, value)
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
    
    //限制同一账号登录同时登录人数控制
    @Bean
    public KickoutSessionFilter kickoutSessionFilter() {
        KickoutSessionFilter kickoutSessionControlFilter = new KickoutSessionFilter();
        kickoutSessionControlFilter.setCacheManager(ehCacheManager());
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        return kickoutSessionControlFilter;
    }
    
    @Bean
    public AuthPermissionFilter authPermissionFilter() {
    	AuthPermissionFilter authPermissionFilter = new AuthPermissionFilter();
//    	authPermissionFilter.setUnauthorizedUrl("/ghoststory/web/unauthorized");
        return authPermissionFilter;
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
        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        sessionManager.setGlobalSessionTimeout(1800000);
        //会话调度器 定期删除无效的会话缓存
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(3600000);
        return sessionManager;
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
}
