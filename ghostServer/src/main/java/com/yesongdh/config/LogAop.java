package com.yesongdh.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class LogAop {
	
	@Pointcut("execution(public * com.yesongdh.controller.*.*(..))")
	public void aopPointCut() {
		
	}

	@Around("aopPointCut()")
	public Object interfaceInvokLog(ProceedingJoinPoint joinPoint) {
		String interfaceName = joinPoint.getSignature().getName();
		log.info(interfaceName + " params:" + Arrays.toString(joinPoint.getArgs()));
		
		long timeMark = System.currentTimeMillis();
		
		Object result = proceedFunction(joinPoint);
		
		log.info(interfaceName + " take time:" + (System.currentTimeMillis() - timeMark));
		
		log.info(interfaceName + " result:" + result.toString());
		
		return result;
	}
	
	public Object proceedFunction(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
