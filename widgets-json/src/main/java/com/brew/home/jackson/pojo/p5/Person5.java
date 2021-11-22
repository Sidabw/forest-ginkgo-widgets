package com.brew.home.jackson.pojo.p5;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author shaogz
 */
public class Person5 extends Person5Parent{

    @JsonProperty(value = "n2")
    private String name;

    private int age;

    public Person5(String name, int age) {
        super(true);
        this.name = name;
        this.age = age;
    }

    public Person5() {
        super(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
