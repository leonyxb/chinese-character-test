package com.xubo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.character.CharacterTestRecords;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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

    @Test
    public void name() throws IOException {
        File json = new File("C:\\Users\\Alain\\Desktop\\Alain学中文\\records\\妈.json");
        if (json.exists()) {
            CharacterTestRecords records = new ObjectMapper().readValue(new File("C:\\Users\\Alain\\Desktop\\Alain学中文\\records\\妈.json"), CharacterTestRecords.class);
            records.getRecords().forEach(System.out::println);
        }
    }
}