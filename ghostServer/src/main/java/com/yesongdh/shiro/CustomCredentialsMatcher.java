package com.yesongdh.shiro;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.yesongdh.bean.Admin;
import com.yesongdh.mapper.AdminMapper;

/**
 * 自定义账号密码验证，登录密码错误次数限制
 */
public class CustomCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;

    /**
     * 获取ehcache缓存
     *
     * @param cacheManager
     */
    public void retryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("shiro-kickout-session");
    }

    @Autowired
    private AdminMapper adminMapper;

    /**
     *登录拦截，密码错误次数统计并于三次后更新用户锁为true
     *
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        String userAccount = (String) token.getPrincipal();
        // retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(userAccount);

        Admin userAdmin = new Admin();
        userAdmin.setName(userAccount);
        List<Admin> users = adminMapper.select(userAdmin);
        if (users == null || users.isEmpty()) {
			return false;
		}
        
        Admin user = users.get(0);
        //如果为null，则表示30分钟内未尝试过登录，自动解锁账号
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            if (user.getLock() == 1){
                user.setLock(0);
                adminMapper.updateByPrimaryKeySelective(user);
            }
            passwordRetryCache.put(userAccount, retryCount);    //将登录次数存入当前登录用户中
        }
        //锁定后重置错误计数为0
        if (user.getLock() == 1){
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(userAccount, retryCount);
            throw new ExcessiveAttemptsException();     //抛出超出登录次数异常
        }

        //增加错误计数 锁定操作
        if (retryCount.incrementAndGet() > 2) {
            user.setLock(1);
            adminMapper.updateByPrimaryKeySelective(user);
        }

        //调用父类的密码验证
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(userAccount);     //移除当前用户的登录值缓存
        }
        return matches;
    }

}
