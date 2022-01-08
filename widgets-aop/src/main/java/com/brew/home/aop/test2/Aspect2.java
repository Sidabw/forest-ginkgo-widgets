package com.brew.home.aop.test2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author shaogz
 */
@Aspect
public class Aspect2 {

    @Pointcut(value = "@annotation(nonAuth)", argNames = "nonAuth")
    public void jointPoint(NonAuth nonAuth) {
    }

    @Around(value = "jointPoint(nonAuth)", argNames = "jp,nonAuth")
    public Object around(ProceedingJoinPoint jp, NonAuth nonAuth) throws Throwable {
        //必须这么写..  暂时没有测试提高ajc的版本会不会修复此bug
        //https://blog.csdn.net/qq_43667968/article/details/86561508
        if (!jp.getKind().equals("method-execution")) {
            return jp.proceed();
        }

        System.out.println("Aspect2 before say");
        System.out.println(jp.getKind());
        Object res = jp.proceed();
        System.out.println("Aspect2 after say");
        return res;
    }

}
