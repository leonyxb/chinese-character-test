package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InMemoryBook implements Book {

    private static final int CHARACTER_NUM_OF_LESSON = 10;

    private String title;

    private List<Character> characters;

    private List<Lesson> lessons = new ArrayList<>();

    public InMemoryBook(String title, List<Character> characters) {
        this.title = title;
        this.characters = characters;

        Map<Integer, List<Character>> groupedList = IntStream.range(0, characters.size())
                .boxed()
                .collect(
                        Collectors.groupingBy(
                                i -> i / CHARACTER_NUM_OF_LESSON + 1,
                                Collectors.mapping(
                                        characters::get,
                                        Collectors.toList()
                                )
                        )
                );

        groupedList.forEach((k, v) -> {
            lessons.add(new InMemoryLesson(String.valueOf(k), v, this));
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<Lesson> getLessons() {
        return lessons;
    }

    @Override
    public boolean display() {
        return true;
    }

    @Override
    public String toString() {
        return getTitle() + " " + characters.size();
    }
}
