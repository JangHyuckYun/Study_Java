package com.custom.custom_annotation.aop;

import com.custom.custom_annotation.annotation.MyAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class TestAOP {

    @Pointcut("@annotation(com.custom.custom_annotation.annotation.MyAnnotation)")
    public void requiredMyAnnotation() {}
    @Before("execution(* com.custom.custom_annotation.controller.MainController.*(..)) && requiredMyAnnotation()")
    public void testLogging(JoinPoint joinPoint) throws Throwable {
        log.info("==================== Aspect TEST ( MyAnnotation ) ====================");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //method 이름
        log.info("===> signature.getName()::"+signature.getName());
        // 메서드의 클래스 패키지
        log.info("===> signature.getDeclaringTypeName()::"+signature.getDeclaringTypeName());
        // 메서드 return type
        log.info("===> signature.getReturnType()::"+signature.getReturnType());
        // 메서드의 클래스 패키지 + 메서드 이름
        log.info("===> signature.getMethod()::"+signature.getMethod());

        // 해당 메서드의 사용되는 어노테이션 리스트
        log.info("===> annotation list:: "+ Arrays.toString(signature.getMethod().getAnnotations()));

        MyAnnotation annotation = signature.getMethod().getAnnotation(MyAnnotation.class);

        log.info("===> MyAnnotation name:: "+annotation.name());
        log.info("===> MyAnnotation age:: "+annotation.age());

        log.info("======================================================================");
    }
}
