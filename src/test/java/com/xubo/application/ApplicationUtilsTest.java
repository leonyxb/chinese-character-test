package com.xubo.application;

import org.junit.Test;

public class ApplicationUtilsTest {

    @Test
    public void test() {

        System.out.println(ApplicationUtils.getRandomDays("我", 10));
        System.out.println(ApplicationUtils.getRandomDays("马", 10));
        System.out.println(ApplicationUtils.getRandomDays("天", 10));
        System.out.println(ApplicationUtils.getRandomDays("妈", 10));
        System.out.println(ApplicationUtils.getRandomDays("地", 10));
        System.out.println(ApplicationUtils.getRandomDays("牛", 10));
        System.out.println(ApplicationUtils.getRandomDays("土", 10));
        System.out.println(ApplicationUtils.getRandomDays("票", 10));
        System.out.println(ApplicationUtils.getRandomDays("美", 10));
    }
}