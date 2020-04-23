package com.xubo.data.book.common;

import com.xubo.data.character.Character;
import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.CharacterFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CommonLesson implements Lesson {

    private String rawline;

    private String title;

    private Book parentBook;

    private List<Character> characters;

    public CommonLesson(String rawline, Book parentBook) {
        this.rawline = rawline;
        this.parentBook = parentBook;

        String[] elements = rawline.split(":");
        this.title = elements[0].trim();
        this.characters = buildCharacters(elements[1].trim());
        
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

    private List<Character> buildCharacters(String line) {

        List<String> tokens = line.chars()
                .mapToObj(num -> String.valueOf((char) num).trim())
                .filter(c -> !c.isEmpty())
                .collect(Collectors.toList());

        return tokens.stream()
                .map(token -> CharacterFactory.getCharacter(token, this))
                .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return "<" + getTitle() + "> " + getCharacters().stream().map(Character::getText).collect(Collectors.joining(", "));
    }
}
