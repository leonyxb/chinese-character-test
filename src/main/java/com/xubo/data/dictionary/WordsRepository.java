package com.xubo.data.dictionary;

import com.xubo.utils.ChineseResourceReader;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class WordsRepository {

    private List<String> rawLines;

    private Map<String, List<String>> words = new HashMap<>();

    public WordsRepository() {
        rawLines = new ArrayList<>();

        rawLines.addAll(ChineseResourceReader.readLines("/words/words.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/words_chengyu.txt", StandardCharsets.UTF_8.toString()));

        rawLines.stream()
                .map(String::trim)
                .distinct()
                .forEach(word -> {
                    word.chars().forEach(c -> {
                        String key = String.valueOf((char) c);
                        this.words.putIfAbsent(key, new ArrayList<>());
                        this.words.get(key).add(word);
                    });
                });
    }

    public List<String> getWords(String character) {
        return words.get(character);
    }
}
