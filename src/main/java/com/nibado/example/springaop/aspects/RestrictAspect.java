package com.nibado.example.springaop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RestrictAspect {
    @Around("@annotation(com.nibado.example.springaop.aspects.Restrict) && execution(public * *(..))")
    public Object localOnly(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final Method method = signature.getMethod();
        final Restrict annotation = method.getAnnotation(Restrict.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if (annotation.admin() && !isAdmin(request)) {
            throw new ForbiddenException("Need admin access");
        }

        if (annotation.localOnly() && !request.getRemoteAddr().equals("127.0.0.1") && !request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            throw new ForbiddenException("Only possible from localhost");
        }

        return proceedingJoinPoint.proceed();
    }

    private static boolean isAdmin(final HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        return authorization != null && authorization.replace("Bearer ", "").equalsIgnoreCase("admin");
    }
}
