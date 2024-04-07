package com.brew.home.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/***
 *  Created by shao.guangze on 2018/8/13
 */
public class SendHtmlMailDemo {
    public static void main(String[] args) throws FileNotFoundException {

        System.out.println(SendHtmlMailDemo.class.getResource("/mystaticdir/pageTemplate.html").getPath());
        new FileInputStream(new File("src/main/resources/mystaticdir/pageTemplate.html"));
    }
}
