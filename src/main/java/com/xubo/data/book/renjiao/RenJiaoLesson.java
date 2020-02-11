package com.xubo.data.book.renjiao;

import com.xubo.data.character.Character;
import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.CharacterFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RenJiaoLesson implements Lesson {

    public static final String SEPRATER = "、";

    private String rawline;

    private String title;

    private Book parentBook;

    private List<Character> characters;

    public RenJiaoLesson(String group, String rawLine, RenJiaoBook parentBook) {
        this.rawline = rawLine;
        this.parentBook = parentBook;

        String[] elements = rawLine.split(SEPRATER, 2);
        this.title = group + elements[0];

        this.characters = buildCharacters(elements[1]);
    }

    @Override
    public String toString() {
        return "<" + getTitle() + "> " + getCharacters().stream().map(Character::getText).collect(Collectors.joining(", "));
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
                .filter(c -> !c.equals(SEPRATER))
                .collect(Collectors.toList());

        return tokens.stream()
                .map(token -> CharacterFactory.getCharacter(token, this))
                .collect(Collectors.toList());
    }
}
