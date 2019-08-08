package com.yesongdh.common;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import com.alibaba.fastjson.JSONObject;

@Aspect
@Configuration
public class RequestLogAop {
	
	public static final Logger log = LoggerFactory.getLogger(RequestLogAop.class);
	
	@Pointcut("execution(public * com.yesongdh.controller.*.*(..))")
	public void aopPointCut() {
		
	}

	@Around("aopPointCut()")
	public Object interfaceInvokLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String requestApi = joinPoint.getSignature().getName();
		long timeMark = System.currentTimeMillis();
		
		JSONObject result = (JSONObject)joinPoint.proceed(joinPoint.getArgs());
		
		log.info(requestApi + " request:" + Arrays.toString(joinPoint.getArgs()));
		log.info(requestApi + " during:" + (System.currentTimeMillis() - timeMark));
		log.info(requestApi + " response:" + result.toJSONString());
		return result;
	}
}
