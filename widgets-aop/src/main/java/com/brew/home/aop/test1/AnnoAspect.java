package com.brew.home.aop.test1;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author shaogz
 */
@Aspect
public class AnnoAspect {

    @Pointcut("execution(* com.brew.home.aop.test1.App.say(..))")
    public void jointPoint() {
    }

    @Before("jointPoint()")
    public void before() {
        System.out.println("AnnoAspect before say");
    }


    @After("jointPoint()")
    public void after() {
        System.out.println("AnnoAspect after say");
    }

}
