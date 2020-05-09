package com.xubo.data.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.book.Lesson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Character {

    private static final Logger logger = LogManager.getLogger(Character.class);

    private final String text;

    private final List<DictionaryEntry> dictionaryEntries = new ArrayList<>();

    private final List<Lesson> lessons = new ArrayList<>();

    private final CharacterTestRecords testRecord;

    private final List<String> words = new ArrayList<>();

    private TestStatus status;

    public Character(String text) {
        this.text = text;
        this.status = TestStatus.NOT_TESTED;
        this.testRecord = readTestRecords();
    }

    public String getText() {
        return text;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public List<Lesson> getLessons() {
        return lessons;
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
                logger.error("Error: Can not read the test record from " + json.getAbsolutePath());
            }
        }
        return new CharacterTestRecords(text);
    }

    public void writeTestRecords() {
        File json = getJsonFile();
        try {
            new ObjectMapper().writeValue(json, testRecord);
        } catch (IOException e) {
            logger.error("Error: Can not write the test record to " + json.getAbsolutePath());
        }
    }

    public void addNewRecord(TestStatus status) {
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
