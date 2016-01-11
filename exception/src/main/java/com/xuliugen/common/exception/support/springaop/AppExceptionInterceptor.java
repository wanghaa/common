package com.xuliugen.common.exception.support.springaop;

import com.xuliugen.common.exception.base.BaseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 类    名：AppExceptionInterceptor.java<br />
 *   
 * 功能描述：应用层异常拦截器	<br />
 *  
 * 创建日期：2012-11-20下午05:38:54  <br />   
 * 
 * 版本信息：v 1.0<br />
 * 
 * 版权信息：Copyright (c) 2011 Csair All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class AppExceptionInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionInterceptor.class);
    

    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Method method = null;
        Class<? extends Object> target = point.getTarget().getClass();
        Method[] methods = target.getDeclaredMethods();
        for (Method tmp : methods) {
            if (point.getSignature().getName().equals(tmp.getName())) {
                method = tmp;
                break;
            }
        }

        Object o = null;
        try {
            o = point.proceed();
        } catch (Exception e) {
            logger.error("Method["+target.getName() + "." + method.getName() + "]", e);
            throw new BaseException(e);
        }
        return o;
    }
}
