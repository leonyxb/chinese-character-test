package com.xubo.data.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GermanWord extends Character {

    private String url;

    private String wordClass;

    private String genre;

    private String level;

    private String frequency;

    private String originalText;

    public GermanWord(String text) {
        super(text, "DE");
    }

    @Override
    public String toString() {
        String display = getText();
        if (wordClass.equals("Noun")) {
            display = display + " [" + getGenre() + "]";
        }

        display = display + "[Lv:" + getLevel() + "]";
        display = display + "[F:" + getFrequency() + "]";
        return display;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWordClass() {
        return wordClass;
    }

    public void setWordClass(String wordClass) {
        this.wordClass = wordClass;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public List<GermanWord> split() {
        List<GermanWord> words = new ArrayList<>();
        if (this.getText().contains(",")) {

            Arrays.stream(this.getText().split(",")).forEach(k -> {
                GermanWord newWord = new GermanWord(k.trim());
                newWord.setWordClass(this.getWordClass());
                newWord.setLevel(this.getLevel());
                newWord.setFrequency(this.getFrequency());
                newWord.getWords().addAll(this.getWords());
                newWord.setGenre(this.getGenre());
                newWord.getDictionaryEntries().addAll(this.getDictionaryEntries());
                newWord.setUrl(this.getUrl());
                newWord.setOriginalText(this.getOriginalText());
                words.add(newWord);
            });

        } else {
            words.add(this);
        }
        return words;
    }

    public void merge(GermanWord word) {
        this.setWordClass(this.getWordClass() + ", " + word.getWordClass());
        this.generateDescription();
    }

    public void generateDescription() {

        this.getWords().clear();

        if (this.originalText.contains(",")) {
            this.getWords().add(originalText);
        }
        this.getWords().add("Class: " + this.wordClass);
        if (this.genre != null) {
            this.getWords().add("Genre: " + this.genre);
        }
        this.getWords().add("Lv: " + this.level);
    }
}
