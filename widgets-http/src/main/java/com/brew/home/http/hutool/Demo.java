package com.brew.home.http.hutool;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 * @author shaogz
 * @since 2024/3/8 17:58
 */
public class Demo {

    public static void main(String[] args) {
        System.out.println(1);

        String url = "http://interaction-schedule.csslcloud.net/servlet/v2/interaction-schedule/admin/post";
        HttpResponse execute = HttpRequest.post(url).body("JsonUtil.serialize(bodyList)").execute();
        String body = execute.body();
        System.out.println(1);
    }
}
