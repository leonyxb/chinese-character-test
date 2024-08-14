package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryLesson implements Lesson {

    private String title;
    private List<Character> characters;
    private Book parentBook;

    public InMemoryLesson(String title, List<Character> characters, Book parentBook) {
        this.title = title;
        this.characters = characters;
        this.parentBook = parentBook;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<Character> getCharacters() {
        return characters;
    }

    @Override
    public Book getParentBook() {
        return parentBook;
    }

    @Override
    public String toString() {
        return "<" + getTitle() + "> " + getCharacters().stream().map(Character::getDisplayText).collect(Collectors.joining(", "));
    }
}
