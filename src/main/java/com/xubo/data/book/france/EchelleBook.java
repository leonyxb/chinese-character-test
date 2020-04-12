package com.xubo.data.book.france;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EchelleBook implements Book {

    private Integer echelle;

    private String classe;

    private List<String> rawLines;

    private List<Lesson> lessons;


    public EchelleBook(Integer echelle, List<String> rawLines) {
        this.echelle = echelle;
        this.classe = rawLines.get(0).split("\t")[1];
        this.rawLines = rawLines;
        this.lessons = buildLessons(rawLines);
    }

    private List<Lesson> buildLessons(List<String> rawLines) {
        List<Lesson> lessons = new ArrayList<>();

        List<String> lines = new ArrayList<>();

        rawLines.sort(Comparator.comparingInt(String::hashCode));

        int index = 1;
        for (String rawLine : rawLines) {
            lines.add(rawLine);
            if (lines.size() == 5) {
                lessons.add(new EchelleLesson(index++, lines, this));
                lines.clear();
            }
        }
        if (!lines.isEmpty()) {
            lessons.add(new EchelleLesson(index, lines, this));
        }
        return lessons;
    }

    @Override
    public String getTitle() {
        return "Echelle " + echelle + ":  " + classe + ",  " + rawLines.size() + " mots";
    }

    @Override
    public List<Lesson> getLessons() {
        return lessons;
    }

    @Override
    public boolean display() {
        return true;
    }

    public Integer getEchelle() {
        return echelle;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
