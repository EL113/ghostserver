package com.yesongdh.common;

import java.lang.reflect.Method;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.yesongdh.annotation.IgnoreToken;
import com.yesongdh.exception.BadRequestException;
import com.yesongdh.util.CryptUtil;

@Aspect
@Configuration
public class RequestLogAop {
	
	@Value("apptoken")
	private String appToken;
	
	public static final Logger log = LoggerFactory.getLogger(RequestLogAop.class);
	
	@Pointcut("execution(public * com.yesongdh.controller.*.*(..))")
	public void logCut() {
		
	}
	
	@Pointcut("within(com.yesongdh.controller.OpenApiController)")
	public void openApiCut() {
		
	}

	//日志记录
	@Around("logCut()")
	public Object interfaceInvokLog(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = getHttpRequest();
		String requestApi = request.getRequestURI();
		long timeMark = System.currentTimeMillis();
		
		Object result = joinPoint.proceed(joinPoint.getArgs());
		
		log.info(requestApi + " request:" + Arrays.toString(joinPoint.getArgs()));
		log.info(requestApi + " during:" + (System.currentTimeMillis() - timeMark));
		log.info(requestApi + " response:" + result);
		return result;
	}
	
	//对外接口的token验证
	@Before("openApiCut()")
	public void openApiToken(JoinPoint joinPoint) {
	 	HttpServletRequest request = getHttpRequest();
	 	String timestamp = request.getHeader("timestamp");
	 	String token = request.getHeader("token");
	 	String uri = request.getRequestURI();
	 	
	 	//不验证token
	 	Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
	 	IgnoreToken ignoreToken = method.getAnnotation(IgnoreToken.class);
	 	if (ignoreToken != null) {
			return;
		}
	 	
	 	if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(token)) {
			throw new BadRequestException("无效请求头");
		}
	 	
	 	if (!uri.contains("openapi") || Long.valueOf(timestamp) < System.currentTimeMillis() - 3000) {
	 		throw new BadRequestException("无效请求头");
		}
	 	
	 	String sign = timestamp + "/" + appToken;
	 	String token1 = CryptUtil.encryptToMD5(sign);
	 	if (!token.equals(token1)) {
			throw new BadRequestException("无效token");
		}
	}
	
	private HttpServletRequest getHttpRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return ((ServletRequestAttributes)requestAttributes).getRequest();
	}
	
}
