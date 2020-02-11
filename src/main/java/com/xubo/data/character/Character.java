package com.xubo.data.character;

import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.book.Lesson;

import java.util.ArrayList;
import java.util.List;

public class Character {

    private final String text;

    private final List<DictionaryEntry> dictionaryEntries = new ArrayList<>();

    private final List<Lesson> lessons = new ArrayList<>();

    CharacterStatus status;

    public Character(String text) {
        this.text = text;
        this.status = CharacterStatus.NOT_TESTED;
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
}
