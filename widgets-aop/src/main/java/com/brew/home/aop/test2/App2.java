package com.brew.home.aop.test2;


/**
 * @author shaogz
 */
public class App2 {

    @NonAuth
    public void say() {
        System.out.println("App say");
    }



}
