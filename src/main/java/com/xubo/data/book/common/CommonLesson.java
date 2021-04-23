package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommonLesson implements Lesson {

    private static final Logger logger = LogManager.getLogger(CommonLesson.class);

    private String rawline;

    private String title;

    private Book parentBook;

    private List<Character> characters;

    public CommonLesson(String rawline, Book parentBook) {
        this.rawline = rawline;
        this.parentBook = parentBook;

        try {
            String[] elements = rawline.split(":");
            this.title = elements[0].trim();
            if (elements.length >= 2) {
                this.characters = buildCharacters(elements[1].trim());
            } else {
                logger.error("    此行只有标题: " + rawline);
                this.characters = Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("    处理此行时遇到异常: " + rawline);
            throw e;
        }
        
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

        List<String> tokens = Arrays.stream(line.trim().split(" "))
                .map(String::trim)
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
