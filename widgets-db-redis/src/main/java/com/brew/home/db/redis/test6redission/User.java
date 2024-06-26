package com.brew.home.db.redis.test6redission;

import java.io.Serializable;

/**
 * @author shaogz
 * @since 2024/5/8 10:15
 */
public class User implements Serializable {

    private String name;

    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
