package com.xubo.data.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.application.ApplicationUtils;
import com.xubo.data.book.renjiao.RenJiaoBookSource;
import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterTestRecord;
import com.xubo.data.character.CharacterTestRecords;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RenJiaoBookSourceTest {

    //@Test
    public void read() throws IOException {
        File json = new File("C:\\Users\\Alain\\Desktop\\records\\爱.json");
        CharacterTestRecords testRecord = new ObjectMapper().readValue(json, CharacterTestRecords.class);

        Assert.assertNotNull(testRecord);

        List<CharacterTestRecord> perDay = ApplicationUtils.oneRecordPerDay(testRecord.getRecords());

        Assert.assertNotNull(perDay);

        Character character = new Character("妈");
        List<CharacterTestRecord> records = character.getTestRecord().getRecords();
        records.clear();
        records.addAll(testRecord.getRecords());

        ApplicationUtils.Colors displayedColors = ApplicationUtils.getDisplayedColors(character);

        Assert.assertNotNull(displayedColors);
    }

    @Test
    public void test_books() {
        List<Book> books = new RenJiaoBookSource().getBooks();

        for (int i = 0; i <= 6; i+=2) {
            List<Character> book = books.get(i).getLessons().stream().flatMap(l -> l.getCharacters().stream()).collect(Collectors.toList());
            for (int y = i + 2; y <= 6; y+=2) {
                List<Character> nextBook = books.get(y).getLessons().stream().flatMap(l -> l.getCharacters().stream()).collect(Collectors.toList());
                book.forEach(c -> {
                    if (nextBook.contains(c)) {
                        System.out.println(c.getText());
                        c.getLessons().forEach(l -> System.out.println(l.getParentBook().getTitle() + "#" + l.toString()));
                    }
                });
            }
        }



        Assert.assertTrue(true);

    }
}