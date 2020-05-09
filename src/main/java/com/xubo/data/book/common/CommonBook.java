package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommonBook implements Book {

    private String title;
    
    private List<Lesson> lessons;
    
    private List<String> rawLines = new ArrayList<>();
    
    public CommonBook(List<String> bookLines) {
        this.rawLines.addAll(normalized(bookLines));

        this.title = rawLines.get(0).replace("Book:", "").trim();
        this.lessons = rawLines
                .subList(1, rawLines.size())
                .stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> new CommonLesson(line, this))
                .collect(Collectors.toList());
    }

    private List<String> normalized(List<String> bookLines) {
        List<String> lines = new ArrayList<>();

        bookLines.forEach(l -> {

            if (!l.contains(":")) {
                l = l.replaceAll("[：|,|，|#|;|；|。|.]", ":");
                if (!l.contains(":")) {
                    l = l.replaceFirst(" ", ":");
                }
                lines.add(l);
            } else {
                lines.add(l);
            }

        });

        return lines;
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
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean display() {
        return true;
    }
}
