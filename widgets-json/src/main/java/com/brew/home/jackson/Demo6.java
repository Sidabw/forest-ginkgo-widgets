package com.brew.home.jackson;

import com.brew.home.jackson.pojo.p6.Person6;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Demo6 {

    public static void main(String[] args) throws IOException {

        Person6 person6 = new ObjectMapper().readValue("{\"a1\":123}", Person6.class);
        System.out.println(person6);

    }
}
