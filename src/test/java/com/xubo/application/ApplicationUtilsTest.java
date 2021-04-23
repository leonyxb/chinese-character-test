package com.xubo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.book.Book;
import com.xubo.data.book.common.CommonBookSourceExternal;
import com.xubo.data.character.CharacterTestRecords;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApplicationUtilsTest {

    @Test
    public void name() throws IOException {
        File json = new File("C:\\Users\\Alain\\Desktop\\Alain学中文\\records\\妈.json");
        if (json.exists()) {
            CharacterTestRecords records = new ObjectMapper().readValue(new File("C:\\Users\\Alain\\Desktop\\Alain学中文\\records\\妈.json"), CharacterTestRecords.class);
            records.getRecords().forEach(System.out::println);
        }
    }
}