package com.xubo.data.word;

import com.xubo.utils.ChineseResourceReader;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class WordsRepository {

    private Map<String, Set<String>> words = new HashMap<>();

    public WordsRepository() {

        List<String>  rawLines = new ArrayList<>();

        rawLines.addAll(ChineseResourceReader.readLines("/words/沪教版小学1-5年级词语表.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/人教版小学语文一至六年级生字词语汇总.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/手动添加.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/words_class_1.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLines("/words/words_class_2.txt", StandardCharsets.UTF_8.toString()));

        List<String> wordsFound = rawLines.stream()
                .map(String::trim)
                .filter(line -> !line.startsWith("#"))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(String::trim)
                .filter(WordsRepository::isValidWord)
                .distinct()
                .collect(Collectors.toList());

        wordsFound.forEach(word -> {
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

    private static boolean isValidWord(String word) {
        if (word.equals("")) {
            return false;
        }

        try {
            Integer.valueOf(word);
            return false;
        } catch (NumberFormatException e) {

        }

        return true;
    }
}
