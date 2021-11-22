/**
 * Copyright (C), 2018-2020, zenki.ai
 * FileName: ApacheHttpUtils
 * Author:   feiyi
 * Date:     2020/10/15 10:37 AM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.brew.home.http.apache;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 〈一句话功能简述〉:
 * 〈〉
 *
 * @author feiyi
 * @create 2020/10/15
 * @since 1.0.0
 */
public class ApacheHttpUtils {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String executePost(String url, Object reqBody) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        ObjectMapper objectMapper = new ObjectMapper();
        StringEntity stringEntity = new StringEntity(objectMapper.writeValueAsString(reqBody), StandardCharsets.UTF_8);
//        stringEntity.setContentEncoding("UTF-8");
        //发送json数据需要设置contentType
//        stringEntity.setContentType("application/json");
//        httpPost.addHeader("Content-type","application/json; charset=utf-8");
//        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type","application/json");
        System.out.println("prepare request, request line: " + httpPost);
        System.out.println("prepare request, request body: " + JSONObject.toJSONString(stringEntity));

        //发送请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String string = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            string = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
        //关闭资源
        response.close();
        System.out.println("request finished, response body: {}" + string);
        return string;
    }

    public static void main(String[] args) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "将阿斯利康的减肥啦开始减肥的了");
        map.put("age", 1);

        executePost("http://127.0.0.1:8021/test", map);
    }

}
