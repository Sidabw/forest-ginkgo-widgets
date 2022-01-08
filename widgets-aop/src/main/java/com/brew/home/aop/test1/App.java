package com.brew.home.aop.test1;

/**
 * @author shaogz
 */
public class App {

    public void say() {
        System.out.println("App say");
    }

    public static void main(String[] args) {
        App app = new App();
        app.say();
    }

}
