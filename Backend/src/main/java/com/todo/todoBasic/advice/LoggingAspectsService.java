package com.todo.todoBasic.advice;

import java.util.HashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspectsService {
  
    @Pointcut("execution(* com.todo.todoBasic.service..*(..))")
    private void serviceLayerExecution(){}

   private static HashMap<String, String> getClassAndMethodName(JoinPoint joinPoint) {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    HashMap<String, String> classHashMap = new HashMap<>();
    classHashMap.put("className", className);
    classHashMap.put("methodName", methodName);
    return classHashMap;
}
    @Before("serviceLayerExecution()")
private void LoggingAdviceBeforeCalling(JoinPoint joinPoint) {
    HashMap<String, String> classAndMethod = getClassAndMethodName(joinPoint);
    System.out.println("Before Executing " + classAndMethod.get("className") + " - " + classAndMethod.get("methodName") + " method");
}

@After("serviceLayerExecution()")
private void LoggingAdviceAfterCalling(JoinPoint joinPoint) {
    HashMap<String, String> classAndMethod = getClassAndMethodName(joinPoint);
    System.out.println("After Executing " + classAndMethod.get("className") + " - " + classAndMethod.get("methodName") + " method");
}



}
