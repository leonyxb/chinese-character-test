package com.xubo.data.character;

import com.xubo.data.book.Lesson;
import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.record.TestRecordManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        this.testRecord = TestRecordManager.getInstance().getFullRecords(text);
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

    public void addNewRecord(TestStatus status) {
        TestRecordManager.getInstance().addRecord(text, new CharacterTestRecord(new Date(), status));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        // DO NOT CHECK THE CLASS (a FrenchCharacter should equals Character)
        // if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;
        return Objects.equals(text, character.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
