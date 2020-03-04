package com.xubo.data.dictionary;

import com.xubo.utils.ChineseResourceReader;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordsRepository {

    private List<String> rawLines;

    private Map<String, Set<String>> words = new HashMap<>();

    public WordsRepository() {
        rawLines = new ArrayList<>();

        rawLines.addAll(ChineseResourceReader.readLines("/words/words_class_1.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/words_class_2.txt", StandardCharsets.UTF_8.toString()));

        rawLines.stream()
                .map(String::trim)
                .distinct()
                .forEach(word -> {
                    word.chars().forEach(c -> {
                        String key = String.valueOf((char) c);
                        this.words.putIfAbsent(key, new LinkedHashSet<>());
                        this.words.get(key).add(word);
                    });
                });

    }

    public Set<String> getWords(String character) {
        return words.get(character);
    }
}
