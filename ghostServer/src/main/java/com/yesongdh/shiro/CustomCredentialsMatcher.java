package com.yesongdh.shiro;

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
        passwordRetryCache = cacheManager.getCache("passwd-retry-count");
    }

    @Autowired
    private AdminMapper adminMapper;

    //密码验证，true表示验证成功，false将在login抛出验证异常，密码错误异常需要手动抛出
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        String userAccount = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(userAccount);

        Admin record = new Admin();
        record.setName(userAccount);
        Admin user = adminMapper.selectOne(record);
        
        //如果账户被锁定并缓存为空 则是缓存消失 已经自动解锁 手动解锁 新建缓存置0
        if (user.getUserLock() == 1 && retryCount == null) {
        	user.setUserLock(0);
            adminMapper.updateByPrimaryKeySelective(user);
		} else if (user.getUserLock() == 1) {
			//账号已被锁定 还没由被自动解锁
			throw new ExcessiveAttemptsException("账号已被锁定");
		}

        //账号未被锁定  调用父类的密码验证
        boolean matches = super.doCredentialsMatch(token, info);
        //如果验证成功移除当前用户的登录值缓存
        if (matches) {
            passwordRetryCache.remove(userAccount);
            return matches;
        }
        
        //首次密码错误
        if (retryCount == null) {
        	passwordRetryCache.put(userAccount, new AtomicInteger(1));
        	return matches;
		}
        //增加错误计数 如果超过次数则执行锁定操作 缓存置0
        if (retryCount.incrementAndGet() > 3) {
            user.setUserLock(1);
            passwordRetryCache.put(userAccount, new AtomicInteger(0));
            adminMapper.updateByPrimaryKeySelective(user);
            throw new ExcessiveAttemptsException("账号已被锁定");
        }
        
        return matches;
    }

}
