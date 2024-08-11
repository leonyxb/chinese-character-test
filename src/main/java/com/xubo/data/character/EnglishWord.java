package com.xubo.data.character;

import java.util.List;

public class EnglishWord extends Character {

    private String partOfSpeech;

    private Integer frequency;

    private List<String> inflections;

    public EnglishWord(String text) {
        super(text, "EN");
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public List<String> getInflections() {
        return inflections;
    }

    public void setInflections(List<String> inflections) {
        this.inflections = inflections;
    }
}
