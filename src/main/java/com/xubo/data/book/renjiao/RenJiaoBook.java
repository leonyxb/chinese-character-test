package com.xubo.data.book.renjiao;

import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;

import java.util.ArrayList;
import java.util.List;

public class RenJiaoBook implements Book {

    private String title;

    private String subTitle;

    private long num = 0;

    private List<Lesson> lessons;

    private List<String> rawLines = new ArrayList<>();
    
    private boolean display;

    public RenJiaoBook(String title, List<String> rawLines) {
        this.rawLines.addAll(rawLines);
        this.title = title;
        this.subTitle = rawLines.get(0).trim()
                .replace(" ", "")
                .replace("（", "(")
                .replace("）", ")");
        this.lessons = buildLessons(rawLines.subList(1, rawLines.size()));
        this.num = lessons.stream().mapToLong(lesson -> lesson.getCharacters().size()).sum();
        this.display = !subTitle.contains("生字表(二)");
    }

    private List<Lesson> buildLessons(List<String> rawLines) {
        List<Lesson> lessons = new ArrayList<>();

        String group = "";
        for (String rawLine : rawLines) {
            if (rawLine.contains("个字")) {
                //useless line
            } else if (rawLine.contains("、")) {
                lessons.add(new RenJiaoLesson(group, rawLine, this));
            } else {
                group = rawLine;
            }

        }
        return lessons;
    }

    @Override
    public String getTitle() {
        return title + " " + num + "字";
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
        return display;
    }
}
