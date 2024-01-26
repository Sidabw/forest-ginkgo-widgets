package com.brew.home.jackson.pojo.p6;

public class Person6 {

    private final Integer a1;

    private final int a2;

    private final String a3;

    public Person6(Integer a1, int a2, String a3) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    public Integer getA1() {
        return a1;
    }

    public int getA2() {
        return a2;
    }

    public String getA3() {
        return a3;
    }

    @Override
    public String toString() {
        return "Person6{" +
                "a1=" + a1 +
                ", a2=" + a2 +
                ", a3='" + a3 + '\'' +
                '}';
    }
}
