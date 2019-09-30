package com.yesongdh.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import com.alibaba.fastjson.JSONObject;

public class KickoutSessionFilter extends AccessControlFilter {

    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

    //shiro拦截链到这里后调用该方法，返回true直接进入下一个拦截器，返回false则调用 onaccessdenied
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /**
     * 返回true则继续执行后面的shiro拦截器链，false则直接退出
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        String username = (String) subject.getPrincipal()+":kickout";
        Serializable sessionId = session.getId();

        //存储session记录
        Deque<Serializable> deque = cacheSession(session, username, sessionId);

        markickoutSession(deque, username);
        
        //检测session是否被踢出 踢出就是直接下线
        if (session.getAttribute("kickout") != null) {
            kickoutSession(subject, request, response);
            return false;
        }
        return true;
    }

	private Deque<Serializable> cacheSession(Session session, String username, Serializable sessionId) {
		Deque<Serializable> deque = cache.get(username);
//		System.out.println("-------------------------------------------------getrediscache");
        if(deque==null){
            deque = new LinkedList<Serializable>();
        }

        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
            cache.put(username, deque);
        }
		return deque;
	}
    
    //推出队列中的sessionId，标记被提出的session的kickout为true
    private void markickoutSession(Deque<Serializable> deque, String username) {
    	System.out.println("-----------------------------:"+deque);
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = deque.removeLast();
            cache.put(username, deque);
            
            Session kickoutSession = null;
            try {
            	kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
			} catch (Exception e) {
				//退出登录并不会处理kickout-session里面的sessionid，如果没有找到sessionid，则什么都不做
				//或者在退出接口注入sessionmanager，手动删除sessionid
			}
            
            if(kickoutSession != null) {
                kickoutSession.setAttribute("kickout", true);
            }
        }
	}
    
    private void kickoutSession(Subject subject, ServletRequest request, ServletResponse response)
            throws IOException {
    	subject.logout();
        saveRequest(request);
        
        JSONObject resJson = new JSONObject();
        resJson.put("user_status", "300");
        resJson.put("message", "您已经在其他地方登录，请重新登录！");
    	System.out.println("---------------------------logout");
    	PrintWriter out = response.getWriter();
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out.write(resJson.toJSONString());
            out.flush();
        } catch (Exception e) {
//            System.err.println("KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        } finally {
        	out.close();
        }
    }
}
