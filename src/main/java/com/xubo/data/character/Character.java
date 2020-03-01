package com.xubo.data.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.book.Lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Character {

    private final String text;

    private final List<DictionaryEntry> dictionaryEntries = new ArrayList<>();

    private final List<Lesson> lessons = new ArrayList<>();

    private final CharacterTestRecords testRecord;

    private final List<String> words = new ArrayList<>();

    CharacterStatus status;

    public Character(String text) {
        this.text = text;
        this.status = CharacterStatus.NOT_TESTED;
        this.testRecord = readTestRecords();
    }

    public String getText() {
        return text;
    }

    public CharacterStatus getStatus() {
        return status;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setStatus(CharacterStatus status) {
        this.status = status;
    }

    public List<DictionaryEntry> getDictionaryEntries() {
        return dictionaryEntries;
    }

    public List<String> getWords() {
        return words;
    }

    public CharacterTestRecords getTestRecord() {
        return testRecord;
    }

    public CharacterTestRecords readTestRecords() {
        File json = getJsonFile();
        if (json.exists()) {
            try {
                return new ObjectMapper().readValue(json, CharacterTestRecords.class);
            } catch (IOException e) {
                System.out.println("Error: Can not read the test record from " + json.getAbsolutePath());
            }
        }
        return new CharacterTestRecords(text);
    }

    public void writeTestRecords() {
        File json = getJsonFile();
        try {
            new ObjectMapper().writeValue(json, testRecord);
        } catch (IOException e) {
            System.out.println("Error: Can not write the test record to " + json.getAbsolutePath());
        }
    }

    public void addNewRecord(CharacterStatus status) {
        this.testRecord.getRecords().add(new CharacterTestRecord(new Date(), status));
        writeTestRecords();
    }

    private File getJsonFile() {
        File baseDir = new File("records");
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            baseDir.mkdirs();
        }
        return new File("records" + File.separator + text + ".json");
    }
}
