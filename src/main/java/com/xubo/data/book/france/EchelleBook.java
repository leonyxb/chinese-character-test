package com.xubo.data.book.france;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.book.common.InMemoryLesson;
import com.xubo.data.character.Character;
import com.xubo.data.character.FrenchWord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EchelleBook implements Book {

    private String classe;

    private List<FrenchWord> characters;

    private List<Lesson> lessons;

    public EchelleBook(String classe, List<FrenchWord> characters) {
        this.classe = classe;
        this.characters = characters;
        this.lessons = buildLessons(characters);
    }

    private List<Lesson> buildLessons(List<FrenchWord> characters) {
        List<Lesson> lessons = new ArrayList<>();
        Map<Integer, List<FrenchWord>> charactersByEchelle = characters.stream().collect(Collectors.groupingBy(c -> c.getEchelle()));

        charactersByEchelle.keySet().stream().sorted().forEach(k -> {
            lessons.addAll(buildEchelleLessons(k, charactersByEchelle.get(k)));
        });
        return lessons;
    }

    private List<Lesson> buildEchelleLessons(Integer echelle, List<FrenchWord> characters) {
        List<Lesson> lessons = new ArrayList<>();

        characters.sort(Comparator.comparingInt(Object::hashCode));

        Map<Integer, List<Character>> groupedList = IntStream.range(0, characters.size())
                .boxed()
                .collect(
                        Collectors.groupingBy(
                                i -> i / 5 + 1,
                                Collectors.mapping(
                                        characters::get,
                                        Collectors.toList()
                                )
                        )
                );

        groupedList.forEach((k, v) -> {
            lessons.add(new InMemoryLesson( echelle + "-" + k, v, this));
        });

        return lessons;
    }

    @Override
    public String getTitle() {
        return "Echelle: " + classe + ",  " + characters.size() + " mots";
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
        return getTitle();
    }
}
