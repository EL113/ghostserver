package com.yesongdh.common;

import java.lang.reflect.Method;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
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
	
	@Pointcut("excution(public * com.yesongdh.controller.OpenApiController.*(..))")
	public void openApiCut() {
		
	}

	//日志记录
	@Around("logCut()")
	public Object interfaceInvokLog(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = getHttpRequest();
		String requestApi = request.getRequestURI();
		long timeMark = System.currentTimeMillis();
		
		JSONObject result = (JSONObject)joinPoint.proceed(joinPoint.getArgs());
		
		log.info(requestApi + " request:" + Arrays.toString(joinPoint.getArgs()));
		log.info(requestApi + " during:" + (System.currentTimeMillis() - timeMark));
		log.info(requestApi + " response:" + result.toJSONString());
		return result;
	}
	
	//对外接口的token验证
	@Before("openApiCut()")
	public void openApiToken(ProceedingJoinPoint joinPoint) {
	 	getAnnotation(joinPoint);
	 	HttpServletRequest request = getHttpRequest();
	 	String timestamp = request.getHeader("timestamp");
	 	String token = request.getHeader("token");
	 	String uri = request.getRequestURI();
	 	
	 	//不验证token
	 	IgnoreToken ignoreToken = getAnnotation(joinPoint);
	 	if (ignoreToken != null) {
			return;
		}
	 	
	 	if (!uri.contains("openapi") || Long.valueOf(timestamp) < System.currentTimeMillis() - 3000) {
	 		throw new BadRequestException("token invalid");
		}
	 	
	 	String sign = timestamp + "/" + appToken;
	 	String token1 = CryptUtil.encryptToMD5(sign);
	 	if (StringUtils.isEmpty(token1) || !token1.equals(token)) {
			throw new BadRequestException("token invalid");
		}
	}
	
	private IgnoreToken getAnnotation(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < args.length; i++) {
              argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method.getAnnotation(IgnoreToken.class);
	}
	
	private HttpServletRequest getHttpRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return ((ServletRequestAttributes)requestAttributes).getRequest();
	}
	
}
