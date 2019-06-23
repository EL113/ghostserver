package com.yesongdh.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AOP {
	
	@Pointcut("execution(public * com.yesongdh.controller.*.*(..))")
	public void aopPointCut() {
		
	}

	@Around("aopPointCut()")
	public Object interfaceInvokLog(ProceedingJoinPoint joinPoint) {
		printInvokLog(joinPoint);
		
		long timeMark = System.currentTimeMillis();
		
		Object result = proceedFunction(joinPoint);
		
		System.out.println("take time:" + (System.currentTimeMillis() - timeMark));
		
		return result;
	}
	
	public void printInvokLog(ProceedingJoinPoint joinPoint) {
		String interfaceName = joinPoint.getSignature().getName();
		System.out.print(interfaceName + "in :" + Arrays.toString(joinPoint.getArgs()) + ";");
	}
	
	public Object proceedFunction(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
