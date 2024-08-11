package com.xubo.data.character;

import com.xubo.application.ApplicationConfig;
import com.xubo.data.book.Lesson;
import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.record.TestRecordManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Character {

    private static final Logger logger = LogManager.getLogger(Character.class);

    private final String text;

    private final List<DictionaryEntry> dictionaryEntries = new ArrayList<>();

    private final List<Lesson> lessons = new ArrayList<>();

    private final CharacterTestRecords testRecord;

    private final List<String> words = new ArrayList<>();

    private final String language;

    private TestStatus status;

    public Character(String text, String language) {
        this.text = text;
        this.status = TestStatus.NOT_TESTED;
        this.language = language;
        this.testRecord = TestRecordManager.getInstance().getFullRecords(text, language);
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
        Calendar cal = Calendar.getInstance();
        TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));

        // for special test
        if (ApplicationConfig.isAdmin && status == TestStatus.KNOWN) {
            cal.add(Calendar.DATE, -1);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -2);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -3);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -5);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -8);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -13);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -21);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -34);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -55);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
            cal.add(Calendar.DATE, -89);
            TestRecordManager.getInstance().addRecord(text, language, new CharacterTestRecord(cal.getTime(), status));
        }
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
