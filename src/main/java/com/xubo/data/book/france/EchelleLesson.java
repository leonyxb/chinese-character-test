package com.xubo.data.book.france;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.character.FrenchCharacter;
import com.xubo.data.dictionary.DictionaryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EchelleLesson implements Lesson {

    private int index;

    private List<String> rawLines = new ArrayList<>();

    private EchelleBook parentBook;

    private List<Character> characters;

    public EchelleLesson(int index, List<String> rawLines, EchelleBook book) {
        this.index = index;
        this.rawLines.addAll(rawLines);
        this.parentBook = book;
        this.characters = buildCharacters(rawLines);
    }

    private List<Character> buildCharacters(List<String> rawLines) {
        return rawLines.stream()
                .map(this::buildCharacter)
                .collect(Collectors.toList());
    }

    private Character buildCharacter(String line) {
        String[] elements = line.split("\t");
        FrenchCharacter character = new FrenchCharacter(elements[2]);

        character.setCategory(elements[3]);
        character.setGenre(elements[4]);

        character.getWords().add(elements[4]);

        character.getLessons().add(this);

        return character;
    }

    @Override
    public String getTitle() {
        return String.valueOf(index);
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
        return "<" + getTitle() + "> " + getCharacters().stream()
                .map(Character::getText)
                .collect(Collectors.joining(", "));
    }
}
