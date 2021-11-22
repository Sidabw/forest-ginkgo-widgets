package com.brew.home.jackson;

import com.brew.home.jackson.pojo.p5.Person5;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author shaogz
 */
public class Demo4 {

    public static void main(String[] args) {
        //jack bean to map
        //经过测试：1.是可以覆盖bean的父类的2.bean里有关jackson的注解也是生效的
        Person5 sida = new Person5("sida", 11);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> res = objectMapper.convertValue(sida, new TypeReference<Map<String, Object>>() {});
        System.out.println(res);
    }

}
